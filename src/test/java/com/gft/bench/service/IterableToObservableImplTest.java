package com.gft.bench.service;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import rx.Observable;
import rx.subjects.ReplaySubject;

import java.util.Arrays;

public class IterableToObservableImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test the iterable to work properly.
     */

//    @Test
//    public void shouldCreateObservableCorrectlyFromAnIterable() {
//        // given
//        Iterable<Integer> it = Arrays.asList(1, 2, 3);
//        Observable<Integer> source = new IterableToObservableImpl<Integer>().getObservable(it);
//
//        // when
//        ReplaySubject<Integer> outputCache = ReplaySubject.create();
//        source.toBlocking().subscribe(outputCache);
//
//        // then
//        Assertions.assertThat(outputCache.toBlocking().getIterator()).contains(1, 2, 3);
//    }
//
//
//    @Test
//    public void shouldThrowNullPointerExceptionWhenIterableIsNull() {
//        exception.expect(NullPointerException.class);
//        exception.expectMessage("iterable must not be null");
//        new IterableToObservableImpl<Integer>().getObservable(null);
//    }
}