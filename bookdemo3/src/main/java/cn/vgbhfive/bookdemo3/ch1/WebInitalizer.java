package cn.vgbhfive.bookdemo3.ch1;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Registration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @time:
 * @author: Vgbh
 */
public class WebInitalizer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

        context.register(MyMvcConfig.class);
        context.setServletContext(servletContext);

        Registration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        ((ServletRegistration.Dynamic) servlet).addMapping("/");
        ((ServletRegistration.Dynamic) servlet).setLoadOnStartup(1);
    }


    /*
        WebApplicationInitalizer是Spring提供用来实现配置Servlet3.0配置的接口，从而实现了替代web.xml的位置，
        实现此接口将会自动被SpringServletContainerInitalizer（用来启动Servlet3.0的容器）获取到。

     */
}
