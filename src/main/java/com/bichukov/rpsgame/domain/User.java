package com.bichukov.rpsgame.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User implements IdGenerated<String>, Comparable<User> {
    private String id;
    private String name;
    private GameOption option;
    private Long gameId;
    private Result result;

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public int compareTo(User otherUser) {
        if (option.equals(otherUser.option)) {
            return 0;
        }

        return switch (option) {
            case ROCK -> otherUser.option == GameOption.SCISSORS ? 1 : -1;
            case PAPER -> otherUser.option == GameOption.ROCK ? 1 : -1;
            case SCISSORS -> otherUser.option == GameOption.PAPER ? 1 : -1;
            case EMPTY -> 0;
        };
    }
}
