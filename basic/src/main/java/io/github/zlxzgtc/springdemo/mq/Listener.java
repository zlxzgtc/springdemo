package io.github.zlxzgtc.springdemo.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    @RabbitListener(queues = "myqueue", id = "myqueue-listener")
    public void processMessage(String message) throws InterruptedException {
        System.out.println("Received: " + message);
        // 模拟耗时任务
        Thread.sleep(10000);
    }
}
