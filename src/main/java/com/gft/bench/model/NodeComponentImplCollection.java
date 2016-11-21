package com.gft.bench.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class NodeComponentImplCollection extends NodeComponentCollection<Path> {
    public NodeComponentImplCollection(@NotNull NodeComponent<Path> node) {
        super(node);
    }
}
