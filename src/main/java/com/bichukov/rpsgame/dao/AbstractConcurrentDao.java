package com.bichukov.rpsgame.dao;

import com.bichukov.rpsgame.domain.IdGenerated;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractConcurrentDao<K, V extends IdGenerated<K>> implements DaoRepository<K, V> {
    private Map<K, V> entries = new ConcurrentHashMap();

    public abstract K incrementAndGet();

    public Map<K, V> getAll() {
        return entries;
    }

    public Optional<V> getById(K id) {
        return Optional.ofNullable(entries.get(id));
    }

    @Override
    public V save(V value) {
        if (value.getId() == null) {
            value.setId(incrementAndGet());
        }
        return entries.put(value.getId(), value);
    }

    @Override
    public void delete(V value) {
        deleteById(value.getId());
    }

    @Override
    public void deleteById(K id) {
        entries.remove(id);
    }

}
