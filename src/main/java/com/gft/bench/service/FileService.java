package com.gft.bench.service;

import rx.Observable;

import java.nio.file.Path;

public interface FileService {

    Observable<Path> getObservable();
}
