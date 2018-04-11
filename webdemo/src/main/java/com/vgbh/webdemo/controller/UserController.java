package com.vgbh.webdemo.controller;


import com.vgbh.webdemo.domain.User;
import com.vgbh.webdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService; = userService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User save (@RequestParam String name) {
        User user = new User();
        user.setName(name);
        if (userService.save(user)) {
            System.out.println(user.toString());
        }
        return user;
    }


}
