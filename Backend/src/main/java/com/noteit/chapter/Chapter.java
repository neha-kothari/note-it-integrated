package com.noteit.chapter;

import com.noteit.book.Book;
import com.noteit.notebook.Notebook;
import com.noteit.user.User;
import lombok.*;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chapterId;

    @ManyToOne
    @JoinColumn(name="bookId")
    private Book book;

    @Column(nullable = false)
    private Integer chapterNumber;

    @Column(nullable = false)
    private String chapterName;

    @Column(nullable = false)
    private Integer startPage;

    @Column(nullable = false)
    private Integer endPage;

    private String description;

    @ManyToMany(mappedBy = "chapters")
    private Set<Notebook> notebookSet;


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
