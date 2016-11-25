package com.gft.bench.controller;

import com.gft.bench.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rx.Observable;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceControllerTest{

    @InjectMocks
    private FileServiceController fileServiceController;//create an instance of the FileServiceController

    @Mock
    private FileService fileService;//this mock object will be injected into fileServiceController

    private FileSystem fileSystem = FileSystems.getDefault();

    //main entry point of our test
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup( fileServiceController ).build();
    }


    @Test
    public void shouldGetTheListOfFile() throws Exception{
        when( fileService.getObservable() ).thenReturn( Observable.just( fileSystem.getPath( "\\test\\file1" ), fileSystem.getPath( "\\test\\file2" ), fileSystem.getPath( "\\test\\file3" ) ) );

        mockMvc.perform(get("/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", equalTo("\\test\\file1")))
                .andExpect(jsonPath("$[1]", equalTo("\\test\\file2")))
                .andExpect(jsonPath("$[2]", equalTo("\\test\\file3")));
    }

}