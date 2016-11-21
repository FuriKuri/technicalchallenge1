package com.gft.bench.model;

import org.jetbrains.annotations.NotNull;

import java.util.*;

class NodeComponentCollection<T>
        implements Iterable<T> {

    private NodeComponent<T> node;

    NodeComponentCollection(@NotNull NodeComponent<T> node) {
        this.node = node;
    }

    @Override
    public Iterator<T> iterator() {
        return new NodeComponentIterator();
    }

    private class NodeComponentIterator
            implements Iterator<T> {

        List<NodeComponent<T>> arrayList = new ArrayList<>();

        NodeComponentIterator() {
            arrayList.add(node);
        }

        @Override
        public boolean hasNext() {
            return !arrayList.isEmpty();
        }

        @Override
        public T next() {
            NodeComponent<T> component = arrayList.get(0);
            arrayList.remove(0);

            if (!component.getChildren().isEmpty()) {
                arrayList.addAll(component.getChildren());
            }
            return component.getPayload();
        }
    }
}
