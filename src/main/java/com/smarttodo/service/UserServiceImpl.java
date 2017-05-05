package com.smarttodo.service;

import com.smarttodo.dao.UserDao;
import com.smarttodo.dto.UserDto;
import com.smarttodo.model.User;
import com.smarttodo.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.smarttodo.model.User.PASSWORD_ENCODER;

/**
 * Created by kpfro on 4/2/2017.
 */
@PropertySource("application.properties")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment env;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    @Override
    public void registerNewUserAccount(UserDto userDto) {

        //todo: add null userDto protection
        //todo: add test for above

        if (userExist(userDto.getUsername())) {
            throw new UsernameAlreadyExistsException("There is an account with that username: " + userDto.getUsername());
        }

        if (emailExist(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("There is an account with that email address: " + userDto.getEmail());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setEnabled(false);
        user.setRole(roleService.findByName(env.getProperty("smarttodo.user.general")));
        userDao.save(user);
    }

    @Override
    public void updateRegisteredUser(User user) {

        if(!userExist(user.getUsername())){
            throw new UserNotFoundException();
        }

        userDao.save(user);

    }

    private boolean userExist(String username) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }


    private boolean emailExist(String email) {
        User user = userDao.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}
