package cn.vgbhfive.bookdemo3sr.config;

import cn.vgbhfive.bookdemo3sr.interceptor.DemoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @time:
 * @author: Vgbh
 */
@EnableWebMvc
@Configuration
@ComponentScan("cn.vgbhfive.bookdemo3sr")
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public InternalResourceViewResolver viewResolver () {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("/WEB-INF/classes/views");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);

        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourcehandler：指明文件放置的目录。
        //addResourceLocations：指明对外暴露的路径
        registry.addResourceHandler("assets/**").addResourceLocations("classpath:assets/");
    }

    /*
        Spring MVC的定制化配置都需要继承WebMvcConfigurerAdapter类，并在此方法使用@EnableWebMvc，来开启对Spring Mvc的配置支持，
        这样就可以重写这个类，完成我们的常用配置。
     */

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("/index");
    }

    /*
        简化页面转向配置
     */

    @Bean
    public DemoInterceptor demoInterceptor () {
        return new DemoInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(demoInterceptor());
    }

    /*
        配置拦截器的Bean
        重写addInterceptors方法，注册拦截器。
     */

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    /*
        在Spring MVC中，路径参数如果带有"."的话，"."后面的值将会被忽略。
     */
}
