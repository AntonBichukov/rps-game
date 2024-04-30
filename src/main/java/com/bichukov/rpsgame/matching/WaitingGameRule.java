package com.bichukov.rpsgame.matching;

import com.bichukov.rpsgame.domain.Game;
import com.bichukov.rpsgame.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WaitingGameRule implements Rule {
    private final Rule delegate;

    @Override
    public List<Game> match(User user, Collection<Game> games) {
        List<Game> gameList = delegate.match(user, games.stream()
                .filter(game -> game.getUsers().size() == 1)
                .filter(game -> !game.getUsers().get(0).getId().equals(user.getId()))
                .collect(Collectors.toList()));
        return gameList;
    }
}
