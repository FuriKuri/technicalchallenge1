package com.gft.bench.service;

import com.gft.bench.pojo.IncomingMessage;
import org.jetbrains.annotations.NotNull;
import rx.Observable;
import rx.Subscriber;

public class IterableToObservableImpl<T> implements IterableToObservable<T> {


    @NotNull
    @Override
    public Observable<T> getObservable(Iterable<T> list) {
        if (list == null) {
            throw new NullPointerException("iterable must not be null");
        }
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                for (T aList : list) {
                    subscriber.onNext(aList);
                }
                subscriber.onCompleted();
            }
        });
    }
}
