package cn.vgbhfive.bookdemo2.conditional;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @time:
 * @author: Vgbh
 */
public class Main {
    public  static void  main (String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConditionConfig.class);

        ListService listService = context.getBean(ListService.class);

        System.out.println(context.getEnvironment().getProperty("os.name") + "------" + listService.showListCmd());
    }
}
