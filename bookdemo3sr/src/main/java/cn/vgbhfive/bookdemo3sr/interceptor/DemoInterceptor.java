package cn.vgbhfive.bookdemo3sr.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @time:
 * @author: Vgbh
 */
public class DemoInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        request.removeAttribute("startTime");
        long endTime = System.currentTimeMillis();

        System.out.println("本次消耗时间：" + (endTime-startTime) );
        request.setAttribute("handingTime", endTime-startTime);
    }

    /*
        继承HandlerInterceptorAdapter类来实现自定义拦截器
        重写preHandle，在请求发生前执行。
        重写postHandler，在请求完成之后。
     */





}
