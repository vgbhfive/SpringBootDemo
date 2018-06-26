package cn.vgbhfive.bookdemo3sr.controller;

import cn.vgbhfive.bookdemo3sr.domain.DemoObj;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @time:
 * @author: Vgbh
 */
@Controller
public class DemoController {

    @RequestMapping("/advice")
    public String getSomething (@ModelAttribute("msg") String msg, DemoObj obj) {
        try {
            throw new IllegalAccessException("status error from attribute " + msg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "ssss";
    }

    @RequestMapping("index")
    public String hello (){
        return "index";
    }
    /*
        在实际中经常会在配置中重写addViewControllers来简化配置。
     */


}
