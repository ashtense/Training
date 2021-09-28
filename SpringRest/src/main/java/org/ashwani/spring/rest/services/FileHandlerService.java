package org.ashwani.spring.rest.services;

import org.ashwani.spring.rest.exceptions.UploaderNotFoundException;
import org.ashwani.spring.rest.file.handlers.FILE_SERVICE;
import org.ashwani.spring.rest.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FileHandlerService {

    @Autowired
    Collection<FileHandler> fileHandlers;

    public void uploadFile(final FileInfo fileToUpload){
        Optional<FileHandler> fileUploaderService = fileHandlers.stream().filter(fileHandler ->
                fileHandler.supports(fileToUpload.getFileService())).findFirst();
        if(!fileUploaderService.isPresent()){
            throw new UploaderNotFoundException();
        }
        fileUploaderService.get().upload(fileToUpload);
    }

    public List<FileInfo> getAllFiles() {
        Optional<FileHandler> fileUploaderService = fileHandlers.stream().filter(fileHandler ->
                fileHandler.supports(FILE_SERVICE.S3)).findFirst();
        if(!fileUploaderService.isPresent()){
            throw new UploaderNotFoundException();
        }
        return fileUploaderService.get().getAllFiles();
    }

    public FileInfo getFileByName(String fileName) {
        Optional<FileHandler> fileHandlerService = fileHandlers.stream().filter(fileHandler ->
                fileHandler.supports(FILE_SERVICE.S3)).findFirst();
        if(!fileHandlerService.isPresent()){
            throw new UploaderNotFoundException();
        }
        return fileHandlerService.get().findFileByName(fileName);
    }
}
