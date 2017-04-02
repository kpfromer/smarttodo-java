package com.smarttodo.service;

import com.smarttodo.dao.TaskDao;
import com.smarttodo.model.Task;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kpfro on 4/2/2017.
 */
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Override
    public Iterable<Task> findAll() {
        return taskDao.findAll();
    }

    @Override
    public Task findOne(Long id) {
        return taskDao.findOne(id);
    }

    @Override
    public void toggleComplete(Long id) {
        Task task = taskDao.findOne(id);
        task.setComplete(!task.isComplete());
        taskDao.save(task);
    }

    @Override
    public void save(Task task) {
        taskDao.save(task);
    }
}
