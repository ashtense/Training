package org.ashwani.spring.rest.exceptions;

public class S3ClientNotFoundException extends FileUploadException{
    public S3ClientNotFoundException() {
        super("S3 Uploader client not found.");
    }
}
