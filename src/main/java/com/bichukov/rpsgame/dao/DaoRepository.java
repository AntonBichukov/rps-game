package com.bichukov.rpsgame.dao;

import java.util.Map;
import java.util.Optional;

public interface DaoRepository<K, V> {
    Map<K, V> getAll();

    Optional<V> getById(K id);

    V save(V game);

    void delete(V value);

    void deleteById(K id);


}
