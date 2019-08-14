package cn.vgbhfive.springbootstarterdemotest;

import cn.vgbhfive.client.VgbhfiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @time: 2019/08/14
 * @author: Vgbh
 */
@RestController
public class DemoController {

    @Autowired
    private VgbhfiveClient vgbhfiveClient;

    @GetMapping("/demo")
    public String test() {
        String name = vgbhfiveClient.getName();
        if (null == name) {
            return "Hello World!";
        }
        return name;
    }

}
