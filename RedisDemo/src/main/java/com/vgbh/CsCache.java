package com.vgbh;

import com.vgbh.store.DataStore;
import com.vgbh.store.PutStatus;
import com.vgbh.store.ValueHolder;

/**
 * @param <K>
 * @param <V>
 * @time：2018-04-09
 * @suthor: Vgbh
 * <p>
 * 接口类
 */

public class CsCache<K, V> {

    private final DataStore<K, V> store;

    public CsCache(final DataStore<K, V> dataStore) {
        this.store = dataStore;
    }

    public V get(final K key) {
        ValueHolder<V> value = store.get(key);
        return value.value();
    }

    public PutStatus put(K key, V value) {
        PutStatus putStatus = store.put(key, value);
        return putStatus;
    }

    public ValueHolder<V> remove(K key) {
        ValueHolder<V> value = store.remove(key);
        return value;
    }

    public void clear() {
        store.clear();
    }

}
