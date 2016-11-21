package com.gft.bench.service;

import org.jetbrains.annotations.NotNull;
import rx.Observable;

public class IterableToObservableImpl<T> implements IterableToObservable<T> {
    @NotNull
    @Override
    public Observable<T> getObservable(Iterable<T> list) {
        if (list == null) {
            throw new NullPointerException("iterable must not be null");
        }
        return Observable.create(subscriber -> {
            for (T aList : list) {
                subscriber.onNext(aList);
            }
            subscriber.onCompleted();
        });
    }
}
