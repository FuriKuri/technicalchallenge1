package com.gft.bench.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rx.Subscription;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class WebSocketBackEnd{

    //by use of this we can send messages to the "brokerChannel"
    //then it is sent to all connected clients
    //simple message broker handles subscription requests from clients,
    // stores them in memory, and broadcasts messages to connected clients
    // with matching destinations.
    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;

    @Autowired
    private WatchingComponent watchingComponent;

    //receive notification from subject(observable)
    private Subscription subscription;

    /**
     * after injecting 'SimpMessagingTemplate' and 'WatchingComponent'
     */
    @PostConstruct
    public void subscribe(){
        subscription = watchingComponent.getPublishSubject().subscribe( ( path ) -> {
            broadcastMessage( path );
        } );
    }

    @PreDestroy
    public void unSubscribe(){
        subscription.unsubscribe();
    }

    @Scheduled(fixedRate = 2000)
    private void broadcastMessage( String path ){
        brokerMessagingTemplate.convertAndSend( "/myDestination", path );
    }

    //1. let spring inject the WatchingComponent
    // we could use his subject and subscribe to it to receive items
    //
    // 2.We need Subscription : Provides a mechanism for receiving push-based
    // notifications from Observables, and permits manual unsubscribing from these Observables
    // (browser disconnect -> unsubscribe)


    /*
    EXAMPLE of a client subscribing to receive stock quites which server may emit
    periodically via scheduled task sending messages through a
    'SimpMessagingTemplate' to the broker

    --SUBSCRIBE
    --id:sub-1
    --destination: /topic/price.stock.*
    --^@

    */


    /*
    destinations to be path-like strings where "/topic/.." implies publish-subscribe (one-to-many)
     and "/queue/" implies point-to-point (one-to-one) message exchanges
     */
    /*
    It is important to know that a server cannot send unsolicited messages.
    All messages from a server must be in response to a specific client subscription,
    and the "subscription-id" header of the server message
    must match the "id" header of the client subscription.
     */

    /*
        Message — a message with headers and a payload.
        MessageHandler — a contract for handling a message.
        MessageChannel — a contract for sending a message enabling
         loose coupling between senders and receivers.
        SubscribableChannel — extends MessageChannel and sends messages
         to registered MessageHandler subscribers.
        ExecutorSubscribableChannel — a concrete implementation of SubscribableChannel
         that can deliver messages asynchronously via a thread pool.
     */

    /*
        The above setup that includes 3 message channels:

        "clientInboundChannel" for messages from WebSocket clients.
        "clientOutboundChannel" for messages to WebSocket clients.
        "brokerChannel" for messages to the broker from within the application.
     */

    /*
    When a message-handling annotated method has a return type,
    its return value is sent as the payload of a Spring Message
    to the "brokerChannel".
    The broker in turn broadcasts the message to clients.
    Sending a message to a destination can also be done from anywhere
     in the application with the help of a messaging template.
     For example, an HTTP POST handling method can broadcast
     a message to connected clients,
     or a service component may periodically broadcast stock quotes.
     */


    /*

    --WebSocket clients connect to the WebSocket endpoint at "/portfolio".
    --Subscriptions to "/topic/greeting" pass through the "clientInboundChannel"
    and are forwarded to the broker.
    -- Greetings sent to "/app/greeting" pass through the "clientInboundChannel"
    and are forwarded to the GreetingController. The controller adds the current time,
    and the return value is passed through the "brokerChannel" as a message
    to "/topic/greeting" (destination is selected based on a convention but can be overridden
    via @SendTo).
    --The broker in turn broadcasts messages to subscribers, and they pass through the
    "clientOutboundChannel".

    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/portfolio");
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.setApplicationDestinationPrefixes("/app");
            registry.enableSimpleBroker("/topic");
        }

    }

    @Controller
    public class GreetingController {
        //It can be used for mapping methods to message destinations
        //and can also be combined with the type-level @MessageMapping
        //for expressing shared mappings across all annotated methods within a controller.
        @MessageMapping("/greeting") {
        public String handle(String greeting) {
            return "[" + getTimestamp() + ": " + greeting;
        }

    }
     */
    //IMPORTANT
    // send messages to connected clients from any part of the application?
    // Any application component can send messages to the "brokerChannel".
    // The easiest way to do that is to have a SimpMessagingTemplate injected,
    // and use it to send messages.


    /*
        Several ApplicationContext events (listed below) are
        published and can be received by implementing
        Spring’s ApplicationListener interface.

        BrokerAvailabilityEvent — indicates when the broker becomes available/unavailable.
            While the "simple" broker becomes available immediately on startup and remains
            so while the application is running, the STOMP "broker relay" may lose its
            connection to the full featured broker, for example if the broker is restarted.
            The broker relay has reconnect logic and will re-establish the "system"
            connection to the broker when it comes back, hence this event is
            published whenever the state changes from connected to disconnected
            and vice versa. Components using the SimpMessagingTemplate should
            subscribe to this event and avoid sending messages at times when
            the broker is not available. In any case they should be prepared
            to handle MessageDeliveryException when sending a message.
        SessionConnectEvent — published when a new STOMP CONNECT is received indicating
            the start of a new client session. The event contains the message
            representing the connect including the session id, user information (if any),
            and any custom headers the client may have sent.
            This is useful for tracking client sessions.
            Components subscribed to this event can wrap the contained message
            using SimpMessageHeaderAccessor or StompMessageHeaderAccessor.
        SessionConnectedEvent — published shortly after a SessionConnectEvent when
            the broker has sent a STOMP CONNECTED frame in response to the CONNECT.
            At this point the STOMP session can be considered fully established.
        SessionSubscribeEvent — published when a new STOMP SUBSCRIBE is received.
        SessionUnsubscribeEvent — published when a new STOMP UNSUBSCRIBE is received.
        SessionDisconnectEvent — published when a STOMP session ends. The DISCONNECT
            may have been sent from the client, or it may also be automatically
            generated when the WebSocket session is closed. In some cases this
            event may be published more than once per session. Components
            should be idempotent with regard to multiple disconnect events.
     */

    /*
    The example below shows how to create a SockJS client and connect to a SockJS endpoint:

        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.doHandshake(new MyWebSocketHandler(), "ws://example.com:8080/sockjs");

        also read STOMP CLIENT 26.4.13 websocket.html
     */


}
