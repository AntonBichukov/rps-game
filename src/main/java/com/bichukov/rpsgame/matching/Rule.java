package com.bichukov.rpsgame.matching;

import com.bichukov.rpsgame.domain.Game;
import com.bichukov.rpsgame.domain.User;

import java.util.Collection;
import java.util.List;

public interface Rule {

    List<Game> match(User user, Collection<Game> games);
}
