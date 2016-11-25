package com.gft.bench.model;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * The {@link NodeComponentCollection} offer services related to {@link NodeComponent}
 * The {@link NodeComponentCollection} uses the {@link NodeComponent} to create Iterable
 * collection. the service that interest us is {@link NodeComponentCollection#iterator()} which return Iterator.
 */
public class NodeComponentCollectionTest{

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    private FileSystem fileSystem = Jimfs.newFileSystem( Configuration.unix() );
    private Path rootp = fileSystem.getPath( "/root" );

    @Before
    public void setUp() throws IOException{
        Files.createDirectory( rootp );


    }

    @After
    public void cleanUp() throws IOException{
        Files.walkFileTree( rootp, new SimpleFileVisitor <Path>(){

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
    public void shouldHaveIteratorOfSizeOneWhenRootWithNoChildren(){

        FileNodeComponent root = new FileNodeComponent( rootp );
        NodeComponentCollection <Path> collection = new NodeComponentCollection <>( root );


        assertThat( Arrays.asList( collection ), hasSize( 1 ) );
        assertThat( collection, contains( root ) );
    }

    @Test
    public void shouldRetrieveTheIteratorProperly() throws IOException{
        int numberOfChildren = 2;
        Path node1 = createDirectory( rootp.toString(), "node1" );
        Path node2 = createDirectory( rootp.toString(), "node2" );

        FileNodeComponent root = new FileNodeComponent( rootp );
        NodeComponentCollection <Path> collection = new NodeComponentCollection <>( root );
        List <NodeComponent <Path>> resultList = new ArrayList <>();
        for( NodeComponent <Path> nodeComponent : collection ){
            resultList.add( nodeComponent );
        }

        assertThat( resultList, hasSize( numberOfChildren+1 ) );
    }
//
    /**
     * the purpose of this method is to check if the following tree is in the iterator
     * <p>
     * <pre>             Root               </pre>
     * <pre>           /     \              </pre>
     * <pre>       node1     node2          </pre>
     * <pre>       /  \        \            </pre>
     * <pre>  node3   node4    node5        </pre>
     * <pre>                  /  \          </pre>
     * <pre>               node6  node7     </pre>
     *
     * so the iterator must have all the 7 nodes.
     */
    @Test
    public void shouldRetrieveAllTheChildrenWithTheRoot() throws IOException{
        //Given Start
        Path node1 = createDirectory( rootp.toString(), "node1" );
        Path node2 = createDirectory( rootp.toString(), "node2" );
        Path node3 = createFile( node1.toString(), "node3" );
        Path node4 = createFile( node1.toString(), "node4" );
        Path node5 = createDirectory( node2.toString(), "node5" );
        Path node6 = createFile( node5.toString(), "node6" );
        Path node7 = createFile( node5.toString(), "node7" );
        FileNodeComponent root = new FileNodeComponent( rootp );

        //when
        NodeComponentCollection <Path> collectionOfNode = new NodeComponentCollection <>( root );

        //then
        Assertions.assertThat( collectionOfNode ).hasSize( 8 );
    }


    private Path createFile( String parent, String file ) throws IOException{
        return Files.createFile( fileSystem.getPath( parent, file ) );
    }

    private Path createDirectory( String parent, String subdirectory ) throws IOException{
        return Files.createDirectories( fileSystem.getPath( parent, subdirectory ) );
    }
}
