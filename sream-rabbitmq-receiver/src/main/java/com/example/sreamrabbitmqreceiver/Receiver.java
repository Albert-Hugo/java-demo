package com.example.sreamrabbitmqreceiver;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class Receiver {


    @StreamListener(Processor.INPUT)
    public void process(UserDetail userDetail){
        System.out.println(userDetail.getName());

    }

}
