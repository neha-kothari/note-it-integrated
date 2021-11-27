package com.noteit.book;

import com.noteit.chapter.Chapter;
import com.noteit.chapter.ChapterTransformer;
import com.noteit.dto.BookDTO;
import com.noteit.dto.BookDetailsDTO;
import com.noteit.dto.ChapterDTO;
import com.noteit.user.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class BookTransformer {

    @Resource
    private ChapterTransformer chapterTransformer;

    @Resource
    private BookRepository bookRepository;

    @Resource
    private UserRepository userRepository;

    public List<BookDTO> toBookDTOs(List<Book> books) {
        if (Objects.isNull(books)) {
            return new ArrayList<>();
        }

        List<BookDTO> bookDTOs = new ArrayList<>();
        books.forEach(book -> {
            if (Objects.nonNull(book) && !book.isDeleted()) {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setBookId(book.getBookId());
                bookDTO.setBookName(book.getBookName());
                bookDTO.setIsbnNumber(book.getIsbnNumber());
                bookDTO.setAuthor(book.getAuthor());
                bookDTO.setPublisher(book.getPublisher());
                bookDTO.setYearOfRelease(book.getYearOfRelease());
                bookDTO.setImageLocation(book.getImageLocation());
                bookDTO.setUploadedByUser(userRepository.findByUserId(book.getUploadedBy().getUserId()).getName());
                bookDTO.setUploadedOn(book.getCreatedOn());
                bookDTOs.add(bookDTO);
            } else {
                bookDTOs.add(null);
            }
        });

        return bookDTOs;
    }

    public Book toEntity(Book book, BookDetailsDTO request) {
        book.setBookName(request.getBookName());
        book.setAuthor(request.getAuthor());
        book.setIsbnNumber(request.getIsbnNumber());
        book.setDescription(request.getDescription());
        book.setSplit(request.isSplit());
        if (null != request.getImageLocation()) {

        }
        return bookRepository.saveAndFlush(book);
    }

    public BookDetailsDTO toBookDetailsDTO(Book book) throws Exception {

        if (null == book || book.isDeleted()) {
            throw new Exception("Invalid Book Id");
        }
        BookDetailsDTO bookDetailsDTO = new BookDetailsDTO();
        bookDetailsDTO.setBookId(book.getBookId());
        bookDetailsDTO.setBookName(book.getBookName());
        bookDetailsDTO.setIsbnNumber(book.getIsbnNumber());
        bookDetailsDTO.setAuthor(book.getAuthor());
        bookDetailsDTO.setPublisher(book.getPublisher());
        bookDetailsDTO.setYearOfRelease(book.getYearOfRelease());
        bookDetailsDTO.setImageLocation(book.getImageLocation());
        bookDetailsDTO.setUploadedByUser(userRepository.findByUserId(book.getUploadedBy().getUserId()).getName());
        bookDetailsDTO.setDescription(book.getDescription());
        bookDetailsDTO.setSplit(book.isSplit());
        if (book.isSplit()) {
            List<ChapterDTO> chapters = new ArrayList<>();
            for (Chapter chapter : book.getChapters()) {
                chapters.add(chapterTransformer.toDto(chapter));
            }
            bookDetailsDTO.setChapters(chapters);
        }
        return bookDetailsDTO;
    }
}
