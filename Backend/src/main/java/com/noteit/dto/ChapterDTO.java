package com.noteit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChapterDTO {

    private Long chapterId;
    private int chapterNumber;
    private String chapterName;
    private int startPage;
    private int endPage;
    private String description;
    private String bookName;
}
