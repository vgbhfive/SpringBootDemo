package cn.vgbhfive.bookdemos.ch2.prepost;

/**
 * @time:
 * @author: Vgbh
 */
public class BeanWayService {

    public void init () {
        System.out.println("@bean-init-method" + "------------");
    }

    public BeanWayService() {
        super();
        System.out.println("初始化构造函数--BeanWayService" + "------------");
    }

    public void destory () {
        System.out.println("@bean-destory-method" + "------------");
    }
}
