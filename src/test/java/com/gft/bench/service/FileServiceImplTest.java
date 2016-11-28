package com.gft.bench.service;

import com.gft.bench.pojo.DefaultDirectory;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceImplTest{


    private final static String ROOT = "/root";
    private FileServiceImpl fileService;
    private DefaultDirectory rootDirectory = new DefaultDirectory( ROOT );
    private FileSystem       fileSystem    = Jimfs.newFileSystem( Configuration.unix() );
    private Path             root          = fileSystem.getPath( "/root" );

    @Before
    public void setUp() throws IOException{
        fileService = new FileServiceImpl( rootDirectory, fileSystem );
        Files.createDirectory( root );
    }

    @After
    public void cleanUp() throws IOException{
        Files.walkFileTree( root, new SimpleFileVisitor <Path>(){

            @Override
            public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException{
                Files.delete( file );
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory( Path dir, IOException exc ) throws IOException{
                Files.delete( dir );
                return FileVisitResult.CONTINUE;
            }

        } );
    }

    @Test
    public void shouldGetFileList() throws IOException{
        Path directory = createDirectory( "directory" );
        Path file1 = createFile( "file11.txt" );
        Path file2 = createFile( "file12.txt" );

        List <String> files = fileService.getObservable().map( Path::toString ).toList().toBlocking().first();

        assertThat( files, contains( root.toString(), directory.toString(), file1.toString(), file2.toString() ) );

    }

    private Path createFile( String file ) throws IOException{
        return Files.createFile( fileSystem.getPath( root.toString(), file ) );
    }

    private Path createDirectory( String subdirectory ) throws IOException{
        return Files.createDirectories( fileSystem.getPath( root.toString(), subdirectory ) );
    }
}