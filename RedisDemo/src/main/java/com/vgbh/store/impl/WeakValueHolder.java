package com.vgbh.store.impl;

import com.vgbh.store.ValueHolder;

import java.lang.ref.WeakReference;

public class WeakValueHolder<V> implements ValueHolder<V> {

    private WeakReference<V> v;

    public WeakValueHolder(V value) {
        this.v = new WeakReference<V>(value);
    }

    @Override
    public V value() {
        return this.v.get();
    }

}
