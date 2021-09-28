package org.ashwani.spring.rest.file.handlers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.ashwani.spring.rest.config.AmazonClient;
import org.ashwani.spring.rest.exceptions.FileUploadException;
import org.ashwani.spring.rest.model.FileInfo;
import org.ashwani.spring.rest.services.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3FileHandler implements FileHandler {

    Logger logger = LoggerFactory.getLogger(S3FileHandler.class);
    private final String BUCKET_NAME = "file-upload-ashwani";
    private final String FILE_SEPARATOR = "\\";
    private final String USER_HOME_DIRECTORY = "user.home";

    @Autowired
    private AmazonClient s3Client;

    @Override
    public List<FileInfo> getAllFiles() {
        AmazonS3 amazonS3 = this.s3Client.getS3Client();
        ObjectListing objectListing = amazonS3.listObjects(BUCKET_NAME);
        List<FileInfo> lstFiles = new ArrayList<>();
        objectListing.getObjectSummaries().forEach(bucketObjects -> {
            lstFiles.add(FileInfo.builder().fileName(bucketObjects.getKey())
                    .fileSize(bucketObjects.getSize())
                    .build());
        });
        return lstFiles;
    }

    @Override
    public FileInfo findFileByName(String fileName) {
        AmazonS3 amazonS3 = this.s3Client.getS3Client();
        GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, fileName);
        S3Object object = amazonS3.getObject(getObjectRequest);
        S3ObjectInputStream objectContent = object.getObjectContent();
        long contentLength = object.getObjectMetadata().getContentLength();
        String property = System.getProperty(USER_HOME_DIRECTORY);
        String filePath = property + FILE_SEPARATOR + object.getKey();
        Path destinationFile = Paths.get(filePath);
        try {
            Files.createDirectories(destinationFile);
            Files.copy(objectContent, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Error while writing file to filesystem.");
            throw new FileUploadException("Error while writing file to filesystem.", e);
        }
        return FileInfo.builder().fileName(object.getKey())
                .filePath(filePath).fileService(FILE_SERVICE.S3).fileSize(contentLength)
                .build();
    }

    @Override
    public void upload(FileInfo fileInfo) {
        Path path = Paths.get(fileInfo.getFilePath());
        if (!Files.exists(path)) {
            logger.error("Target file doesn't exist on fileSystem.");
            throw new FileUploadException("Target file doesn't exist on fileSystem");
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(fileInfo.getFilePath());
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(fileInputStream.available());
            TransferManager transferManager = TransferManagerBuilder.standard()
                    .withS3Client(s3Client.getS3Client())
                    .build();
            PutObjectRequest request = new PutObjectRequest(fileInfo.getDestination(),
                    fileInfo.getFileName(), fileInputStream, meta);
            Upload upload = transferManager.upload(request);
            logger.info("FileUpload started. *****");
            upload.waitForCompletion();
            if (upload.isDone()) {
                logger.info("Upload is completed.");
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Exception occured while uploading file.", e.getMessage());
            throw new FileUploadException("Exception occured during file upload.", e);
        }
    }

    @Override
    public boolean supports(FILE_SERVICE file_service) {
        return FILE_SERVICE.S3.equals(file_service);
    }
}
