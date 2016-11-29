package com.gft.bench.config;

import com.gft.bench.pojo.DefaultDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.security.Principal;
import java.security.*;
import java.util.Map;
import java.util.Random;

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

    //https://www.infoq.com/presentations/spring-websocket
    //1. define an endpoint : "/challenge1"
    //2. every client needs to connect to the endpoint above

    //  so the client side should be like this
    // "ws://localhost:8080/challenge1"

    //in the client side we should define a callback
    //means what should we do when we receive message
    /*
    *       var ws;
    *       ws = new WebSocket('ws://localhost:8080/challenge1');
    *
    *       //define a callback here: whenever we get the message we want to put it on the list
    *       ws.onmessage = function(event){
    *       $('#challenge1').prepend('<div class="row"><div class="col s12"><div class="card grey-text"><div = class"card-content center"><p>' + event.data+'</p></div></div></div></div>');
    *       });
    *
    *
    *
    *       //send something using send method of websocket
    *       //whatever in the text area is sent to the server
    *       //useful for save file from the client
    *       function sendSomething(){
    *       ws.send();
    *       };
    *
    *       //---------------------------
    *       ws = new SockJS("/challenge1");
    *       stompClient = Stomp.over(ws);
    *       stompClient.connect{(},function(frame){
    *       stompClient.subscribe("/topic/challenge1",function(message){
    *       console.log("Received:"+message);
    *       });
    *       },function(error){
    *       console.log("STOMP protocol error " + error);
    *       });
    *       });
    *
    *   .setHandshakeHandler(new RandomUsernameHandshakeHandler())
    *
    * */


    @Override
    public void registerStompEndpoints( StompEndpointRegistry registry ){
        registry.addEndpoint("/challenge1").setAllowedOrigins("*").withSockJS();
//        RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();
//        registry.addEndpoint("/hello")
//                .withSockJS();
//
//        registry.addEndpoint("/hello")
//                .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
//                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker( MessageBrokerRegistry registry ){
        //application destination: forward to my controllers
        //so any destination which start with /app forward to my controller

        //the client side should be like this:
        /*
        *       ws = new SockJS("/challenge1");
        *       stompClient = Stomp.over(ws);
        *       stompClient.connect{(},function(frame){
        *               stompClient.subscribe("/topic/challenge1",function(message){
        *               console.log("Received:"+message);
        *           });
        *           },function(error){
        *               console.log("STOMP protocol error " + error);
        *               });
        *           });
        *
        */
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    /**
     *
     */
//    class RandomUsernameHandshakeHandler extends DefaultHandshakeHandler {
//        private String[] ADJECTIVES = {"aggressive", "annoyed", "black", "beautiful", "crazy", "elegant", "little", "old-fashioned"};
//        private String[] NOUNS      = {"agent", "american", "anaconda", "caiman", "crab", "flamingo", "gorilla", "kitten"};
//
//        @Override
//        protected Principal determineUser( ServerHttpRequest request, WebSocketHandler wsHandler,
//                                           Map<String,Object> attributes){
//            String username = this.getRandom(ADJECTIVES) + "-" + this.getRandom(NOUNS) + "-" + getRandomInt(ADJECTIVES.length);
//            return new UsernamePasswordAuthenticationToken(username,null);
//        }
//
//        private String getRandom(String [] array){
//            int random = getRandomInt(array.length);
//            return array[random];
//        }
//
//        private int getRandomInt(int bound){
//            return new Random().nextInt(bound);
//        }
//    }


//    //define a handler
//    //a handler will be a class which handle all the messages
//    class MessageHandler extends TextWebSocketHandler {
//
//        private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//
//        //so whenever i receive the message which will be our question
//        //i broadcast it to all connected users
//        @Override
//        public void afterConnectionEstablished( WebSocketSession session ) throws Exception{
//            //store all the sessions
//            sessions.add(session);
//        }
//
//        @Override
//        protected void handleTextMessage( WebSocketSession session, TextMessage message ) throws Exception{
//
//            for( WebSocketSession s : sessions ){
//                s.sendMessage(message);
//            }
//        }
//    }


}