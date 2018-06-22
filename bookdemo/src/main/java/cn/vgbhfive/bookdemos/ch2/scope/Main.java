package cn.vgbhfive.bookdemos.ch2.scope;

import cn.vgbhfive.bookdemos.ch2.configuration.ScopeConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @time:
 * @author: Vgbh
 */
public class Main {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScopeConfig.class);

        DemoSingletonService s1 = context.getBean(DemoSingletonService.class);
        DemoSingletonService s2 = context.getBean(DemoSingletonService.class);

        DemoPrototypeService p1 = context.getBean(DemoPrototypeService.class);
        DemoPrototypeService p2 = context.getBean(DemoPrototypeService.class);

        System.out.println("s1与s2是否相等：" + s1.equals(s2));
        System.out.println("p1与p2是否相等：" + p1.equals(p2));

        context.close();
    }

    /*
        Singleton:全局共一个Bean的实例。
        Prototype:为每次调用创建新的实例
        Request:为每一个Http request请求创建一个Bean实例
        Session:为每一个Http session请求创建一个Bean实例
        GlobalSession:这个只在portal应用中有用，给每一个global Http session请求创建一个Bean实例
     */
}
