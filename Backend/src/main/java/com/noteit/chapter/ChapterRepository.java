package com.noteit.chapter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    Chapter findByChapterId(Long chapterId);
}
