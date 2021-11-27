package com.noteit.notebook;

import com.noteit.chapter.Chapter;
import com.noteit.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notebook")
public class Notebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notebookId;

    @Column(nullable = false) // C-In Cart and P-Processed & Merged
    private Character status;

    @Column
    private String notebookName;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;

    @Column
    private String location;

    @Column
    private LocalDateTime creationDate;

    @ManyToMany
    @JoinTable(
            name = "notebook_chapters",
            joinColumns = @JoinColumn(name = "notebookId"),
            inverseJoinColumns = @JoinColumn(name = "chapterId"))
    private Set<Chapter> chapters;


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
