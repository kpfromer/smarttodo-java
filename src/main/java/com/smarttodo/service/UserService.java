package com.smarttodo.service;

import com.smarttodo.dto.UserDto;
import com.smarttodo.model.User;
import com.smarttodo.service.exceptions.EmailAlreadyExistsException;
import com.smarttodo.service.exceptions.RoleNotFoundException;
import com.smarttodo.service.exceptions.UserNotFoundException;
import com.smarttodo.service.exceptions.UsernameAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by kpfro on 4/2/2017.
 */
public interface UserService extends UserDetailsService {

    User findByUsername(String username) throws UserNotFoundException;

    void registerNewUserAccount(UserDto userDto) throws UsernameAlreadyExistsException, EmailAlreadyExistsException;

    void updateRegisteredUser(User user) throws UserNotFoundException ;

}
