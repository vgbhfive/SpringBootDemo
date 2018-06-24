package cn.vgbhfive.bookdemo3.ch1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @time:
 * @author: Vgbh
 */
@Controller
public class HelloController {

    @RequestMapping("/index")
    public String hello () {
        return "index";
    }

}
