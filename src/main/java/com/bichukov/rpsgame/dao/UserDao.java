package com.bichukov.rpsgame.dao;

import com.bichukov.rpsgame.domain.User;

public class UserDao extends AbstractConcurrentDao<String, User> {

    @Override
    public String incrementAndGet() {
        throw new UnsupportedOperationException("User should get id from connection");
    }
}
