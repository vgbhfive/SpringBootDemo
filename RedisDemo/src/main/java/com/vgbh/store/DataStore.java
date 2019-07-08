package com.vgbh.store;

/**
 * @param <K>
 * @param <V>
 * @time: 2018-04-09
 * @author: Vgbh
 * <p>
 * 数据基类
 */


public interface DataStore<K, V> {

    PutStatus put(K key, V value);

    ValueHolder<V> get(K key);

    ValueHolder<V> remove(K key);

    void clear();

}
