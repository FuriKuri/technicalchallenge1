package com.gft.bench.integration;

import com.gft.bench.Application;
import com.gft.bench.pojo.DefaultDirectory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class WebSocketServerIntegrationTest {


    public ConfigurableApplicationContext run = null;
    @LocalServerPort
    private String port;

    @Test
    public void shouldDoHandshake() throws InterruptedException{

        ThreadForTest threadForTest = new ThreadForTest();

        //GIVEN
        String url = "ws://localhost:" + port + "/challenge1";
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();//A WebSocketClient based on standard Java WebSocket API
        List<Transport> transports = new ArrayList<>(2);//Transport: A client-side implementation for a SockJS transport
        transports.add(new RestTemplateXhrTransport());
        transports.add(new WebSocketTransport(standardWebSocketClient));//A SockJS Transport that uses a WebSocketClient
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);//A STOMP over WebSocket client that connects using an implementation of WebSocketClient including SockJsClient
        stompClient.setMessageConverter(new StringMessageConverter());
        MyStompHandler webSocketHandler = new MyStompHandler();//A handler for WebSocket messages and lifecycle events.

        //WHEN
        ListenableFuture<StompSession> webSocketSessionListenableFuture = stompClient.connect(url, webSocketHandler);
        StompSession session = null;
        try{
            //error
            session = webSocketSessionListenableFuture.get();
            session.subscribe("/topic", webSocketHandler);
            webSocketHandler.session.send("/list", null);

            System.out.println("Handled response: " + webSocketHandler.singleResponse.get().getPath());

            //THEN
            Assert.assertTrue(session.isConnected());
        } catch( InterruptedException | ExecutionException e ){
            e.printStackTrace();
        } finally {
            assert session != null;
            session.disconnect();
            //stompClient.
            // stop the thread
            if( run.isActive() ){
                run.close();
            }

        }


    }

    class ThreadForTest implements Runnable {
        Thread t;

        ThreadForTest(){
            // Create a new, second thread
            t = new Thread(this, "Test Thread");
            System.out.println("Test thread: " + t);
            t.start(); // Start the thread
        }

        // This is the entry point for the second thread.
        public void run(){

            run = SpringApplication.run(Application.class);

            System.out.println("Exiting 'Test Thread' thread.");
        }

    }

    private class MyStompHandler implements StompSessionHandler {

        StompSession session;

        CompletableFuture<DefaultDirectory> singleResponse = new CompletableFuture<>();

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