package com.noteit.user;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noteit.book.BookService;
import com.noteit.config.JwtTokenProvider;
import com.noteit.dto.*;
import com.noteit.notebook.NotebookService;
import org.apache.logging.log4j.util.Strings;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Collections;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private BookService bookService;

    @Resource
    private NotebookService notebookService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenProvider tokenProvider;

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @PostMapping(path = "/register")
    public boolean registerUser(@RequestBody UserDTO request) {
        //return userService.getLoginStatus(request);
        return false;
    }

    @PostMapping(path ="/users/{user_id}/upload")
    public ResponseEntity<BookDetailsDTO> uploadBook(@RequestParam("json") String requestString, @RequestParam("file") MultipartFile bookFile, @PathVariable Long user_id) throws Exception {

        BookDetailsDTO request = new ObjectMapper().readValue(requestString, BookDetailsDTO.class);
        if (validateUploadRequest(request)) {
            return ResponseEntity.created(null)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bookService.addBook(request, bookFile, user_id));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(request);
        }
    }



    @PostMapping(path ="/users/{user_id}/notes")
    public ResponseEntity<NotebookDTO> saveNotes(@RequestBody NotebookDTO notebookDTO, @PathVariable Long user_id) throws Exception {

        notebookService.saveNotes(notebookDTO, user_id);
        return ResponseEntity.created(null)
                .contentType(MediaType.APPLICATION_JSON)
                .body(null);

    }

    @GetMapping(path ="/users/{user_id}/notes")
    public ResponseEntity<NotesOutputDTO> getNotes(@PathVariable Long user_id) throws Exception {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(notebookService.getNotes(user_id));

    }

    @GetMapping(path ="/users/{user_id}/profile")
    public ResponseEntity<Object> getProfile(@PathVariable Long user_id) throws Exception {

        UserDTO userDTO = userService.profile(user_id);
        if (null != userDTO) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userService.profile(user_id));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID does not exist!");

    }

    @DeleteMapping(path ="/users/{user_id}/notes/{chapter_id}")
    public ResponseEntity<NotesOutputDTO> deleteFromNotes(@PathVariable Long user_id, @PathVariable Long chapter_id) throws Exception {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(notebookService.deleteChapter(user_id, chapter_id));

    }

    @PostMapping(path ="/users/{user_id}/notes/merge")
    public ResponseEntity<ByteArrayResource> mergeNotes(@RequestBody NotebookDTO notebookDTO, @PathVariable Long user_id) throws Exception {

        if (notebookDTO.getCustom_name() == null || notebookDTO.getCustom_name().isEmpty()) {
            throw new Exception("Invalid Notebook Name");
        }
        try {
            FileDTO file = notebookService.mergeNotes(notebookDTO, user_id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\\"+file.getFileName()+"\\")
                    .body(file.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping(path ="/users/trial")
    public ResponseEntity<User> addUser(@RequestBody UserRegistrationDTO user) throws Exception {

        return ResponseEntity.created(null)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.addUser(user));
    }

    private boolean validateUploadRequest(BookDetailsDTO request) {

        if (Strings.isBlank(request.getBookName())) {
            request.setError("Book Title should not be blank.");
            return false;
        } else if (Strings.isBlank(request.getAuthor())) {
            request.setError("Author should not be blank.");
            return false;
        } else if (Strings.isBlank(request.getDescription())) {
            request.setError("Description should not be blank.");
            return false;
        } else if (bookService.isbnExists(request.getIsbnNumber())) {
            request.setError("Book with this ISBN number exists already.");
            return false;
        }
        return true;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest user) throws Exception {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))));
            if (authentication.isAuthenticated()) {
                String email = user.getUsername();
                UserDTO userDTO = userService.getUserByEmail(email);
                final String jwtToken =  tokenProvider.createToken(email);
                return ResponseEntity.ok(new AuthenticationResponse(userDTO.getUserId(), jwtToken));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Credentials");
            }
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("Invalid credentials!", e);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exception!!");
        }
    }
}
