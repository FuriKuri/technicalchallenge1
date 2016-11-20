package com.gft.bench.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

interface NodeComponent<T> {

    T getPayload();

    @NotNull
    List<NodeComponent<T>> getChildren();
}
