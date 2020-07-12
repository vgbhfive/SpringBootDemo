package cn.vgbhfive.mpdemo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Vgbh
 * @date 2020/7/12 15:29
 */
@Configuration
@MapperScan("cn.vgbhfive.mpdemo.mapper")
public class MpConfig {
}
