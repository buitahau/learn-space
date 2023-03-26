package com.haukute.springintegration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

import java.util.concurrent.Future;

@MessagingGateway(name = "customGateway")
public interface CustomGateway {

    @Gateway(requestChannel = "echoChannel", replyTimeout = 2, requestTimeout = 200)
    void print(Message<String> message);

    @Gateway(requestChannel = "reverseChannel", replyTimeout = 2, requestTimeout = 200)
    void reverse(String message);

    @Gateway(requestChannel = "queueChannel")
    Future<Message<String>> printQueue(Message<String> message);

    @Gateway(requestChannel = "directChannel")
    Message<String> printDirectChannel(Message<String> message);

    @Gateway(requestChannel = "pubSubChannel")
    public void printPubsub(Message<String> message);
}
