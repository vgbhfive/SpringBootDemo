# VertxDemo
Vert.x Demo

## vertx-maven-starter
wiki Demo Use Vert.x 

## vertxDemo2.0
重构vertx-maven-starter ，将代码重构为可重用的顶点。
![/images/verticles-refactoring.png](/images/verticles-refactoring.png)

生成的顶点之间不会相互直接引用，它们仅通过会约定的事件总线中的目标名称以及消息格式来调用对应的服务。

事件总线上发送的消息将以JSON 编码。

## vertxDemo3.0
重构设计和使用Vert.x服务。

Vert.x 服务的主要优点是它定义了一个接口，用于执行顶点公开的操作。