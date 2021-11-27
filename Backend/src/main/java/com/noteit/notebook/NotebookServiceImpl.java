package com.noteit.notebook;

import com.noteit.chapter.Chapter;
import com.noteit.chapter.ChapterRepository;
import com.noteit.dto.FileDTO;
import com.noteit.dto.NotebookDTO;
import com.noteit.dto.NotesOutputDTO;
import com.noteit.service.PDFService;
import com.noteit.user.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Component
public class NotebookServiceImpl implements NotebookService{

    @Resource
    private NotebookRepository notebookRepository;

    @Resource
    private PDFService pdfMergeService;

    @Resource
    private UserRepository userRepository;

    @Resource
    private ChapterRepository chapterRepository;

    @Resource
    private NotebookTransformer notebookTransformer;

    @Override
    @Transactional
    public void saveNotes(NotebookDTO notebookDTO, Long user_id) {

        Notebook notebook = notebookRepository.findNotebookIdForUser(user_id);
        if (null == notebook) {
            notebook = new Notebook();
            notebook.setStatus('C');
            notebook.setUserId(userRepository.findByUserId(user_id));
        }

        notebook = notebookTransformer.toEntity(notebook, notebookDTO);
        notebookRepository.save(notebook);
    }

    @Override
    @Transactional
    public NotesOutputDTO getNotes(Long user_id) {
        Notebook notebook = notebookRepository.findNotebookIdForUser(user_id);
        if (null == notebook) {
            return null;
        }
        return notebookTransformer.toNotesOutputDTO(notebook);
    }

    @Override
    @Transactional
    public NotesOutputDTO deleteChapter(Long user_id, Long chapter_id) {
        Notebook notebook = notebookRepository.findNotebookIdForUser(user_id);
        if (null == notebook) {
            return null;
        }
        if (notebook.getChapters() != null) {
            Chapter chapter = chapterRepository.findByChapterId(chapter_id);
            notebook.getChapters().remove(chapter);
            notebookRepository.save(notebook);
        }

        return notebookTransformer.toNotesOutputDTO(notebook);
    }


    @Override
    public FileDTO mergeNotes(NotebookDTO notebookDTO, Long user_id) throws Exception {
        saveNotes(notebookDTO, user_id);
        Notebook notebook = notebookRepository.findNotebookIdForUser(user_id);
        notebook.setNotebookName(notebookDTO.getCustom_name());
        notebook.setLocation(pdfMergeService.merge(notebook));
        notebook.setStatus('P');
        notebook.setCreationDate(LocalDateTime.now());
        notebookRepository.save(notebook);
        FileDTO file = new FileDTO();
        file.setFileName(notebook.getNotebookName());
        file.setData(new ByteArrayResource(retrieveNoteBook(notebook.getLocation())));
        return file;
    }

    @Override
    public byte[] retrieveNoteBook(String notebookPath) throws Exception {
        if (Strings.isBlank(notebookPath)) {
            return null;
        }
        Path path = Paths.get(notebookPath);
        return Files.readAllBytes(path);
    }

    @Override
    @Transactional(readOnly = true)
    public FileDTO downloadNotes(Long notebook_id) throws Exception {

        Notebook notebook = notebookRepository.findByNotebookId(notebook_id);
        FileDTO file = new FileDTO();
        file.setFileName(notebook.getNotebookName());
        file.setData(new ByteArrayResource(retrieveNoteBook(notebook.getLocation())));
        return file;
    }
}
