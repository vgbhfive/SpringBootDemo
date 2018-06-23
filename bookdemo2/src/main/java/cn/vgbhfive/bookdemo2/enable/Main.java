package cn.vgbhfive.bookdemo2.enable;

import java.util.SplittableRandom;

/**
 * @time:
 * @author: Vgbh
 */
public class Main {

    public static void main (String[] args) {
        System.out.println("Hello World!");
    }

    /*
        @EnableAspectJAutoProxy开启对AspectJ自动代理的支持
        @EnableAsync开启对异步方法的支持
        @EnableScheduing开启对计划任务的支持
        @EnableWebMvc开启对Web Mvc的支持
        @EnableConfigurationProperties开启对@ConfigurationProperties注解配置Bean的支持
        @EnableJpaRepositories开启对Spring Data JPA Repository的支持
        @EnableTransactionManagment开启注解式事务的支持
        @EnableCaching开启注解式的缓存支持

        通过简单的@Enable*来开启一项功能的支持，从而避免自己配置大量的代码，大大降低使用难度。

        每一个@Enable*中都有一个@Import注解，@Import被用来导入配置注解类的，这也就意味着这些自动开启的实现
        其实是导入了一些自动配置的Bean。
    */

}
