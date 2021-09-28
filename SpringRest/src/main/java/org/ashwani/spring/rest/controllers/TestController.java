package org.ashwani.spring.rest.controllers;

import com.amazonaws.util.CollectionUtils;
import org.ashwani.spring.rest.model.FileInfo;
import org.ashwani.spring.rest.services.FileHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/test")
public class TestController {

    Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private FileHandlerService fileHandlerService;

    @GetMapping("/")
    public ResponseEntity<List<FileInfo>> getAllFiles() {
        List<FileInfo> lstFiles = fileHandlerService.getAllFiles();
        if(CollectionUtils.isNullOrEmpty(lstFiles)){
            return new ResponseEntity<List<FileInfo>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<FileInfo>>(lstFiles, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<String> testNewFileUploader(@RequestBody(required = true)
                                                          @Valid FileInfo fileToUpload){
        fileHandlerService.uploadFile(fileToUpload);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<FileInfo> getFileByName(@PathVariable("fileName") String fileName){
        FileInfo fileByName = fileHandlerService.getFileByName(fileName);
        if(Objects.isNull(fileByName)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fileByName, HttpStatus.OK);
    }

}
