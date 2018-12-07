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
 * @time: 2018/12/07
 * @author: Vgbh
 */
@Component
public class BookHandler {

    private static final Logger logger = LoggerFactory.getLogger(BookHandler.class);

    @RabbitListener(queues = {RabbitConfig.DEFAULT_BOOK_QUEUE})
    public void listenerAutoAck(Book book, Message message, Channel channel) {
        // TODO 手动ACK,消息会被监听消费，但是在消息在队列中依然存在，若是未配置acknowledge-mode 默认是会在消费完毕后自动ACK掉.
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            logger.info("[listenerAutoAck监听的消息] - [{}]", book.toString());
            // TODO 通知MQ消息已被成功接收，可以ACKl
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                // TODO 消息处理失败，重新压入队列
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
            // TODO 如果报错了，那么我们进入容错处理，比如将当前消息进入其他队列
        }
    }

}
