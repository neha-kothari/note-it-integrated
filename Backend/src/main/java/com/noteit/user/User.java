package com.noteit.user;

import com.noteit.book.Book;
import com.noteit.notebook.Notebook;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "emailAddress"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, unique = true)
    private String emailAddress;
    @Column(nullable = false)
    private int userType;
    @Column(nullable = false)
    private char accountStatus;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "uploadedBy")
    private List<Book> uploadedBooks;

    @OneToMany(mappedBy = "userId")
    private List<Notebook> notebooks;
}
