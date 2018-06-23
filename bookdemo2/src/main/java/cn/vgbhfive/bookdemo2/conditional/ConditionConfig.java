package cn.vgbhfive.bookdemo2.conditional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @time:
 * @author: Vgbh
 */
@Configuration
@ComponentScan("cn.vgbhfive.bookdemo2.conditional")
public class ConditionConfig {

    @Bean
    @Conditional(WindowsCondition.class)
    public ListService windowsListService () {
        return new WindowsListService();
    }

    @Bean
    @Conditional(LinuxCondition.class)
    public ListService linuxListService () {
        return new LinuxListService();
    }

}
