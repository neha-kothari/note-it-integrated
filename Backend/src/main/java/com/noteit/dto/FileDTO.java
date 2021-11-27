package com.noteit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@Data
@NoArgsConstructor
public class FileDTO {

    private String fileName;
    private ByteArrayResource data;
}
