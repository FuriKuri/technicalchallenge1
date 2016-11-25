package com.gft.bench.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TechnicalChallenge1IntegrationTest{

    @LocalServerPort
    private int port;

    private SockJsClient sockJsClient;
    private WebSocketStompClient webSocketStompClient;

    private final WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();

    /**
     * we have a browser
     * she sent request to WebSocketBackEnd
     *
     */
    @Test
    public void shouldAllowTheClientToSubscribe(){

    }

}
