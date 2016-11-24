package com.gft.bench.pojo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanCreator{

    private String path;

    @Bean
    public IncomingMessage incomingMessage(){
        return new IncomingMessage( path );
    }
}
