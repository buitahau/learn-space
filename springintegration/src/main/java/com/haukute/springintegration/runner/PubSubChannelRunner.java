package com.haukute.springintegration.runner;

import com.haukute.springintegration.gateway.CustomGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class PubSubChannelRunner {

    @Autowired
    private CustomGateway customGateway;

    private PublishSubscribeChannel publishSubscribeChannel;

    public void setPublishSubscribeChannel(@Qualifier("pubSubChannel") PublishSubscribeChannel publishSubscribeChannel) {
        this.publishSubscribeChannel = publishSubscribeChannel;
    }

    @Bean(name = "pubSubChannel")
    public PublishSubscribeChannel pubSubChannel() {
        Executor executor = new ThreadPoolExecutor(5, 5, 0L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        return new PublishSubscribeChannel(executor);
    }

    @Bean
    public CommandLineRunner pubSub() {
        return (args) -> {
            for (int i = 0; i < 10; i++) {
                Message<String> message = MessageBuilder.withPayload("Msg " + i).build();
                customGateway.printPubsub(message);
            }
        };
    }
}
