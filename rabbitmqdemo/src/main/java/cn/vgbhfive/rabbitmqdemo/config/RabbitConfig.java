package cn.vgbhfive.rabbitmqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @time: 2018/12/07
 * @author: Vgbh
 */
@Configuration
public class RabbitConfig {

    /**
     * 默认队列
     */
    public static final String DEFAULT_BOOK_QUEUE = "dev.book.register.default.queue";

    /**
     * 持久化处理队列
     */
    public static final String MANUAL_BOOK_QUEUE = "dev.bok.register.manual.queue";

    /**
     * 分列模式队列
     */
    public static final String FANOUT_MODE_QUEUE = "fanout.mode";

    /**
     * 主题模式队列
     */
    public static final String TOPIC_MODE_QUEUE = "topic.mode";

    /**
     * 主题绑定模式队列
     */
    public static final String TOPIC_ROUTING_KEY_ONE = "queue.#";

    /**
     * 主题绑定模式队列2
     */
    public static final String TOPIC_ROUTING_KEY_TWO = "*.queue";

    /**
     * 主题绑定模式队列3
     */
    public static final String TOPIC_ROUTING_KEY_THREE = "3.queue";

    /**
     * 延迟队列
     */
    public static final String DELAY_QUEUE = "delay.queue";

    /**
     * 延迟模式队列
     */
    public static final String DELAY_MODE_QUEUE = "delay.mode";

    /**
     * 一般队列
     * @return
     */
    @Bean
    @Qualifier("defaultQueue")
    public Queue defaultBookQueue() {
        return new Queue(DEFAULT_BOOK_QUEUE);
    }

    /**
     * 持久化队列
     * @return
     */
    @Bean
    public Queue manualBookQueue() {
        return new Queue(MANUAL_BOOK_QUEUE, true);
    }

    /**
     * 分列模式队列
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_MODE_QUEUE);
    }

    /**
     * 分列模式绑定队列1
     *
     * @param directOneQueue 绑定队列1
     * @param fanoutExchange 分列模式交换器
     */
    @Bean
    public Binding fanoutBinding1(@Qualifier("defaultQueue") Queue directOneQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(directOneQueue).to(fanoutExchange);
    }

    /**
     * 主题模式队列
     * <li>路由格式必须以 . 分隔，比如 user.email 或者 user.aaa.email</li>
     * <li>通配符 * ，代表一个占位符，或者说一个单词，比如路由为 user.*，那么 user.email 可以匹配，但是 user.aaa.email 就匹配不了</li>
     * <li>通配符 # ，代表一个或多个占位符，或者说一个或多个单词，比如路由为 user.#，那么 user.email 可以匹配，user.aaa.email 也可以匹配</li>
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_MODE_QUEUE);
    }

    /**
     * 主题模式绑定分列模式
     *
     * @param fanoutExchange 分列模式交换器
     * @param topicExchange  主题模式交换器
     */
    @Bean
    public Binding topicBinding1(FanoutExchange fanoutExchange, TopicExchange topicExchange) {
        return BindingBuilder.bind(fanoutExchange).to(topicExchange).with(TOPIC_ROUTING_KEY_ONE);
    }

    /**
     * 主题模式绑定队列2
     *
     * @param queueTwo      队列2
     * @param topicExchange 主题模式交换器
     */
    @Bean
    public Binding topicBinding2(@Qualifier("defaultQueue") Queue queueTwo, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueTwo).to(topicExchange).with(TOPIC_ROUTING_KEY_TWO);
    }

    /**
     * 主题模式绑定队列3
     *
     * @param queueThree    队列3
     * @param topicExchange 主题模式交换器
     */
    @Bean
    public Binding topicBinding3(@Qualifier("defaultQueue") Queue queueThree, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueThree).to(topicExchange).with(TOPIC_ROUTING_KEY_THREE);
    }

    /**
     * 延迟队列
     */
    @Bean
    public Queue delayQueue() {
        return new Queue(DELAY_QUEUE, true);
    }

    /**
     * 延迟队列交换器, x-delayed-type 和 x-delayed-message 固定
     */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_MODE_QUEUE, "x-delayed-message", true, false, args);
    }

    /**
     * 延迟队列绑定自定义交换器
     *
     * @param delayQueue    队列
     * @param delayExchange 延迟交换器
     */
    @Bean
    public Binding delayBinding(Queue delayQueue, CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_QUEUE).noargs();
    }

}
