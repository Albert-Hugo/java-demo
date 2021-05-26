package com.example.rabbit;

import com.oppein.bsp.ordercenter.dto.domainevent.OrderPaidEvent;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Sender {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MessagingRabbitmqApplication.class, args);
    }


    @Bean
    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }


    @Component
    public class Runner implements CommandLineRunner {

        private final RabbitTemplate rabbitTemplate;
        private final Receiver receiver;

        public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
            this.receiver = receiver;
            this.rabbitTemplate = rabbitTemplate;
        }

        @Override
        public void run(String... args) throws Exception {
            System.out.println("Sending message...");
            OrderPaidEvent orderPaidEvent = new OrderPaidEvent();
            orderPaidEvent.setOrderNo("order no");
            rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.topicExchangeName, "MTDS_MALL_COUPON_ORDER", orderPaidEvent);
            receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        }

    }



}
