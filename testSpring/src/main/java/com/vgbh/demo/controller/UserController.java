package com.vgbh.demo.controller;

import com.vgbh.demo.Util.ResultUtil;
import com.vgbh.demo.domain.Result;
import com.vgbh.demo.domain.User;
import com.vgbh.demo.repositories.UserRepository;
import com.vgbh.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> userList () {
        return userRepository.findAll();
    }

    /**
     * 添加一个用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result<User> addUser (@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info(bindingResult.getFieldError().getDefaultMessage());//日志

            return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
        }
        user.setAge(user.getAge());
        user.setName(user.getName());

        return ResultUtil.success(userRepository.save(user));
    }


    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User findOne (@PathVariable("id") Integer id) {
        return userRepository.findOne(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public User updateOne (@PathVariable("id") Integer id,
                           @RequestParam("name") String name,
                           @RequestParam("age") Integer age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);

        return userRepository.save(user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String deleteOne (@PathVariable("id") Integer id) {
        userRepository.delete(id);
        return "Delete OK!";
    }

    /**
     * 测试统一异常处理
     * @param id
     * @throws Exception
     */
    @RequestMapping(value = "/user/age/{id}", method = RequestMethod.GET)
    public void getAge (@PathVariable("id") Integer id) throws Exception{
        userService.getAge(id);
    }

}

