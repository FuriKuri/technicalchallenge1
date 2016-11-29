//package com.gft.bench.integration;
//
//import com.gft.bench.pojo.DefaultDirectory;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.context.embedded.LocalServerPort;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompFrameHandler;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.messaging.simp.stomp.StompSession;
//import org.springframework.messaging.simp.stomp.StompSessionHandler;
//import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.socket.WebSocketHttpHeaders;
//import org.springframework.web.socket.client.WebSocketClient;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//import org.springframework.web.socket.sockjs.client.SockJsClient;
//import org.springframework.web.socket.sockjs.client.Transport;
//import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.contains;
//import static org.mockito.Mockito.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class TechnicalChallenge1IntegrationTest{
//
//    @LocalServerPort
//    private int port;
//
//    private SockJsClient sockJsClient;
//
//    private WebSocketStompClient stompClient;
//
//    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
//
//    @Before
//    public void setup() {
//        List<Transport> transports = new ArrayList<>();
//        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
//        this.sockJsClient = new SockJsClient(transports);
//
//        this.stompClient = new WebSocketStompClient(sockJsClient);
//        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//    }
//
//    /**
//     * we have a browser
//     * she sent request to WebSocketBackEnd
//     *
//     */
//    @Test
//    public void shouldAllowTheClientToSubscribe()throws Exception {
//        String destUri = "ws://echo.websocket.org";
////        if (args.length > 0)
////        {
////            destUri = args[0];
////        }
////
////        WebSocketClient client = new WebSocketClient();
////        SimpleEchoSocket socket = new SimpleEchoSocket();
////        try
////        {
////            client.start();
////
////            URI echoUri = new URI(destUri);
////            ClientUpgradeRequest request = new ClientUpgradeRequest();
////            client.connect(socket,echoUri,request);
////            System.out.printf("Connecting to : %s%n",echoUri);
////
////            // wait for closed socket connection.
////            socket.awaitClose(5,TimeUnit.SECONDS);
////        }
////        catch (Throwable t)
////        {
////            t.printStackTrace();
////        }
////        finally
////        {
////            try
////            {
////                client.stop();
////            }
////            catch (Exception e)
////            {
////                e.printStackTrace();
////            }
////        }
//    }
//
//}
