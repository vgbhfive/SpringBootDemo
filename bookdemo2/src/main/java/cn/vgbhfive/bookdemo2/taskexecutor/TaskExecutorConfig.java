package cn.vgbhfive.bookdemo2.taskexecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @time:
 * @author: Vgbh
 */
@Configuration
@ComponentScan("cn.vgbhfive.bookdemo2.taskexecutor")
@EnableAsync
public class TaskExecutorConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

    /*
        Spring 通过执行器（TaskExecutor）来实现多线程和并发编程。
        使用ThreadPoolTaskExecutor可实现一个基于线程池的TaskExecutor。
        实际任务一般是非阻塞的，异步实现。
        所以因此我们需要通过@Async实现开启对异步的支持，并通过在实际执行的Bean的方法使用@Async注解来声明这是一个异步任务。
     */

}
