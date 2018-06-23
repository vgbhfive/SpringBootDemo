package cn.vgbhfive.bookdemo2.annotation;

import org.springframework.stereotype.Service;

/**
 * @time:
 * @author: Vgbh
 */
@Service
public class DemoService {

    public void outputResult () {
        System.out.println("从组合注解中获得Bean");
    }
}
