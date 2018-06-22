package cn.vgbhfive.bookdemos.ch2.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * @time:
 * @author: Vgbh
 */
public class ProfileConfig {

    @Bean
    @Profile("dev")
    public DemoBean devDemoBean () {
        return new DemoBean("from development profile");
    }

    @Bean
    @Profile("prod")
    public DemoBean prodDemoBean () {
        return new DemoBean("from produce profile");
    }

}
