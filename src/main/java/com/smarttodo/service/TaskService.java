package com.smarttodo.service;

import com.smarttodo.model.Task;
import com.smarttodo.service.exceptions.TaskAlreadyExistsException;
import com.smarttodo.service.exceptions.TaskNotFoundException;

/**
 * Created by kpfro on 4/2/2017.
 */
public interface TaskService {
    //todo: add throws exceptions!
    Iterable<Task> findAll();
    Task findById(Long id) throws TaskNotFoundException;
    void toggleComplete(Long id) throws TaskNotFoundException;
    void save(Task task) throws TaskAlreadyExistsException;
    void update(Task task) throws TaskNotFoundException;
}
