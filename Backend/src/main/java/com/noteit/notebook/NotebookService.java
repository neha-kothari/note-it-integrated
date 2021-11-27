package com.noteit.notebook;

import com.noteit.dto.FileDTO;
import com.noteit.dto.NotebookDTO;
import com.noteit.dto.NotesOutputDTO;
import com.noteit.user.User;

public interface NotebookService {

    void saveNotes(NotebookDTO notebookDTO, Long user_id);

    NotesOutputDTO getNotes(Long user_id);

    NotesOutputDTO deleteChapter(Long user_id, Long chapter_id);

    FileDTO downloadNotes(Long notebook_id) throws Exception;

    FileDTO mergeNotes(NotebookDTO notebookDTO, Long user_id) throws Exception;

    byte[] retrieveNoteBook(String notebookPath) throws Exception;
}
