# 日志收集小工具


> 用于收集log4j 日志消息  
需搭配对应的 log4j appender，就是client
 

 <br>

工作原理
- 客户端使用appender通过socket向服务端发送消息
- 服务端在收到消息一定时间后将消息输出
- 服务器端采用控制台输出所有级别，指定级别输出到文件的方式

使用框架
- log4j
- netty

服务端配置项说明
<br>
- delay 延时，服务端收到消息得知后的等待时间，超出这个时间则输出消息，单位毫秒
- file_path 日志文件存储路径，支持时间匹配；如/home/inkbox/{yyyy}/{dd}
- port 服务器端监听的端口，需要和客户端一致
- level 需要输出到文件的日志级别，如debug，不区分大小写