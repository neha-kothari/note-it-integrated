package com.noteit.user;

import com.noteit.dto.UserDTO;
import com.noteit.dto.UserRegistrationDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    User addUser(UserRegistrationDTO userDTO);
    UserDTO profile(Long user_id);
    UserDTO getUserByEmail(String email);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
