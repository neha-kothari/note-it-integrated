package com.noteit.user;

import com.noteit.dto.UserRegistrationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Resource
    private UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registerUserAccount(@RequestBody UserRegistrationDTO registrationDto) {
        try {
            userService.addUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("{'data':'Registered Successfully'}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{'data':'Email already in use'}");
//			return "redirect:/registration?success";
        }
    }
}
