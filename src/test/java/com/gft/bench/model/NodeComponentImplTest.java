package com.gft.bench.model;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class NodeComponentImplTest {

    private Path rootDirectory = FileSystems.getDefault().getPath("D:\\IdeaProjects\\TechnicalChallenge1\\DirectoryForTest");
    private Path file1 = FileSystems.getDefault().getPath("D:\\IdeaProjects\\TechnicalChallenge1\\DirectoryForTest\\file1.txt");
    private Path dir1 = FileSystems.getDefault().getPath("D:\\IdeaProjects\\TechnicalChallenge1\\DirectoryForTest\\dir1");
    private Path file2 = FileSystems.getDefault().getPath("D:\\IdeaProjects\\TechnicalChallenge1\\DirectoryForTest\\dir1\\file2.txt");
    private Path dir2 = FileSystems.getDefault().getPath("D:\\IdeaProjects\\TechnicalChallenge1\\DirectoryForTest\\dir1\\dir2");


    @Before
    public void init() {
        try {
            if (!Files.exists(rootDirectory))
                Files.createDirectory(rootDirectory);
            if (!Files.exists(file1))
                Files.createFile(file1);
            if (!Files.exists(dir1))
                Files.createDirectory(dir1);
            if (!Files.exists(file2))
                Files.createFile(file2);
            if (!Files.exists(dir2))
                Files.createDirectory(dir2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void shouldReturnEmptyListWhenRootDirectoryIsEmpty() throws IOException {
        //given
        String path = "D:\\root";
        Path root = FileSystems.getDefault().getPath(path);
        if (!Files.exists(root)) {
            Files.createDirectory(root);
        }
        NodeComponentImpl rootNode = new NodeComponentImpl(root);

        //when
        List<NodeComponent<Path>> listOfChildrenOfRoot = rootNode.getChildren();

        //then
        Assert.assertNotNull(listOfChildrenOfRoot);
    }

    @Test
    public void shouldReturnChildrenOfRootDirectory() {

        //given
        NodeComponentImpl root = new NodeComponentImpl(rootDirectory);

        //when
        List<NodeComponent<Path>> listOfChildrenOfRoot = root.getChildren();
        List<Path> childrenPaths = new ArrayList<>();
        for (NodeComponent<Path> aListOfChildrenOfRoot : listOfChildrenOfRoot) {
            Path a = aListOfChildrenOfRoot.getPayload();
            childrenPaths.add(a);
        }

        //then
        assertThat(childrenPaths, hasSize(2));
        assertThat(childrenPaths, contains(dir1, file1));

    }

    @Test
    public void shouldReturnTheChildrenOfSubdirectory() {
        //given
        NodeComponentImpl root = new NodeComponentImpl(dir1);

        //when
        List<NodeComponent<Path>> listOfChildrenOfRoot = root.getChildren();
        List<Path> childrenPaths = new ArrayList<>();
        for (NodeComponent<Path> aListOfChildrenOfRoot : listOfChildrenOfRoot) {
            Path a = aListOfChildrenOfRoot.getPayload();
            childrenPaths.add(a);
        }

        //then
        assertThat(childrenPaths,hasSize(2));
        assertThat(childrenPaths, contains(dir2, file2));
    }


}