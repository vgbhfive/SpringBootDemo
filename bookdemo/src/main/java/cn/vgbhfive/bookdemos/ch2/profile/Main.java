package cn.vgbhfive.bookdemos.ch2.profile;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.annotation.Annotation;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @time:
 * @author: Vgbh
 */
public class Main {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.getEnvironment().setActiveProfiles("prod");
        context.register(ProfileConfig.class);
        context.refresh();

        DemoBean demoBean = context.getBean(DemoBean.class);

        System.out.println(demoBean.getContext() + "-----------------------");

        context.close();

    }

}
