package com.noteit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class NotesOutputDTO {

    private String customName;
    private List<ChapterDTO> chapters;
    private LocalDateTime createdOn;
    private Long notebookId;
}
