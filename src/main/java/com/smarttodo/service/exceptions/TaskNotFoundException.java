package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 4/4/17.
 */

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException() {
        super("Task could not be found.");
    }
}
