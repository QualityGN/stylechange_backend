package com.example.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class CoreApplication {

    public static void main(String[] args)
    {
        System.out.println("run");
        SpringApplication.run(CoreApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate rest() {

        return new RestTemplate();
    }

}
