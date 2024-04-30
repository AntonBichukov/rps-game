package com.bichukov.rpsgame.matching;

import com.bichukov.rpsgame.domain.Game;
import com.bichukov.rpsgame.domain.GameState;
import com.bichukov.rpsgame.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CreatedGameRule implements Rule {

    @Override
    public List<Game> match(User user, Collection<Game> games) {
        List<Game> gameList = games.stream()
                .filter(game -> game.getState().equals(GameState.CREATED))
                .collect(Collectors.toList());
        return gameList;
    }
}
