package cn.vgbhfive.rabbitmqdemo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @time: 2018/12/07
 * @author: Vgbh
 */
@Configuration
public class RabbitConfig {

    public static final String DEFAULT_BOOK_QUEUE = "dev.book.register.default.queue";

    public static final String MANUAL_BOOK_QUEUE = "dev.bok.register.manual.queue";

    @Bean
    public Queue defaultBookQueue() {
        return new Queue(DEFAULT_BOOK_QUEUE);
    }

    @Bean
    public Queue manualBookQueue() {
        return new Queue(MANUAL_BOOK_QUEUE);
    }

}
