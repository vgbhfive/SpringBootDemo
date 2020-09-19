## 简介
这是一个简单的RabbitMQ 的实现案例，主要包含有简单的基础队列、主题队列、延迟队列等。

## 使用
1. 安装RabbitMQ Server 并配置好全局环境变量。

2. 启动RabbitMQ Server 然后启用管理插件，启用管理插件之后就可以通过管理页面访问了（http://localhost:15672）。
```shell script
rabbitmq-plugins enable rabbitmq_managment
```

3. 新建一个新的用户
Admin -> Add a user 

这里强调注意权限的赋值。

4. 在这之后配置应用程序中使用的用户和队列等信息。