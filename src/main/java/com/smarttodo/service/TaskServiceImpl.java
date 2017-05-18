package com.smarttodo.service;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import com.smarttodo.dao.TaskDao;
import com.smarttodo.dao.UserDao;
import com.smarttodo.dto.EditedTextAndEvent;
import com.smarttodo.model.Event;
import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import com.smarttodo.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kpfro on 4/2/2017.
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Override
    public Iterable<Task> findAll() {
        return taskDao.findAll();
    }

    @Override
    public Task findById(Long id) throws TaskNotFoundException, EventNullException {

        Task task = taskDao.findOne(id);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        if(task.getEvent() == null){
            throw new EventNullException();
        }

        return task;

    }

    @Override
    public void toggleComplete(Long id) {

        Task task = findById(id);

        if(!task.isComplete()) {
            task.complete();

            if (task.getEvent().isCompleted()) {
                task.setComplete(true);
            }
        } else {
            task.setComplete(false);
        }

        this.saveOrUpdate(task);
    }



    @Override
    public void saveOrUpdate(Task task) throws UsernameNotFoundException, DescriptionNullException {

        if(task.getId() != null) {
            Task databaseTask = taskDao.findOne(task.getId());

            if (!databaseTask.getUser().equals(task.getUser())) {
                throw new InvalidTaskId();
            }
        }


        //todo: add event null exception
        if (task.getEvent() == null){
            task.setEvent(new Event());
        }

        if (task.getUser() == null){
            throw new UserNotFoundException();
        }

        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            throw new DescriptionNullException();
        }
        
        taskDao.save(task);
    }

}
