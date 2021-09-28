package org.ashwani.spring.rest.services;

import org.ashwani.spring.rest.model.FileInfo;
import org.ashwani.spring.rest.file.handlers.FILE_SERVICE;

import java.util.List;

public interface FileHandler {

    public void upload(FileInfo fileInfo);

    public boolean supports(FILE_SERVICE file_service);

    public List<FileInfo> getAllFiles();

    FileInfo findFileByName(String fileName);
}
