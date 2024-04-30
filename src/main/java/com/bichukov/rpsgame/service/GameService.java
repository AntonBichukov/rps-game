package com.bichukov.rpsgame.service;

import com.bichukov.rpsgame.dao.GameDao;
import com.bichukov.rpsgame.domain.Game;
import com.bichukov.rpsgame.domain.GameOption;
import com.bichukov.rpsgame.domain.User;
import com.bichukov.rpsgame.tcp.Broadcaster;
import com.bichukov.rpsgame.tcp.ConnectionsHolder;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Map;

import static com.bichukov.rpsgame.domain.GameOption.EMPTY;
import static com.bichukov.rpsgame.domain.GameState.CALCULATION;
import static com.bichukov.rpsgame.domain.GameState.FINISHED;
import static com.bichukov.rpsgame.domain.GameState.IN_PROGRESS;
import static com.bichukov.rpsgame.domain.GameState.READY;
import static com.bichukov.rpsgame.domain.Result.LOSE;
import static com.bichukov.rpsgame.domain.Result.TIE;
import static com.bichukov.rpsgame.domain.Result.WIN;

@RequiredArgsConstructor
public class GameService {

    private final Broadcaster broadcaster;
    private final UserService userService;
    private final GameDao gameDao;
    private final MatchingService matchingService;

    private Comparator<User> optionComparator = User::compareTo;

    public void precessGameStep(String connectionId, String message) {
        User user = userService.get(connectionId);
        checkName(user, message);

        playGame(user, message);
    }

    private void checkName(User user, String name) {
        if (user.getName() == null || user.getName().isEmpty()) {
            userService.setName(user.getId(), name);
        }
    }

    private void playGame(User user, String message) {
        Game game = selectGame(user);
        switch (game.getState()) {
            case CREATED -> gameCreatedStepProcess(user, game);
            case READY -> gameReadyStepProcess(game);
            case IN_PROGRESS -> gameInProgressStepProcess(user, game, message);
            case CALCULATION -> gameWinnerCalculationProcess(game);
            case FINISHED -> closeConnections(game);
        }
    }

    public Game selectGame(User user) {
        Game selectedGame;
        if (user.getGameId() != null) {
            selectedGame = gameDao.getById(user.getGameId()).get();
        } else {
            Map<Long, Game> games = gameDao.getAll();
            selectedGame = matchingService.match(user, games.values()).orElseGet(() -> createGame(user));
            selectedGame.addUser(user);
            user.setGameId(selectedGame.getId()); //need to change places with previous string
        }
        return selectedGame;
    }

    private Game createGame(User user) {
        Game game = new Game();
        gameDao.save(game);
        return game;
    }

    private void gameCreatedStepProcess(User user, Game game) {
        if (game.getUsers().size() == 2) {
            game.setState(READY);
            playGame(user, null);
        } else {
            broadcaster.helloMessage(user);
        }
    }

    private void gameReadyStepProcess(Game game) {
        game.setState(IN_PROGRESS);
        broadcaster.gameReadyMessage(game);
    }

    private void gameInProgressStepProcess(User user, Game game, String message) {
        GameOption option = GameOption.getByName(message);
        if (option.equals(EMPTY)) {
            processIncorrectOptionMessage(user);
        } else {
            processCorrectOptionMessage(user, game, message);
        }
    }

    private void processIncorrectOptionMessage(User user) {
        if (user.getOption() != null && !user.getOption().equals(EMPTY)) {
            broadcaster.waitingMessage(user);
        } else {
            broadcaster.wrongOptionMessage(user);
        }
    }

    private void processCorrectOptionMessage(User user, Game game, String message) {
        user.setOption(GameOption.getByName(message));
        if (game.allUsersWithOptions()) {
            game.setState(CALCULATION);
            playGame(user, null);
        } else {
            broadcaster.waitingMessage(user);
        }
    }

    private void gameWinnerCalculationProcess(Game game) {
        User user1 = game.getUsers().get(0);
        User user2 = game.getUsers().get(1);

        switch (optionComparator.compare(user1, user2)) {
            case 0 -> {
                user1.setResult(TIE);
                user2.setResult(TIE);
                game.setState(READY);
            }
            case 1 -> {
                user1.setResult(WIN);
                user2.setResult(LOSE);
                game.setState(FINISHED);
            }
            case -1 -> {
                user1.setResult(LOSE);
                user2.setResult(WIN);
                game.setState(FINISHED);
            }
        }
        broadcaster.resultMessage(game);
        playGame(user1, null);
    }

    public void closeConnections(Game game) {
        game.getUsers().forEach(u -> ConnectionsHolder.getInstance().killSession(u.getId()));
    }
}
