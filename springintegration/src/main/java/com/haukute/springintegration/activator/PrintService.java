package com.haukute.springintegration.activator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class PrintService {

    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    @ServiceActivator(inputChannel = "echoChannel", outputChannel = "ackChannel")
    public Message<String>  print(Message<String> message) {
        log.info("Received : " + message);
        return MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders()).build();
    }

    @ServiceActivator(inputChannel = "reverseChannel")
    public void reversePrint(String message) {
        log.info(new StringBuilder(message).reverse().toString());
    }

    @ServiceActivator(inputChannel = "queueChannel", poller = {
        @Poller(maxMessagesPerPoll = "3", fixedRate = "3000")
    })
    public Message<String> consumeQueue1(Message<String> message) {
        log.info("PrintService.consumeQueue1 -> Received from gateway : " + message.getPayload());
        return MessageBuilder.withPayload("Message '" + message.getPayload() + "' received by PrintService.consumeQueue1")
            .build();
    }

    @ServiceActivator(inputChannel = "queueChannel", poller = {
        @Poller(maxMessagesPerPoll = "2", fixedRate = "2000")
    })
    public Message<String> consumeQueue2(Message<String> message) {
        log.info("PrintService.consumeQueue2 -> Received from gateway : " + message.getPayload());
        return MessageBuilder.withPayload("Message '" + message.getPayload() + "' received by PrintService.consumeQueue2")
            .build();
    }

    @ServiceActivator(inputChannel = "directChannel")
    public Message<String> consumeDirectMessage1(Message<String> message) {
        log.info("PrintService.consumeDirectMessage1 -> Received message from gateway : " + message.getPayload());
        return MessageBuilder.withPayload("Message '" + message.getPayload() + "' received by PrintService.consumeDirectMessage1")
            .build();
    }

    @ServiceActivator(inputChannel = "directChannel")
    public Message<String> consumeDirectMessage2(Message<String> message) {
        log.info("PrintService.consumeDirectMessage2 -> Received message from gateway : " + message.getPayload());
        return MessageBuilder.withPayload("Message '" + message.getPayload() + "' received by PrintService.consumeDirectMessage2")
            .build();
    }

    @ServiceActivator(inputChannel = "directChannel")
    public Message<String> consumeDirectMessage3(Message<String> message) {
        log.info("PrintService.consumeDirectMessage3 -> Received message from gateway : " + message.getPayload());
        throw new RuntimeException("PrintService.consumeDirectMessage3 is down.");
    }

    @ServiceActivator(inputChannel = "pubSubChannel")
    public Message<String> consumePubsubMessage1(Message<String> message) {
        log.info("PrintService.consumePubsubMessage1 -> Received message from gateway : " + message.getPayload());
        return MessageBuilder.withPayload("Message '" + message.getPayload() + "' received by PrintService.consumePubsubMessage1")
            .build();
    }

    @ServiceActivator(inputChannel = "pubSubChannel")
    public Message<String> consumePubsubMessage2(Message<String> message) {
        log.info("PrintService.consumePubsubMessage2 -> Received message from gateway : " + message.getPayload());
        return MessageBuilder.withPayload("Message '" + message.getPayload() + "' received by PrintService.consumePubsubMessage2")
            .build();
    }
}
