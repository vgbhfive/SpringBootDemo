# SpringBootDemo

## testSpring:
Spring MVC 相关的内容进行综合的练习Demo。

### 内容
主要内容包括用户的CURD、Exception 的默认处理、AOP面向切面编程、连接数据库。

### 依赖
```XML
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

	</dependencies>
```

<hr>

## webdemo:
Spring Web Flux Demo

### 内容
使用了Spring 5 中的反应式编程，实现查询全部用户信息。

### 依赖
```XML
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>io.projectreactor</groupId>
			<artifactId>reactor-test</artifactId>
			<scope>test</scope>
		</dependency>

	</dependencies>
```

### Configuration 类
```Java
	@Bean
    @Autowired
    public RouterFunction<ServerResponse> userFindAll (UserRepository userRepository) {
        // 返回所有信息
        return RouterFunctions.route(RequestPredicates.GET("/user"),
                request -> {
                    Collection<User> users = userRepository.findAll();
                    Flux<User> userFlux = Flux.fromIterable(users);
                    return ServerResponse.ok().body(userFlux, User.class);
                });
    }
```

<hr>

## webdemo2:
将Spring项目进行多模块化拆分

### 思想
采用模块化的思想，将业务流程部分进行拆分。

### 依赖
```XML
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>io.projectreactor</groupId>
			<artifactId>reactor-test</artifactId>
			<scope>test</scope>
		</dependency>

	</dependencies>
```

<hr>

## websocket/NettyDemo

### 内容
使用Netty框架建立WebSocket服务端、客户端使用WebSocket 连接服务端与之通信、同时测试往返数据。
建立客户端和服务端，记录客户端和服务端在从建立连接开始到断开连接之间的交互。

### 依赖
netty-all-5.0.0.Alpha1.jar

<hr>

## swaggerdemo

### 内容
练习使用swagger建立API文档，主要内容为用户信息的CURD操作。

### 依赖
```XML
	<dependencies>
        <!--start-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!--swagger-->
        <dependency>
            <groupId>com.spring4all</groupId>
            <artifactId>swagger-spring-boot-starter</artifactId>
            <version>1.7.1.RELEASE</version>
        </dependency>


        <!--logger-->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.9.1</version>
        </dependency>
    </dependencies>

```

<hr>

## RabbitMQ
消息队列

### 内容
使用RabbitMQ组件来学习对应用法。

### 依赖
```XML
	<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--Test-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!--RabbitMQ-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <!--Json-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.45</version>
        </dependency>
    </dependencies>
```

### RabbitMQConfig 类
```Java
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
```
 Controller 类：
```Java
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
```
 Handler 类：
```Java
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
```

<hr>

## mybatisDemo

### 内容
SSM(Spring MVC、 Spring Boot、 Mybatis) ，使用SSM实现功能主要为学生的CRUD操作。

### 依赖
```XML
	<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
            <version>2.0.3.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>2.0.3.RELEASE</version>
        </dependency>

        <!--mybatis-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.5</version>
        </dependency>

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>1.3.1</version>
        </dependency>

        <!--数据库 mysql 驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.46</version>
        </dependency>

        <!--实现slf4j接口并整合-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.9.1</version>
        </dependency>

        <!--junit 测试-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

    </dependencies>
```

<hr>

## actuatorDemo

### 内容
服务控制与管理。
主要用来学习Actuator 组件的使用情况、以及添加自定义节点信息。

### 依赖
```XML
	<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!--Actuator-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>
```

### Configuration 类
```Java
import cn.vgbhfive.actuatordemo.endpoint.MyEndpoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @time: 2018/12/23
 * @author: Vgbh
 */
@Configuration
public class MyEndpointConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnEnabledEndpoint
    public MyEndpoint myEndpoint() {
        return new MyEndpoint();
    }

}
```

<hr>

## bookdemo/bookdemo2/bookdemo3/bookdemo3sr

### 内容
学会使用@Scope注解、Entity类字段注解、JSR250、Profile。
@Schedule、定时任务启动器。
使用Spring MVC构造服务。
自定义拦截器。

<hr>

## RedisDemo/redisdemo2

### 内容
RedisDemo 用于实现一个简单版本的缓存服务器，具备最基础的功能。
redisdemo2 实现的Book 的CURD ，并加入了缓存的内容，使用的是Spring Cache 的接口。

<hr>
## poidemo
poidemo 主要学习如何导出报表。

<hr>
如果你还想看其他的内容，可以去看我的[博客](https://vgbhfive.github.io/)。

