package cn.vgbhfive.actuatordemo.config;

import cn.vgbhfive.actuatordemo.endpoint.MyEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @time: 2018/12/23
 * @author: Vgbh
 */
@Configuration
public class MyEndpointConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnEnabledEndpoint
    public MyEndpoint myEndpoint() {
        return new MyEndpoint();
    }

}
