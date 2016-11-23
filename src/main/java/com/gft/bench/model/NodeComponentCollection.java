package com.gft.bench.model;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NodeComponentCollection<T>
        implements Iterable <NodeComponent <T>> {

    private NodeComponent <T> nodeComponent;

    public NodeComponentCollection(@NotNull NodeComponent <T> nodeComponent) {
        this.nodeComponent = nodeComponent;
    }

    @Override
    public Iterator <NodeComponent <T>> iterator() {
        return new NodeComponentIterator();
    }

    private class NodeComponentIterator
            implements Iterator <NodeComponent <T>> {

        List <NodeComponent <T>> nodeComponentChildren = new ArrayList <>();

        NodeComponentIterator() {
            if (nodeComponent != null) {
                nodeComponentChildren.add(nodeComponent);
            }
        }

        @Override
        public boolean hasNext() {
            return !nodeComponentChildren.isEmpty();
        }

        @Override
        public NodeComponent <T> next() {
            NodeComponent <T> component = nodeComponentChildren.get(0);
            nodeComponentChildren.remove(0);

            if (!component.getChildren().isEmpty()) {
                nodeComponentChildren.addAll(component.getChildren());
            }
            return component;
        }
    }
}
