package com.vgbh.webdemo.config;

import com.vgbh.webdemo.domain.User;
import com.vgbh.webdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import java.util.Collection;

/**
 * 路由器函数
 */
@Configuration
public class RouteFunctionConfiguration {

    /**
     * Servlet
     * 请求接口：ServletRequest 或者 HttpServletRequest
     * 响应接口：ServletResponse 或者 HttpServletResponse
     *
     * Spring 5.0 重新定义了响应接口和请求接口
     * 请求接口：ServerRequest
     * 响应接口：ServerResponse
     *
     * 即可以支持Servlet规范也可以支持Netty （Web Server）
     *
     * Flux 是 0 - N 个对象
     * Mono 是 0 - 1 个对象
     * Reactive 中的Flux是异步处理（非阻塞）
     * 集合对象基本上是在同步处理（阻塞）
     *
     * Flux 或者 Mono 都是 Publisher（发布器）
     */

    @Bean
    @Autowired
    public RouterFunction<ServerResponse> userFindAll (UserRepository userRepository) {
        // 返回所有信息
        return RouterFunctions.route(RequestPredicates.GET("/user"),
                request -> {
                    Collection<User> users = userRepository.findAll();
                    Flux<User> userFlux = Flux.fromIterable(users);
                    return ServerResponse.ok().body(userFlux, User.class);
                });
    }

}
