package org.ashwani.spring.rest.exceptions;

public class UploaderNotFoundException extends FileUploadException {

    public UploaderNotFoundException() {
        super("Target File uploading service not found.");
    }
}
