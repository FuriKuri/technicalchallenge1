package com.gft.bench.model;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

class NodeComponentImpl implements
        NodeComponent<Path> {

    private Path payLoad;


    NodeComponentImpl(Path payLoad) {
        this.payLoad = payLoad;
    }

    @Override
    public Path getPayload() {
        return payLoad;
    }

    @NotNull
    @Override
    public List<NodeComponent<Path>> getChildren() {
        return createTreeOfFile().stream().map(NodeComponentImpl::new).collect(toList());

    }

    /**
     * this method create tree file of current folder
     */
    private List<Path> createTreeOfFile() {
        List<Path> files = Collections.emptyList();
        try {
            if (payLoad == null) {
                return files;
            }
            if (!Files.isDirectory(payLoad)) {
                files.add(payLoad);
            }
            if (Files.isDirectory(payLoad)) {
                files = Files.list(payLoad).collect(toList()); //elements of the parent directory
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }


}
