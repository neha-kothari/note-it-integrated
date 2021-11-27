package com.noteit.user;

import com.noteit.book.Book;
import com.noteit.book.BookTransformer;
import com.noteit.dto.*;
import com.noteit.notebook.Notebook;
import com.noteit.notebook.NotebookTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRepository userRepository;

    @Resource
    private BookTransformer bookTransformer;

    @Resource
    private NotebookTransformer notebookTransformer;

    @Override
    public User addUser(UserRegistrationDTO userDTO) {
        User user = new User();
        user.setEmailAddress(userDTO.getEmailAddress());
        user.setName(userDTO.getFirstName() + " " + userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAccountStatus('A'); //A - Active
        user = userRepository.save(user);
        return user;
    }

    @Override
    public UserDTO profile(Long user_id) {
        User user = userRepository.findByUserId(user_id);
        if (user == null) {
            return null;
        }
        return toDTO(user);
    }

    private UserDTO toDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setEmailAddress(user.getEmailAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        List<BookDTO> uploadedBooks  = bookTransformer.toBookDTOs(user.getUploadedBooks());
        userDTO.setUploadedBooks(uploadedBooks);
        List<NotesOutputDTO> notebookDTOS = new ArrayList<>();
        for (Notebook notebook : user.getNotebooks()) {
            NotesOutputDTO notebookDTO = notebookTransformer.toNotesOutputDTO(notebook);
            notebookDTOS.add(notebookDTO);
        }
        userDTO.setNotebooks(notebookDTOS);

        return userDTO;
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmailAddress(email);
        return toDTO(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(username);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserId().toString(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
