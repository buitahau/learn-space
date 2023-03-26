package com.haukute.springintegration.runner;

import com.haukute.springintegration.gateway.CustomGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

@Configuration
public class DirectChannelRunner {

    private static final Logger log = LoggerFactory.getLogger(DirectChannelRunner.class);

    @Autowired
    private CustomGateway customGateway;

    @Bean(name = "directChannel")
    public DirectChannel channel1() {
        return new DirectChannel();
    }

    @Bean
    public CommandLineRunner demoDirectChannel() {
        return (args) -> {

            for (int i = 0; i < 10; i++) {
                Message<String> message = MessageBuilder.withPayload("Msg " + i).build();
                log.info(customGateway.printDirectChannel(message).getPayload());
            }
        };
    }
}
