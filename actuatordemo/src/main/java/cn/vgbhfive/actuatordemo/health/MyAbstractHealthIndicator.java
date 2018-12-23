package cn.vgbhfive.actuatordemo.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * 自定义健康端点
 * 功能强大(DataSourceHealthIndicator / RedisHealthINdicator都是这种写法)
 *
 * @time: 2018/12/23
 * @author: Vgbh
 */
@Component("myHealthTwo")
public class MyAbstractHealthIndicator extends AbstractHealthIndicator {

    private static final String VERSION = "v1.0.0";

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        int code = check();
        if (code != 0) {
            builder.down().withDetail("code", code).withDetail("version", VERSION).build();
        }
        builder.up().withDetail("code", code).withDetail("version", VERSION).build();
    }

    private int check() {
        return 0;
    }
}
