package cn.vgbhfive.config;

import cn.vgbhfive.client.VgbhfiveClient;
import cn.vgbhfive.properties.DemoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动创建服务类
 *
 * @time: 2019/08/13
 * @author: Vgbh
 */
@Configuration //开启配置
@EnableConfigurationProperties(DemoProperties.class) // 开启使用映射实体
public class VgbhfiveAutoConfigure {

    private static final Logger logger = LoggerFactory.getLogger(VgbhfiveAutoConfigure.class);

    @Bean
    public VgbhfiveClient vgbhfiveClient(DemoProperties demoProperties) {
        logger.info(" ----- VgbhfiveClient Created! ----- ");
        return new VgbhfiveClient(demoProperties);
    }

}
