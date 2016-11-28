package com.gft.bench.config;

import com.gft.bench.pojo.DefaultDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Value("${dirName}")
    public String dirName;

    @Bean
    public DefaultDirectory defaultDirectory(){
        return new DefaultDirectory(dirName);
    }

    @Bean
    public FileSystem fileSystem(){
        return FileSystems.getDefault();
    }

    @Override
    public void registerStompEndpoints( StompEndpointRegistry registry ){
        registry.addEndpoint("/challenge1").withSockJS();
    }

    @Override
    public void configureMessageBroker( MessageBrokerRegistry registry ){
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }


}