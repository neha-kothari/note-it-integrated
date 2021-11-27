package com.noteit.notebook;

import com.noteit.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotebookRepository  extends JpaRepository<Notebook, Long> {

    @Query(value = "SELECT * FROM notebook n WHERE n.user_id = :userId and n.status = 'C'", nativeQuery = true)
    Notebook findNotebookIdForUser(@Param("userId") Long userId);

    Notebook findByNotebookId(Long notebookId);
}
