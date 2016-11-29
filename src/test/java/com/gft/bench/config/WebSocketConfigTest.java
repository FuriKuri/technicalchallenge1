package com.gft.bench.config;

import com.gft.bench.Application;
import com.gft.bench.pojo.DefaultDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)//, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketConfigTest {

//    @LocalServerPort
//    private int port;


    //my first test is to check if the server response to my upgradeHandshake request
    //1. HTTP Request come to server asking for upgrade Communication The server should respond with OK message HTTP 101

    @Test
    public void shouldUpgradeToWebSocket() throws ExecutionException, InterruptedException{

        //when
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());


        new Thread(() -> {
            SpringApplication.run(Application.class);
        }).start();
//
        new Scanner(System.in).nextLine();

        System.out.println("session ready!");

        WebSocketClient transport = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(transport);
        stompClient.setMessageConverter(new StringMessageConverter());

        String url = "ws://localhost:8080/challenge1";
        MyStompHandler stompHandler = new MyStompHandler();


        ListenableFuture<StompSession> future = stompClient.connect(url, stompHandler);
        StompSession stompSession = future.get();

        new Scanner(System.in).nextLine();

        stompSession.subscribe("/topic", stompHandler);

        stompHandler.session.send("/app/list", null);

        System.out.println("Handled response: " + stompHandler.singleResponse.get().getPath());
        //new Scanner(System.in).nextLine();
    }

    private class MyStompHandler implements StompSessionHandler {

        public StompSession session;

        public CompletableFuture<DefaultDirectory> singleResponse = new CompletableFuture<>();

        @Override
        public Type getPayloadType( StompHeaders headers ){
            return DefaultDirectory.class;
        }

        @Override
        public void handleFrame( StompHeaders headers, Object payload ){
            singleResponse.complete((DefaultDirectory) payload);
        }

        @Override
        public void afterConnected( StompSession session, StompHeaders connectedHeaders ){
            this.session = session;
        }

        @Override
        public void handleException( StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception ){
            System.out.println("exception: " + exception.getMessage());
        }

        @Override
        public void handleTransportError( StompSession session, Throwable exception ){
            System.out.println(exception.getMessage());
        }
    }
}