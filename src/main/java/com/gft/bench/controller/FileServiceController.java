package com.gft.bench.controller;

import com.gft.bench.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;
import java.util.List;

@Controller
public class FileServiceController {

    @Autowired
    private FileService fileService;

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String index() {
//        return "index";
//    }
//
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    @ResponseBody

    /*
    *   what destination i want to process -> "/challenge1"
    *
    *   everything with "/app/challenge1" will forward here
    *
    *   the return of this controller will send back to the broker anyway
    */
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<String> list() {
        return fileService.getObservable().map(Path::toString).toList().toBlocking().first();
    }
}
