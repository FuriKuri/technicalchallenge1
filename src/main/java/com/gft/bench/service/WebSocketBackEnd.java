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
        subscription = watchingComponent.getPublishSubject().subscribe(this::broadcastMessage);
    }

    @PreDestroy
    public void unSubscribe(){
        subscription.unsubscribe();
    }

    private void broadcastMessage( String message ){
        brokerMessagingTemplate.convertAndSend( "/topic", message );
    }

}
