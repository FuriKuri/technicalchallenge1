package com.gft.bench.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class FileNodeComponentCollection extends NodeComponentCollection<Path> {

    public FileNodeComponentCollection(@NotNull NodeComponent<Path> node) {
        super(node);
    }
}
