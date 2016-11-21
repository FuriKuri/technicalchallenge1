package com.gft.bench.service;

import org.jetbrains.annotations.NotNull;
import rx.Observable;


public interface IterableToObservable<T> {

    /**
     * create observables from Iterable
     *
     * @return Observable
     * @param list
     */
    @NotNull
    public Observable<T> getObservable(Iterable<T> list);
}