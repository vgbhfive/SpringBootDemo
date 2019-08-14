package cn.vgbhfive.springbootstarterdemotest;

import cn.vgbhfive.annotation.EnableVgbhfiveCient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableVgbhfiveCient
@SpringBootApplication
public class SpringbootstarterdemotestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootstarterdemotestApplication.class, args);
    }

}
