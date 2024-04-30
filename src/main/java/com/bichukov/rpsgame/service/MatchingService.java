package com.bichukov.rpsgame.service;

import com.bichukov.rpsgame.domain.Game;
import com.bichukov.rpsgame.domain.User;
import com.bichukov.rpsgame.matching.Rule;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class MatchingService {

    private final Rule matchingRule;

    public Optional<Game> match(User user, Collection<Game> games) {
        Optional<Game> game = matchingRule.match(user, games).stream().findFirst();
        return game;
    }
}
