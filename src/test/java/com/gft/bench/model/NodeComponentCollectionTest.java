package com.gft.bench.model;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@link NodeComponentCollection} offer services related to {@link NodeComponent}
 * The {@link NodeComponentCollection} uses the {@link NodeComponent} to create Iterable
 * collection. the service that interest us is {@link NodeComponentCollection#iterator()} which return Iterator.
 */
public class NodeComponentCollectionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none ( );

    private NodeComponentStringImpl root;
    private NodeComponentStringImpl node1;
    private NodeComponentStringImpl node2;
    private NodeComponentStringImpl node3;
    private NodeComponentStringImpl node4;
    private NodeComponentStringImpl node5;
    private NodeComponentStringImpl node6;
    private NodeComponentStringImpl node7;

    @Before
    public void setUp() {
        root = new NodeComponentStringImpl ( "root", Lists.emptyList ( ) );
        node1 = new NodeComponentStringImpl ( "node1", Lists.emptyList ( ) );
        node2 = new NodeComponentStringImpl ( "node2", Lists.emptyList ( ) );
        node3 = new NodeComponentStringImpl ( "node3", Lists.emptyList ( ) );
        node4 = new NodeComponentStringImpl ( "node4", Lists.emptyList ( ) );
        node5 = new NodeComponentStringImpl ( "node5", Lists.emptyList ( ) );
        node6 = new NodeComponentStringImpl ( "node6", Lists.emptyList ( ) );
        node7 = new NodeComponentStringImpl ( "node7", Lists.emptyList ( ) );
    }

    @After
    public void cleanUp() {
        root = null;
        node1 = null;
        node2 = null;
        node3 = null;
        node4 = null;
        node5 = null;
        node6 = null;
        node7 = null;
    }

    @Test
    public void shouldNotAllowNullValue() {

        expectedException.expect ( IllegalArgumentException.class );
        expectedException.expectMessage ( "Argument for @NotNull parameter 'node' of com/gft/bench/model/NodeComponentCollection.<init> must not be null" );
        NodeComponentCollection <String> nodeComponentCollection = new NodeComponentCollection <>(null);
        nodeComponentCollection.iterator();

    }

    @Test
    public void shouldHaveIteratorOfSizeOneWhenRootWithNoChildren() {
        NodeComponentCollection<String> collection = new NodeComponentCollection<> ( root );

        Assertions.assertThat ( collection ).hasSize ( 1 );
    }

    @Test
    public void shouldRetrieveTheIteratorProperly() {
        int numberOfChildren = 2;
        createChildren ( root, node1, node2 );

        NodeComponentCollection<String> collection = new NodeComponentCollection<> ( root );

        Assertions.assertThat ( collection ).size ( ).isEqualTo ( numberOfChildren + 1 );//considering the root object
    }

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
    public void shouldHaveAllTheChildrenWithTheRoot() {
        //Given Start
        createChildren ( root, node1, node2 );
        createChildren ( node1, node3, node4 );
        createChildren ( node2, node5 );
        createChildren ( node5, node6, node7 );

        //when
        NodeComponentCollection<String> collectionOfNode = new NodeComponentCollection<> ( root );

        for (String s : collectionOfNode) {
            System.out.println ( s );

        }
        //then
        Assertions.assertThat ( collectionOfNode ).hasSize ( 8 );
        Assertions.assertThat ( collectionOfNode ).contains ( root.getPayload ( ),
                node1.getPayload ( ), node2.getPayload ( ), node3.getPayload ( ),
                node4.getPayload ( ), node5.getPayload ( ), node6.getPayload ( ),
                node7.getPayload ( ) );
    }


    /**
     * first parameter is the parent node
     * the second parameter is the array of children which is dynamic
     */
    private void createChildren(NodeComponentStringImpl root, NodeComponentStringImpl... node) {
        List<NodeComponentStringImpl> list = Arrays.asList ( node );
        List<NodeComponent<String>> children_of_root = new ArrayList<> ( );
        children_of_root.addAll ( list );
        root.setChildren ( children_of_root );
    }
}
