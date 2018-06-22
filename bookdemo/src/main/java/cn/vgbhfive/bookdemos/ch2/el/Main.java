package cn.vgbhfive.bookdemos.ch2.el;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @time:
 * @author: Vgbh
 */

public class Main {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ElConfig.class);

        ElConfig elConfig = context.getBean(ElConfig.class);

        elConfig.outputSource();

        context.close();
    }

}
