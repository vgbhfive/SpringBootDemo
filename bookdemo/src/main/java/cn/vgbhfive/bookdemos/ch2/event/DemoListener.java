package cn.vgbhfive.bookdemos.ch2.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @time:
 * @author: Vgbh
 */
@Component
public class DemoListener implements ApplicationListener<DemoEvent> {

    @Override
    public void onApplicationEvent(DemoEvent demoEvent) {
        String msg = demoEvent.getMsg();

        System.out.println("-------------------------------" + "我（bean-demoListener）接收到了bean-demoPublisher发布的消息："
                + msg + "-------------------");
    }

    /*
        实现ApplicationListener<>接口，并监听制定的的类
        使用onApplicationEvent方法对消息进行接受处理
     */
}
