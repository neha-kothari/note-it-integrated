package com.noteit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NotebookDTO {

    private List<Long> selected_chapters;
    private String custom_name;
}
