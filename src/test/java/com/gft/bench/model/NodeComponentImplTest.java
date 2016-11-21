package com.gft.bench.model;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class NodeComponentImplTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void shouldReturnEmptyListWhenRootDirectoryIsEmpty() throws IOException {
        //given
        NodeComponentImpl rootNode = new NodeComponentImpl(tempFolder.getRoot().toPath());

        //when
        List<NodeComponent<Path>> listOfChildrenOfRoot = rootNode.getChildren();

        //then
        Assert.assertNotNull(listOfChildrenOfRoot);
    }

    @Test
    public void shouldReturnChildrenOfRootDirectory() throws IOException {

        //given
        File file1 = tempFolder.newFile("file1.txt");
        File folder1 = tempFolder.newFolder("folder1");
        File folder2 = tempFolder.newFolder("folder1","folder2");
        File file2 = tempFolder.newFile("folder1\\file2");

        NodeComponentImpl root = new NodeComponentImpl(tempFolder.getRoot().toPath());

        //when
        List<NodeComponent<Path>> listOfChildrenOfRoot = root.getChildren();
        List<Path> childrenPaths = new ArrayList<>();
        for (NodeComponent<Path> aListOfChildrenOfRoot : listOfChildrenOfRoot) {
            Path a = aListOfChildrenOfRoot.getPayload();
            childrenPaths.add(a);
        }

        //then
        assertThat(childrenPaths, hasSize(2));
        assertThat(childrenPaths, contains(file1.toPath(), folder1.toPath()));

    }

    @Test
    public void shouldReturnTheChildrenOfSubdirectory() throws IOException {
        //given
        File file1 = tempFolder.newFile("file1.txt");
        File folder1 = tempFolder.newFolder("folder1");
        File folder2 = tempFolder.newFolder("folder1","folder2");
        File file2 = tempFolder.newFile("folder1\\file2");
        NodeComponentImpl root = new NodeComponentImpl(folder1.toPath());

        //when
        List<NodeComponent<Path>> listOfChildrenOfRoot = root.getChildren();
        List<Path> childrenPaths = new ArrayList<>();
        for (NodeComponent<Path> aListOfChildrenOfRoot : listOfChildrenOfRoot) {
            Path a = aListOfChildrenOfRoot.getPayload();
            childrenPaths.add(a);
        }

        //then
        assertThat(childrenPaths,hasSize(2));
        assertThat(childrenPaths, containsInAnyOrder(folder2.toPath(), file2.toPath()));
    }


}