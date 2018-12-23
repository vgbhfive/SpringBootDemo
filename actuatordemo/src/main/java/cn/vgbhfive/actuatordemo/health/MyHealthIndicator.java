package cn.vgbhfive.actuatordemo.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * 自定义健康端点
 *
 * @time: 2018/12/23
 * @author: Vgbh
 */
@Component("myHealthOne")
public class MyHealthIndicator implements HealthIndicator {

    private static final String VERSION = "v1.0.0";

    @Override
    public Health health() {
        int code = check();
        if (code != 0) {
            Health.down().withDetail("code", code).withDetail("version", VERSION).build();
        }
        return Health.up().withDetail("code", code).withDetail("version", VERSION).build();
    }

    private int check() {
        return 0;
    }
}
