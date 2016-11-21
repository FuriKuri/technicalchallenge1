package com.gft.bench.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * For the purpose of testing we need to mock the {@link NodeComponent} interface.
 * So we implement the interface to just working as we expected.
 * The service that interest us is {@link NodeComponent#getPayload()} and
 * {@link NodeComponent#getChildren()}
 */
class NodeComponentStringImpl implements NodeComponent<String> {


    /**
     * The payload associated with this node
     */
    private String payLoad;

    /**
     * The children associated with this node
     */
    private List<NodeComponent<String>> children;


    /**
     * set level parent payload and children of this node
     */
    NodeComponentStringImpl(String payLoad, List<NodeComponent<String>> children) {

        this.payLoad = payLoad;
        this.children = children;
    }


    /**
     * Return the payLoad associated with this node.
     */
    @Override
    public String getPayload() {
        return payLoad;
    }

    /**
     * Return the children associated with this node.
     */
    @NotNull
    @Override
    public List<NodeComponent<String>> getChildren() {
        return children;
    }

    /**
     * Replace the children subtree.
     */
    void setChildren(List<NodeComponent<String>> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "MockNodeComponent{" +
                "payLoad='" + payLoad + '\'' +
                ", children=" + children +
                '}';
    }
}
