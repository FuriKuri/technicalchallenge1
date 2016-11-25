package com.gft.bench.model;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

public class FileNodeComponentTest{

    private FileSystem fileSystem = Jimfs.newFileSystem( Configuration.unix() );
    private Path root = fileSystem.getPath( "/root" );

    @Before
    public void setUp() throws IOException{
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
    public void shouldReturnEmptyListWhenRootDirectoryIsEmpty() throws IOException{
        //given
        FileNodeComponent rootNode = new FileNodeComponent( root );

        //when
        List <NodeComponent <Path>> listOfChildrenOfRoot = rootNode.getChildren();

        //then
        Assert.assertNotNull( listOfChildrenOfRoot );
    }


    @Test
    public void shouldReturnOnlyChildrenOfRootDirectory() throws IOException{

        //given
        Path directory = createDirectory( root.toString(), "directory" );
        Path file1 = createFile( root.toString(), "file1.txt" );
        Path file2 = createFile( root.toString(), "file2.txt" );
        FileNodeComponent rootDir = new FileNodeComponent( root );

        //when
        List <NodeComponent <Path>> listOfChildrenOfRoot = rootDir.getChildren();
        List <Path> childrenPaths = new ArrayList <>();
        for( NodeComponent <Path> aListOfChildrenOfRoot : listOfChildrenOfRoot ){
            Path a = aListOfChildrenOfRoot.getPayload();
            childrenPaths.add( a );
        }

        //then
        assertThat( childrenPaths, hasSize( 3 ) );
        assertThat( childrenPaths, contains( directory, file1, file2 ) );

    }


    @Test
    public void shouldReturnTheChildrenOfSubdirectory() throws IOException{
        //given
        Path directory = createDirectory( root.toString(), "directory" );
        Path directory2 = createDirectory( directory.toString(), "directory2" );
        Path directory3 = createDirectory( directory.toString(), "directory3" );
        Path file3 = createFile( directory.toString(), "file3" );
        Path file4 = createFile( directory.toString(), "file4" );

        FileNodeComponent subDirectory = new FileNodeComponent( directory );

        //when
        List <NodeComponent <Path>> listOfChildrenOfRoot = subDirectory.getChildren();
        List <Path> childrenPaths = new ArrayList <>();
        for( NodeComponent <Path> aListOfChildrenOfRoot : listOfChildrenOfRoot ){
            Path a = aListOfChildrenOfRoot.getPayload();
            childrenPaths.add( a );
        }

        //then
        assertThat( childrenPaths, hasSize( 4 ) );
        assertThat( childrenPaths, contains( directory2, directory3, file3, file4 ) );
    }

    private Path createFile( String parent, String file ) throws IOException{
        return Files.createFile( fileSystem.getPath( parent, file ) );
    }

    private Path createDirectory( String parent, String subdirectory ) throws IOException{
        return Files.createDirectories( fileSystem.getPath( parent, subdirectory ) );
    }
}