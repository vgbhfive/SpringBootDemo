package cn.vgbhfive.bookdemos.ch2.prepost;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @time:
 * @author: Vgbh
 */
public class JSR250WayService {

    @PostConstruct
    public void init () {
        System.out.println("@jsr250-init-method" + "------------");
    }

    public JSR250WayService() {
        super();
        System.out.println("初始化构造函数--JSR250WayService" + "------------");
    }

    @PreDestroy
    public void destory () {
        System.out.println("@JSR250-destory-method" + "-----------------");
    }

    /*
        PostConstruct:在构造函数之前运行
        PreDestory:在Bean销毁之前运行
     */
}
