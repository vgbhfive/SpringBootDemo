package com.vgbh.webdemo.service;


import com.vgbh.webdemo.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserService {

    private final static ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();

    private AtomicInteger idGenerator = new AtomicInteger();

    public boolean save (User user) {
        Integer id = idGenerator.incrementAndGet();
        user.setId(id);
        return users.put(id, user) == null;
    }

    public Collection<User> findAll () {
        return users.values();
    }

}
