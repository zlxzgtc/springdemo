package io.github.zlxzgtc.springdemo.controller;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
public class MQController {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;
    private final RabbitListenerEndpointRegistry registry;

    public MQController(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin, RabbitListenerEndpointRegistry registry) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
        this.registry = registry;
    }

    @GetMapping("/send")
    public String send() {
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend("myqueue", "Hello, RabbitMQ! " + i);
        }
        return "Message sent!";
    }

    @GetMapping("/receive")
    public String receive() {
        String message = (String) rabbitTemplate.receiveAndConvert("myqueue");
        return message != null ? "Received message: " + message : "No message!";
    }

    @GetMapping("/stop")
    public String stop() {
        registry.getListenerContainer("myqueue-listener").stop(() ->
                System.out.println("Listener stopped! and do something else..."));
        return "Listener stopped!";
    }

    @GetMapping("/start")
    public String start() {
        registry.getListenerContainer("myqueue-listener").start();
        return "Listener start!";
    }

    @GetMapping("/queue_status")
    public String getQueueStatus() {
        Properties queueInformation = rabbitAdmin.getQueueProperties("myqueue");
        if (queueInformation != null) {
            return "Queue Status: " + queueInformation;
        } else {
            return "Queue not found";
        }
    }
}
