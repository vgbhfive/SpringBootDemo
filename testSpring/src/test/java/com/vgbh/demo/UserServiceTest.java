package com.vgbh.demo;

import com.vgbh.demo.domain.User;
import com.vgbh.demo.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void findOneTest () {
        User user = userService.findOne(5);

        Assert.assertEquals(new Integer(17), user.getAge());
    }


}

