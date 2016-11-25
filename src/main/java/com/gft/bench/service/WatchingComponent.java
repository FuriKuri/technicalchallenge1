package com.gft.bench.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.subjects.PublishSubject;

import java.nio.file.Path;
import java.util.List;

@Component
public class WatchingComponent{


    //private final WatchService watcher;
    //private final Map <WatchKey, Path> keys;

    //get all file and subdirectory
    private final FileService fileService;

    //to notify the websocket
    private PublishSubject <String> publishSubject = PublishSubject.create();

    @Autowired
    public WatchingComponent( FileService fileService ){
        this.fileService = fileService;
    }

    //call the fileService.getObservable
    //this method might invoked by several browser
    //we must lock this method to be sure the invocation is finished properly
    //only one thread will be able to execute the code block on given instance of the class
    public synchronized void startWatching(){
        //create a list of file and folder
        List <Path> list = fileService.getObservable().toList().toBlocking().first();
        //emit items to the subject
        list.stream().map( Path::toString ).forEach( publishSubject::onNext );
    }

    public Observable<String> getPublishSubject(){
        return this.publishSubject;
    }

    //retrieve the subject , this method is called by the websocket


//    private void walkAndRegisterDirectories( final Path startPath ) throws IOException{
//        Files.walkFileTree( startPath, new SimpleFileVisitor <Path>(){
//            @Override
//            public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException{
//                registerDirectory( dir );
//                System.out.println( "Now we are watching this dir: " + dir + " ..." );
//                return FileVisitResult.CONTINUE;
//            }
//        } );
//    }

//    private void registerDirectory( Path path ) throws IOException{
//        Boolean isFolder = (Boolean) Files.getAttribute( path,
//                "basic:isDirectory", NOFOLLOW_LINKS );
//        if( !isFolder ){
//            throw new IllegalArgumentException( "Path: " + path + " is not a folder" );
//        }
//        WatchKey key = path.register( watcher, ENTRY_CREATE,
//                ENTRY_DELETE );
//        keys.put( key, path );
//    }

//    private void startWatching(){
//        for( ; ; ){
//            WatchKey key;
//            try{
//                key = watcher.take();
//            } catch( InterruptedException x ){
//                return;
//            }
//
//            Path dir = keys.get( key );
//            if( dir == null ){
//                System.err.println( "WatchKey not recognized!!" );
//                continue;
//            }
//
//            for( WatchEvent <?> event : key.pollEvents() ){
//                @SuppressWarnings("rawtypes")
//                WatchEvent.Kind kind = event.kind();
//
//                // Context for directory entry event is the file name of entry
//                @SuppressWarnings("unchecked")
//                Path name = ( (WatchEvent <Path>) event ).context();
//                Path child = dir.resolve( name );
//
//                // print out event
//                System.out.format( "%s: %s: %s\n", event.kind().name(), child, name );
//
//                // if directory is created, and watching recursively, then register it and its sub-directories
//                if( kind == ENTRY_CREATE ){
//                    try{
//                        if( Files.isDirectory( child ) ){
//                            walkAndRegisterDirectories( child );
//                        }
//                    } catch( IOException x ){
//                        // do something useful
//                    }
//                }
//            }
//
//            // reset key and remove from set if directory no longer accessible
//            boolean valid = key.reset();
//            if( !valid ){
//                keys.remove( key );
//
//                // all directories are inaccessible
//                if( keys.isEmpty() ){
//                    break;
//                }
//            }
//        }
//    }

//
//    public static void main( String[] args ) throws IOException{
//        Path homeFolder = Paths.get( "/test" );
//        new WatchingComponent( homeFolder ).startWatching();
//    }
}
