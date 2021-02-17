package cn.vgbhfive.doglicking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DoglickingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoglickingApplication.class, args);
    }

}
