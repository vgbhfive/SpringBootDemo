package com.vgbh.store.impl;

import com.vgbh.store.DataStore;
import com.vgbh.store.PutStatus;
import com.vgbh.store.ValueHolder;

import java.util.concurrent.ConcurrentHashMap;

public class WeakValueDataStore<K, V> implements DataStore<K, V> {

    ConcurrentHashMap<K, ValueHolder<V>> map = new ConcurrentHashMap<K, ValueHolder<V>>();

    @Override
    public PutStatus put(Object key, Object value) {
        ValueHolder<V> v = new WeakValueHolder<V>((V) value);
        map.put((K) key, v);
        return PutStatus.PUT;
    }

    @Override
    public ValueHolder<V> get(K key) {
        return map.get(key);
    }

    @Override
    public ValueHolder<V> remove(K key) {
        return map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }


}
