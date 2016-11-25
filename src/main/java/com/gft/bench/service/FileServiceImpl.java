package com.gft.bench.service;

import com.gft.bench.model.FileNodeComponent;
import com.gft.bench.model.FileNodeComponentCollection;
import com.gft.bench.model.NodeComponent;
import com.gft.bench.pojo.IncomingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * this class is called from WatchingComponent to create an Observable
 */
@Service
class FileServiceImpl implements FileService {


    private IncomingMessage incomingMessage;
    private FileSystem fileSystem;


    @Autowired
    FileServiceImpl(IncomingMessage incomingMessage, FileSystem fileSystem) {
        this.incomingMessage = incomingMessage;
        this.fileSystem = fileSystem;
    }

    @Override
    public Observable <Path> getObservable() {

        return Observable.create(subscriber -> {
            Path path = fileSystem.getPath(incomingMessage.getPath());
            FileNodeComponentCollection files = new FileNodeComponentCollection(new FileNodeComponent(path));
            for (NodeComponent <Path> file : files) {
                subscriber.onNext(file.getPayload());
            }
            subscriber.onCompleted();
        });

    }
//    private List <Path> listAllFilesInADirectory(FileNodeComponentCollection files) {
//        List <Path> all = new ArrayList <>();
//        if (path == null) {
//            return Collections.emptyList();
//        }
//        if (Files.isDirectory(path)) {
//            try (DirectoryStream <Path> stream = Files.newDirectoryStream(path)) {
//                for (Path entry : stream) {
//                    all.add(entry);
//                }
//            } catch (DirectoryIteratorException ex) {
//                ex.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            FileNodeComponent nodeRoot = new FileNodeComponent(path);
//            List<NodeComponent<Path>> list = nodeRoot.getChildren();
//            all.addAll(list);
//            list.stream ( ).filter ( nc -> {
//                return Files.isDirectory ( nc.getPayload ( ) );
//            } ).forEach ( nc -> {
//                listAllFilesInADirectory ( nc.getPayload ( ) );
//            } );
//        }
//        //all.add(new FileNodeComponent(path));
//        return all;
//    }

//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
}
