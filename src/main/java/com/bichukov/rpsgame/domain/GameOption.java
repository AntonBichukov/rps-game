package com.bichukov.rpsgame.domain;

import java.util.Arrays;
import java.util.List;

public enum GameOption {
    ROCK(List.of("1", "камень", "rock", "kamen")),
    SCISSORS(List.of("2", "ножницы", "scissors", "nojnici")),
    PAPER(List.of("3", "бумага", "paper", "bumaga")),
    EMPTY(null);

    private List<String> names;

    GameOption(List<String> names) {
        this.names = names;
    }

    public static GameOption getByName(String name) {
        return Arrays.stream(GameOption.values()).filter(option -> option.names.contains(name.toLowerCase())).findFirst().orElse(EMPTY);
    }
}
