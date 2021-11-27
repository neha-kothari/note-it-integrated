package com.noteit.book;

import com.noteit.chapter.Chapter;
import com.noteit.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    @Column(nullable = false)
    private String bookName;
    @Column(unique = true)
    private String isbnNumber;
    @Column(nullable = false)
    private String author;

    private String publisher;
    @Column
    private Integer yearOfRelease;

    @Column
    private String imageLocation;
    @Column
    private String fileLocation;
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User uploadedBy;

    @Column
    private boolean isDeleted;

    @Column
    private LocalDateTime createdOn;

    @Column
    private boolean isSplit;

    @OneToMany(mappedBy = "book")
    private List<Chapter> chapters;
}
