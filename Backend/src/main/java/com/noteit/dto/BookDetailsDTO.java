package com.noteit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookDetailsDTO {

    private Long bookId;
    private String bookName;
    private String isbnNumber;
    private String author;
    private String publisher;
    private Integer yearOfRelease;
    private String imageLocation;
    private String uploadedByUser;
    private String error;
    private String description;
    private boolean isSplit;
    private List<ChapterDTO> chapters;
}
