package cn.vgbhfive.rabbitmqdemo.handler;

import cn.vgbhfive.rabbitmqdemo.config.RabbitConfig;
import cn.vgbhfive.rabbitmqdemo.domain.Book;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 默认情况下 spring-boot-data-amqp是自动ACK机制，就意味着MQ会在消息消费完毕后自动帮我们去ACK。
 * 这样依赖就存在这样一个问题：如果报错了，消息不会丢失，会无限循环消费，很容易把磁盘空间耗完，
 * 虽然可以配置消费的次数但这种做法也不太好。目前比较推荐的就是我们手动ACK然后将消费错误的消息转移到其他的消息队列中，做补偿处理。
 *
 * @time: 2018/12/07
 * @author: Vgbh
 */
@Component
public class BookHandler {

    private static final Logger logger = LoggerFactory.getLogger(BookHandler.class);

    @RabbitListener(queues = {RabbitConfig.DEFAULT_BOOK_QUEUE})
    public void listenerAutoAck(Book book, Message message, Channel channel) {
        // 手动ACK,消息会被监听消费，但是在消息在队列中依然存在，若是未配置acknowledge-mode 默认是会在消费完毕后自动ACK掉。
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            logger.info("[listenerAutoAck监听的消息] - [{}]", book.toString());
            // 通知MQ消息已被成功接收，可以ACK了
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                // 消息处理失败，重新压入队列
                channel.basicRecover();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RabbitListener(queues = {RabbitConfig.MANUAL_BOOK_QUEUE})
    public void listenerManualAck(Book book, Message message, Channel channel) {
        logger.info("[listenerManualAck 监听消息] - [{}]", book.toString());
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            // 如果报错了，那么我们进入容错处理，比如将当前消息进入其他队列
        }
    }

}
