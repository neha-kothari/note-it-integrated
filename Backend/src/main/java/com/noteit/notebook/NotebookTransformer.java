package com.noteit.notebook;

import com.noteit.chapter.Chapter;
import com.noteit.chapter.ChapterRepository;
import com.noteit.chapter.ChapterTransformer;
import com.noteit.dto.ChapterDTO;
import com.noteit.dto.NotebookDTO;
import com.noteit.dto.NotesOutputDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class NotebookTransformer {

    @Resource
    private ChapterRepository chapterRepository;

    @Resource
    private ChapterTransformer chaptertransformer;

    public Notebook toEntity(Notebook notebook, NotebookDTO request) {

        HashSet<Chapter> chapters = new HashSet<>();
        for (Long chapterId : request.getSelected_chapters()) {
            Chapter chapter = chapterRepository.findByChapterId(chapterId);
            if (null != chapter) {
                chapters.add(chapter);
            }
        }
        if (null ==  notebook.getChapters()) {
            notebook.setChapters(new HashSet<>());
        }
        notebook.setChapters(chapters);

        return notebook;
    }

    public NotesOutputDTO toNotesOutputDTO(Notebook notebook) {

        NotesOutputDTO outputDTO = new NotesOutputDTO();
        List<ChapterDTO> chapters = new ArrayList<>();
        for (Chapter nchapter : notebook.getChapters()) {
            chapters.add(chaptertransformer.toDto(nchapter));
        }
        outputDTO.setChapters(chapters);
        if (notebook.getNotebookName() != null) {
            outputDTO.setCustomName(notebook.getNotebookName());
        }
        outputDTO.setCreatedOn(notebook.getCreationDate());
        outputDTO.setNotebookId(notebook.getNotebookId());
        return outputDTO;
    }
}
