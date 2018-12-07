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
     * this.rabbitTemplate.convertAndSend(RabbitConfig.MANUAL_BOOK_QUEUE, book); --> BookHandler.listenerManualAck
     */
    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public void defaultMessage() {
        Book book = new Book();
        book.setId(1);
        book.setName("lyl");
        book.setAuthor("lyl");
        book.setContext("sssssssssss");
        this.rabbitTemplate.convertAndSend(RabbitConfig.DEFAULT_BOOK_QUEUE, book);
        this.rabbitTemplate.convertAndSend(RabbitConfig.MANUAL_BOOK_QUEUE, book);
    }
}
