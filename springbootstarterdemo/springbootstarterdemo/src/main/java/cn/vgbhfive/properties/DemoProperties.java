package cn.vgbhfive.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置参数类
 *
 * @time: 2019/08/13
 * @author: Vgbh
 */
@Data
@ConfigurationProperties("spring.vgbhfive")
public class DemoProperties {

    private String name;

}
