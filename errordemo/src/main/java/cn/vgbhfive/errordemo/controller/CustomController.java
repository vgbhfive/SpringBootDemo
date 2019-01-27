package cn.vgbhfive.errordemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常演示接口
 *
 * @time: 2019/1/27
 * @author: Vgbh
 */
@RestController
public class CustomController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        int i = 3 / 0;
        return "Hello World! -> " + i;
    }

}
