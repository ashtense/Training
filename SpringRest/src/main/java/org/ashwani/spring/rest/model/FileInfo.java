package org.ashwani.spring.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ashwani.spring.rest.file.handlers.FILE_SERVICE;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1559551537509055296L;

    @NotNull
    private String filePath;

    @NotNull
    private String fileName;

    @NotNull
    private FILE_SERVICE fileService;

    @NotNull
    private String destination;
    private long fileSize;

}
