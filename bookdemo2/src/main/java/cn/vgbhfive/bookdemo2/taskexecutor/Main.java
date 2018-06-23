package cn.vgbhfive.bookdemo2.taskexecutor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @time:
 * @author: Vgbh
 */
public class Main {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskExecutorConfig.class);

        AsyncTaskSevice asyncTaskSevice = context.getBean(AsyncTaskSevice.class);
        for (int i = 0; i < 10; i++) {
            asyncTaskSevice.executorAsyncTask(i);
            asyncTaskSevice.executorAsyncTaskPlus(i);
        }

        context.close();
    }
}
