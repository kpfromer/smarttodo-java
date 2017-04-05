package com.smarttodo.service;

import com.smarttodo.dao.TaskDao;
import com.smarttodo.model.Task;
import com.smarttodo.service.exceptions.TaskAlreadyExistsException;
import com.smarttodo.service.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

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
    public Task findById(Long id) throws TaskNotFoundException {

        Task task = taskDao.findOne(id);

        if (task == null) {
            throw new TaskNotFoundException();
        }

        return task;

    }

    @Override
    public void toggleComplete(Long id) throws TaskNotFoundException {
        Task task = taskDao.findOne(id);

        if(task == null){
            throw new TaskNotFoundException();
        }

        task.setComplete(!task.isComplete());
        taskDao.updateForCurrentUser(task);
    }

    @Override
    public void save(Task task) {
        //todo: make sure that null value doesn't throw exception
        if (task.getId() != null && taskDao.exists(task.getId())) {
            throw new TaskAlreadyExistsException();
        }

        taskDao.saveForCurrentUser(task);
    }

    @Override
    public void update(Task task) {
        if(!taskDao.exists(task.getId())){
            throw new TaskNotFoundException();
        }
        taskDao.updateForCurrentUser(task);
    }
}
