package com.vgbh.store.impl;

import com.vgbh.store.DataStore;
import com.vgbh.store.PutStatus;
import com.vgbh.store.ValueHolder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @time：2018-04-09
 * @author: Vgbh
 */

public class LRUDataStore<K, V> implements DataStore<K, V> {

    @Override
    public PutStatus put(K key, V value) {

        return null;
    }

    @Override
    public ValueHolder<V> get(K key) {
        return null;
    }

    @Override
    public ValueHolder<V> remove(K key) {
        return null;
    }

    @Override
    public void clear() {

    }


}
