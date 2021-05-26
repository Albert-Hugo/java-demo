package com.example.streamrabbitmq;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
@EnableScheduling
@EnableBinding(Source.class)
public class UsageDetailSender {

    @Autowired
    private Source source;
    private static int c;


    @Scheduled(fixedDelay = 1000)
    public void sendEvents() {
        UserDetail usageDetail = new UserDetail();
        usageDetail.setName("ido"+c++);
        this.source.output().send(MessageBuilder.withPayload(usageDetail).build());
    }
}