package cn.vgbhfive.bookdemo2.taskexecutor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @time:
 * @author: Vgbh
 */
@Service
public class AsyncTaskSevice {

    @Async
    public void executorAsyncTask (Integer i) {
        System.out.println("执行异步计划：" + i);
    }

    @Async
    public void executorAsyncTaskPlus (Integer i) {
        System.out.println("执行异步计划+1：" + (i+1));
    }
}
