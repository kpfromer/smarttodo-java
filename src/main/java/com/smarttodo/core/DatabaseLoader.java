package com.smarttodo.core;

import com.smarttodo.dao.RoleDao;
import com.smarttodo.dao.TaskDao;
import com.smarttodo.dao.UserDao;
import com.smarttodo.model.Event;
import com.smarttodo.model.Role;
import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kpfromer on 4/25/17.
 */

@Component
public class DatabaseLoader implements ApplicationRunner {

    private final RoleDao roleDao;
    private final UserDao userDao;
    private final TaskDao taskDao;

    @Autowired
    public DatabaseLoader(RoleDao roleDao, UserDao userDao, TaskDao taskDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.taskDao = taskDao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        roleDao.save(new Role(1L, "ROLE_USER"));

        Role role = roleDao.findByName("ROLE_USER");

        List<User> users = Arrays.asList(
                new User("user", "$2a$08$wgwoMKfYl5AUE9QtP4OjheNkkSDoqDmFGjjPE2XTPLDe9xso/hy7u", true, role, "ƒexampleemail@example.com"),
                new User("user2", "password", true, role, "exampleemail2@example.com")
        );

        userDao.save(users);

        List<Task> tasks = Arrays.asList(
                new Task("Code Task entity", true, userDao.findByUsername("user"), new Event.EventBuilder().withRecurring(false).withCompleted(true).build()),
                new Task("Discuss users and roles", false, userDao.findByUsername("user"), new Event()),
                new Task("Enable Spring Security", false, userDao.findByUsername("user2"), new Event.EventBuilder().withCompleted(false).withRecurring(true).build()),
                new Task("Test application", true, userDao.findByUsername("user2"), new Event.EventBuilder().withCompleted(true).withRecurring(false).build())
        );

        taskDao.save(tasks);

    }
}
