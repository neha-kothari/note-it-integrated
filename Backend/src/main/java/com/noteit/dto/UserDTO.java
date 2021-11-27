package com.noteit.dto;

import com.noteit.book.Book;
import com.noteit.notebook.Notebook;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String name;
    private String emailAddress;
    private String phoneNumber;
    private List<BookDTO> uploadedBooks;
    private List<NotesOutputDTO> notebooks;

}
