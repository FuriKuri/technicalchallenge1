package com.gft.bench.service;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

public class FileServiceImplTest {
    @Test
    public void shouldGetFileList() throws IOException {
        FileSystem fileSystem = FileSystems.getDefault();
        String path = "/test";
        FileServiceImpl service = new FileServiceImpl(fileSystem,path);


        List<String> files = service.getObservable().map(Path::toString).toList().toBlocking().first();

        assertThat(files, hasSize(3));
    }
}