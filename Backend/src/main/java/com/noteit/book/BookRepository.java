package com.noteit.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByBookId(Long bookId);
    @Query(value = "SELECT * FROM books b WHERE b.isbn_number =:isbnNumber", nativeQuery = true)
    public Book findByIsbnNumber(@Param("isbnNumber") String isbnNumber);

}