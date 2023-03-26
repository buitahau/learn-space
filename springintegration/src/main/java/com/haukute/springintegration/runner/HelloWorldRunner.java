package com.haukute.springintegration.runner;

import com.haukute.springintegration.gateway.CustomGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Configuration
public class HelloWorldRunner {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldRunner.class);

    @Autowired
    private CustomGateway gateway;

    private QueueChannel queueChannel;

    public void setQueueChannel(@Qualifier("queueChannel") QueueChannel queueChannel) {
        this.queueChannel = queueChannel;
    }

    @Bean(name = "queueChannel")
    public QueueChannel channel1(){
        return new QueueChannel(100);
    }

    @Bean
    public CommandLineRunner demo() {
        return (args -> {
            String json = "{\"name\" : \"hau-kute\"}";
            Message<String> message = MessageBuilder
                .withPayload(json)
                .setHeader("content-type", "application/json")
                .setHeader("my-origin", "localhost")
                .build();
            gateway.print(message);

            gateway.reverse("Reverse Me!!!");

            sendToQueueChannel();
        });
    }

    private void sendToQueueChannel() throws ExecutionException, InterruptedException {
        List<Future<Message<String>>> futures = new ArrayList<>();
        for (int i = 0; i< 10; i++) {
            System.out.println("Sending message number " + i);
            Message<String> message = MessageBuilder.withPayload("msg " + i).build();
            futures.add(gateway.printQueue(message));
        }

        log.info("All the messages sent in asynchronous way");

        for(Future<Message<String>> future: futures) {
            Message<String> message = future.get();

            log.info(message.getPayload());
        }
    }
}
