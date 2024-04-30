package com.bichukov.rpsgame.dao;

import com.bichukov.rpsgame.domain.Game;

import java.util.concurrent.atomic.AtomicLong;

public class GameDao extends AbstractConcurrentDao<Long, Game> {
    private AtomicLong idGenerator = new AtomicLong();

    @Override
    public Long incrementAndGet() {
        return idGenerator.incrementAndGet();
    }
}
