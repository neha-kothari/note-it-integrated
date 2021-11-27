package com.noteit.notebook;

import com.noteit.book.BookService;
import com.noteit.dto.BookDetailsDTO;
import com.noteit.dto.FileDTO;
import com.noteit.dto.NotebookDTO;
import com.noteit.user.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/notes")
public class NotebookController {

    @Resource
    private NotebookService notebookService;


    @GetMapping("/{notebook_id}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long notebook_id) {

        try {
            FileDTO file = notebookService.downloadNotes(notebook_id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\\"+file.getFileName()+"\\")
                    .body(file.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
