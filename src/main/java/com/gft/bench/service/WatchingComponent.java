package com.gft.bench.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.nio.file.Path;

@Component
public class WatchingComponent {

    private FileService fileService;
    @Autowired
    public WatchingComponent( FileService fileService ){
        this.fileService = fileService;
    }

    private PublishSubject<String> publishSubject = PublishSubject.create();

    private int currentCount;
    @Scheduled(fixedRate = 1000)
    public synchronized void emitData(){
        int countOfElements = fileService.getObservable().count().toBlocking().first();
        if(currentCount != countOfElements){
            currentCount = countOfElements;
            fileService.getObservable().map(Path::toString).forEach(publishSubject::onNext);
        }
    }
    public Observable<String> getPublishSubject(){
        return this.publishSubject;
    }
}
