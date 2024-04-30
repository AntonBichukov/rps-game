package com.bichukov.rpsgame.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game implements IdGenerated<Long> {
    private Long id;
    private List<User> users = new ArrayList<>();
    private GameState state = GameState.CREATED;

    public void addUser(User user) {
        users.add(user);
    }

    public boolean allUsersWithOptions() {
        return users.stream().noneMatch(u -> u.getOption().equals(GameOption.EMPTY));
    }
}
