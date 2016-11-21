package com.gft.bench.service;

import com.gft.bench.model.NodeComponent;
import com.gft.bench.model.NodeComponentCollection;
import com.gft.bench.model.NodeComponentImpl;
import com.gft.bench.model.NodeComponentImplCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FileServiceImpl implements FileService {

    private FileSystem fileSystem;
    private String path;


    public FileServiceImpl(FileSystem fileSystem, String path) {
        this.fileSystem = fileSystem;
        this.path = path;
    }

    @Override
    public Observable<Path> getObservable() {


        return Observable.create(subscriber -> {
            List<NodeComponent<Path>> list = findAllFileAndFolder(fileSystem.getPath(path));

            for (NodeComponent<Path> node : list) {
                subscriber.onNext(node.getPayload());
            }
            subscriber.onCompleted();
        });

    }

    private List<NodeComponent<Path>> findAllFileAndFolder(Path path) {
        List<NodeComponent<Path>> all = new ArrayList<>();
        if (path == null) {
            return Collections.emptyList();
        }
        if (Files.isDirectory(path)) {
            NodeComponentImpl nodeRoot = new NodeComponentImpl(path);
            List<NodeComponent<Path>> list = nodeRoot.getChildren();
            all.addAll(list);
            list.stream().filter(nc -> Files.isDirectory(nc.getPayload())).forEach(nc -> {
                findAllFileAndFolder(nc.getPayload());
            });
        }
        all.add(new NodeComponentImpl(path));
        return all;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
