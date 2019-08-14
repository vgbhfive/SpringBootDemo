package cn.vgbhfive.annotation;

import cn.vgbhfive.config.VgbhfiveAutoConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启服务注解
 *
 * @time: 2019/08/14
 * @author: Vgbh
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({VgbhfiveAutoConfigure.class})
public @interface EnableVgbhfiveCient {
}
