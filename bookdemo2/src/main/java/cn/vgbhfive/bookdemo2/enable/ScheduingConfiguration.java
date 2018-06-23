package cn.vgbhfive.bookdemo2.enable;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.TaskManagementConfigUtils;

/**
 * @time:
 * @author: Vgbh
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ScheduingConfiguration {

    @Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor () {
        return new ScheduledAnnotationBeanPostProcessor();
    }

    /*
        直接导入配置类
     */
}
