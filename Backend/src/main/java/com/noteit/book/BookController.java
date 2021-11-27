package com.noteit.book;


import com.noteit.dto.BookDTO;
import com.noteit.dto.BookDetailsDTO;
import com.noteit.dto.ChapterDTO;
import com.noteit.dto.FileDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/books")
public class BookController {

    @Resource
    private BookService bookService;


    @ExceptionHandler(Exception.class)
    public BookDTO handleException(Exception e) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setError(e.getMessage());
        e.printStackTrace();
        return bookDTO;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getBooks(){

        List<BookDTO> bookDTOs = bookService.getAllBooks();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookDTOs);
    }

    @GetMapping("/{book_id}")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@PathVariable Long book_id){
        try {
            BookDetailsDTO bookDetailsDTO = bookService.getBookDetails(book_id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bookDetailsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/split")
    public ResponseEntity<BookDetailsDTO> getBookDetails(@RequestBody BookDetailsDTO bookDetails) {

        try {
            if (validateSplitRequest(bookDetails)) {
                BookDetailsDTO bookDetailsDTO = bookService.splitBook(bookDetails);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(bookDetailsDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bookDetails);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bookDetails);
        }
    }

    @GetMapping("/{book_id}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long book_id) {

        try {
            FileDTO file = bookService.downloadBook(book_id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\\"+file.getFileName()+"\\")
                    .body(file.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    private boolean validateSplitRequest(BookDetailsDTO bookDetails) throws IOException {

        int totalPages = bookService.getTotalPages(bookDetails);

        for (ChapterDTO chapter : bookDetails.getChapters()) {
            if (chapter.getStartPage() > chapter.getEndPage()) {
                bookDetails.setError("Start Page is greater than End Page for Chapter Number: " + chapter.getChapterNumber());
                return false;
            }

            if (chapter.getStartPage() > totalPages || chapter.getEndPage() > totalPages) {
                bookDetails.setError("Start Page or End Page is greater than total pages in the book for Chapter Number: " + chapter.getChapterNumber());
                return false;
            }
        }

        return true;
    }

}
