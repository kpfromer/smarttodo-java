package com.smarttodo.core;

import com.smarttodo.dao.RoleDao;
import com.smarttodo.dao.TaskDao;
import com.smarttodo.dao.UserDao;
import com.smarttodo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by kpfromer on 5/2/17.
 */

@Component
public class DatabaseLoader implements ApplicationRunner {

    private final RoleDao roleDao;

    @Autowired
    public DatabaseLoader(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        roleDao.save(new Role(1L, "ROLE_USER"));
    }
}
