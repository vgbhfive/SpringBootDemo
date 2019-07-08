package com.vgbh;

import com.vgbh.store.impl.WeakValueDataStore;
import org.junit.Test;

public class WeakTest {

    @Test
    public void TestWeakValue() throws InterruptedException {
        CsCache<String, Integer> cache = new CsCache<String, Integer>(new WeakValueDataStore<String, Integer>());

        String key = "asdfgh";
        Integer value = 345;

        cache.put(key, value);

        value = 0;

        System.out.print(value + "\n");

        value = cache.get(key);

        System.out.print(value + "\n");

        System.gc();
        Thread.sleep(10000);

        System.out.print(cache.get(key));

    }


}
