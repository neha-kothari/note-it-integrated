package com.noteit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class BookDTO {

    private Long bookId;
    private String bookName;
    private String isbnNumber;
    private String author;
    private String publisher;
    private Integer yearOfRelease;
    private String imageLocation;
    private String uploadedByUser;
    private String error;
    private LocalDateTime uploadedOn;
}
