package com.bichukov.rpsgame.service;

import com.bichukov.rpsgame.dao.UserDao;
import com.bichukov.rpsgame.domain.Game;
import com.bichukov.rpsgame.domain.GameOption;
import com.bichukov.rpsgame.domain.User;
import com.bichukov.rpsgame.exception.BadOptionException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public User get(String id) {
        return userDao.getById(id).orElseGet(() -> createUser(id));
    }

    public User setName(String id, String name) {
        User user = get(id);
        user.setName(name);
        return user;
    }

    public User setOption(String id, String option) throws BadOptionException {
        GameOption gameOption = GameOption.getByName(option);
        if (gameOption.equals(GameOption.EMPTY)) {
            throw new BadOptionException("Your option is not correct. Try again.");
        }
        User user = get(id);
        return user;
    }

    public User setGame(String id, Game game) {
        User user = get(id);
        user.setGameId(game.getId());
        return user;
    }

    private User createUser(String id) {
        User user = User.builder()
                .id(id)
                .option(GameOption.EMPTY)
                .build();
        userDao.save(user);
        return user;
    }

}
