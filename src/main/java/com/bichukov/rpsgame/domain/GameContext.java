package com.bichukov.rpsgame.domain;

import lombok.Data;

import java.util.List;

@Data
public class GameContext {
    private List<User> users;

    private Game game;
}
