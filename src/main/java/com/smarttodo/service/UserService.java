package com.smarttodo.service;

import com.smarttodo.model.User;
import com.smarttodo.service.exceptions.RoleNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by kpfro on 4/2/2017.
 */
public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    void save(User user);
}
