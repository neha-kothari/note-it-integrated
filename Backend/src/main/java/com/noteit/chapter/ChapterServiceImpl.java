package com.noteit.chapter;

import com.noteit.book.Book;
import com.noteit.dto.ChapterDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class ChapterServiceImpl implements ChapterService {

    @Resource
    private ChapterTransformer chapterTransformer;

    @Resource
    private ChapterRepository chapterRepository;

    @Override
    @Transactional
    public Chapter addChapter(ChapterDTO chapterDTO, Book book) {

        Chapter chapter = new Chapter();
        chapter = chapterTransformer.toEntity(chapter, chapterDTO);
        chapter.setBook(book);
        chapter = chapterRepository.save(chapter);
        return chapter;
    }
}
