package com.gft.bench.service;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.Subscription;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WatchingComponentTest {

    @InjectMocks
    private WatchingComponent watchingComponent;
    @Mock
    private FileService       fileService;

    private FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());

    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Subscription subscription;

    @After
    public void tearDown(){
        reset(fileService);
        if( subscription != null && !subscription.isUnsubscribed() )
            subscription.unsubscribe();
    }

    @Test
    public void shouldDetectChanges() throws InterruptedException{
        List<String> expectedFiles = new ArrayList<>();//Arrays.asList(fileSystem.getPath("file1.txt"), fileSystem.getPath("file2.txt"));
        CountDownLatch countDownLatch = new CountDownLatch(2);

        when(fileService.getObservable()).thenReturn(Observable.just(fileSystem.getPath("file1.txt")));
        watchingComponent.emitData();


        //provide new Observable
        when(fileService.getObservable()).thenReturn(Observable.just(fileSystem.getPath("file1.txt"), fileSystem.getPath("file2.txt")));
        //wait until items emitted use new thread
        subscription = watchingComponent.getPublishSubject()
                .subscribe(item -> {
                    expectedFiles.add(item);//System.out.println(item);
                    countDownLatch.countDown();
                });
        executorService.execute(() -> watchingComponent.emitData());
        countDownLatch.await();

        assertThat(expectedFiles, hasSize(2));
        assertThat(expectedFiles, contains("file1.txt", "file2.txt"));
    }

    @Test
    public void shouldNotUpdateIfNoChangesDetected() throws InterruptedException{
        int NUMBER_OF_INVOCATION_PER_EMITDATA_WHEN_CHANGE = 2;
        int NUMBER_OF_INVOCATION_PER_EMITDATA_WHEN_NO_CHANGE = 1;

        when(fileService.getObservable()).thenReturn(Observable.just(fileSystem.getPath("file1.txt")));
        watchingComponent.emitData();
        watchingComponent.emitData();

        verify(fileService, times(3)).getObservable();
    }

}