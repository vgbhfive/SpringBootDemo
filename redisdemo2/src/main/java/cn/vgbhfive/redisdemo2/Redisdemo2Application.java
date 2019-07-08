package cn.vgbhfive.redisdemo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching //开启缓存
public class Redisdemo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Redisdemo2Application.class, args);
    }

    /**
     * @EnableCaching：开启缓存后，Spring Boot 会在后台配置一个RedisCacheManager 的Bean。
     * 具体在RedisCacheConfiguration 类中完成。
     */

}
