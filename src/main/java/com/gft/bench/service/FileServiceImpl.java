package com.gft.bench.service;

import com.gft.bench.model.FileNodeComponent;
import com.gft.bench.model.FileNodeComponentCollection;
import com.gft.bench.model.NodeComponent;
import com.gft.bench.pojo.DefaultDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * this class is called from WatchingComponent to create an Observable
 */
@Service
class FileServiceImpl implements FileService {


    private DefaultDirectory defaultDirectory;
    private FileSystem       fileSystem;

    @Autowired
    FileServiceImpl( DefaultDirectory defaultDirectory, FileSystem fileSystem) {
        this.defaultDirectory = defaultDirectory;
        this.fileSystem = fileSystem;
    }

    @Override
    public Observable <Path> getObservable() {

        return Observable.create(subscriber -> {
            Path path = fileSystem.getPath(defaultDirectory.getPath());
            FileNodeComponentCollection files = new FileNodeComponentCollection(new FileNodeComponent(path));
            for (NodeComponent <Path> file : files) {
                subscriber.onNext(file.getPayload());
            }
            subscriber.onCompleted();
        });

    }
}
