package com.noteit.chapter;

import com.noteit.book.Book;
import com.noteit.dto.ChapterDTO;

public interface ChapterService {

    Chapter addChapter(ChapterDTO chapterDTO, Book book);
}
