package cn.vgbhfive.jibtest.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author: Vgbh
 * @date: 2019/12/20 11:27
 */
@RestController
@RequestMapping("/test")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

}
