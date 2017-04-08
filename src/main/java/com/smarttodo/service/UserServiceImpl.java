package com.smarttodo.service;

import com.smarttodo.dao.UserDao;
import com.smarttodo.model.User;
import com.smarttodo.service.exceptions.RoleNotFoundException;
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
        return userDao.findByUsername(username);
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
    public void save(User user) throws RoleNotFoundException {
        user.setRole(roleService.findByName(env.getProperty("smarttodo.user.general")));
        user.setEnabled(true);
        //todo: fix improper way of encoding user password (going to fix in next commit) http://www.baeldung.com/registration-with-spring-mvc-and-spring-security
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        userDao.save(user);
    }
}
