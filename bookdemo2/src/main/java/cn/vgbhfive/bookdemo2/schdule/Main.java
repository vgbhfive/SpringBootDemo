package cn.vgbhfive.bookdemo2.schdule;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @time:
 * @author: Vgbh
 */
public class Main {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskSchdulerConfig.class);

    }

}
