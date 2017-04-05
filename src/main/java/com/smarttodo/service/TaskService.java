package com.smarttodo.service;

import com.smarttodo.model.Task;

/**
 * Created by kpfro on 4/2/2017.
 */
public interface TaskService {
    Iterable<Task> findAll();
    Task findById(Long id);
    void toggleComplete(Long id);
    void save(Task task);
}
