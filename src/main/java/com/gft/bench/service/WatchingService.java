package com.gft.bench.service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class WatchingService{


    private final WatchService watcher;
    private final Map <WatchKey, Path> keys;

    private WatchingService( Path path ) throws IOException{
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap <>();

        walkAndRegisterDirectories( path );
    }

    private void walkAndRegisterDirectories( final Path startPath ) throws IOException{
        Files.walkFileTree( startPath, new SimpleFileVisitor <Path>(){
            @Override
            public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException{
                registerDirectory( dir );
                System.out.println( "Now we are watching this dir: " + dir + " ..." );
                return FileVisitResult.CONTINUE;
            }
        } );
    }

    private void registerDirectory( Path path ) throws IOException{
        Boolean isFolder = (Boolean) Files.getAttribute( path,
                "basic:isDirectory", NOFOLLOW_LINKS );
        if( !isFolder ){
            throw new IllegalArgumentException( "Path: " + path + " is not a folder" );
        }
        WatchKey key = path.register( watcher, ENTRY_CREATE,
                ENTRY_DELETE );
        keys.put( key, path );
    }

    private void sendEvents(){
        for( ; ; ){
            WatchKey key;
            try{
                key = watcher.take();
            } catch( InterruptedException x ){
                return;
            }

            Path dir = keys.get( key );
            if( dir == null ){
                System.err.println( "WatchKey not recognized!!" );
                continue;
            }

            for( WatchEvent <?> event : key.pollEvents() ){
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();

                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ( (WatchEvent <Path>) event ).context();
                Path child = dir.resolve( name );

                // print out event
                System.out.format( "%s: %s: %s\n", event.kind().name(), child, name );

                // if directory is created, and watching recursively, then register it and its sub-directories
                if( kind == ENTRY_CREATE ){
                    try{
                        if( Files.isDirectory( child ) ){
                            walkAndRegisterDirectories( child );
                        }
                    } catch( IOException x ){
                        // do something useful
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if( !valid ){
                keys.remove( key );

                // all directories are inaccessible
                if( keys.isEmpty() ){
                    break;
                }
            }
        }
    }


    public static void main( String[] args ) throws IOException{
        Path homeFolder = Paths.get( "/test" );
        new WatchingService( homeFolder ).sendEvents();
    }
}
