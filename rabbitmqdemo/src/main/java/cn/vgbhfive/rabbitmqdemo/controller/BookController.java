package cn.vgbhfive.rabbitmqdemo.controller;

import cn.vgbhfive.rabbitmqdemo.config.RabbitConfig;
import cn.vgbhfive.rabbitmqdemo.domain.Book;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @time: 2018//12/07
 * @author: Vgbh
 */
@RestController
@RequestMapping(value = "/book")
public class BookController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public BookController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * this.rabbitTemplate.convertAndSend(RabbitConfig.DEFAULT_BOOK_QUEUE, book); --> BookHandler.linstenerAutoAck
     */
    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public void defaultMessage() {
        Book book = new Book();
        book.setId(1);
        book.setName("lyl");
        book.setAuthor("lyl");
        book.setContext("default queue!");
        this.rabbitTemplate.convertAndSend(RabbitConfig.DEFAULT_BOOK_QUEUE, book);
    }

    /**
     * this.rabbitTemplate.convertAndSend(RabbitConfig.MANUAL_BOOK_QUEUE, book); --> BookHandler.listenerManualAck
     */
    @RequestMapping(value = "/manual", method = RequestMethod.GET)
    public void manualMessage() {
        Book book = new Book();
        book.setId(1);
        book.setName("lyl");
        book.setAuthor("lyl");
        book.setContext("manual queue!");
        this.rabbitTemplate.convertAndSend(RabbitConfig.MANUAL_BOOK_QUEUE, book);
    }

    /**
     * 分列模式
     */
    @RequestMapping(value = "/fanout", method = RequestMethod.GET)
    public void fanoutMessage() {
        Book book = new Book();
        book.setId(1);
        book.setName("lyl");
        book.setAuthor("lyl");
        book.setContext("fanout queue!");
        this.rabbitTemplate.convertAndSend(RabbitConfig.FANOUT_MODE_QUEUE, "", book);
    }

    /**
     * 主题模式发送1
     */
    @RequestMapping(value = "/topic/one", method = RequestMethod.GET)
    public void topicMessageOne() {
        Book book = new Book();
        book.setId(1);
        book.setName("lyl");
        book.setAuthor("lyl");
        book.setContext("Topic one queue!");
        this.rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_ROUTING_KEY_ONE, "queue.aa.bb", book);
    }

    /**
     * 主题模式发送2
     */
    @RequestMapping(value = "/topic/two", method = RequestMethod.GET)
    public void topicMessageTwo() {
        Book book = new Book();
        book.setId(1);
        book.setName("lyl");
        book.setAuthor("lyl");
        book.setContext("Topic two queue!");
        this.rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_ROUTING_KEY_ONE, "ccc.queue", book);
    }

    /**
     * 主题模式发送3
     */
    @RequestMapping(value = "/topic/three", method = RequestMethod.GET)
    public void topicMessageThree() {
        Book book = new Book();
        book.setId(1);
        book.setName("lyl");
        book.setAuthor("lyl");
        book.setContext("Topic three queue!");
        this.rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_ROUTING_KEY_ONE, "3.queue", book);
    }

    /**
     * 延迟队列
     */
    @RequestMapping(value = "/delay", method = RequestMethod.GET)
    public void delayMessage() {
        Book book = new Book();
        book.setId(1);
        book.setName("lyl");
        book.setAuthor("lyl");
        book.setContext("delay 5000ms queue!");
        rabbitTemplate.convertAndSend(RabbitConfig.DELAY_MODE_QUEUE, RabbitConfig.DELAY_QUEUE, book, message -> {
            message.getMessageProperties().setHeader("x-delay", 5000);
            return message;
        });

        book.setContext("delay 2000ms queue!");
        rabbitTemplate.convertAndSend(RabbitConfig.DELAY_MODE_QUEUE, RabbitConfig.DELAY_QUEUE, book, message -> {
            message.getMessageProperties().setHeader("x-delay", 2000);
            return message;
        });

        book.setContext("delay 8000ms queue!");
        rabbitTemplate.convertAndSend(RabbitConfig.DELAY_MODE_QUEUE, RabbitConfig.DELAY_QUEUE, book, message -> {
            message.getMessageProperties().setHeader("x-delay", 8000);
            return message;
        });
    }
}
