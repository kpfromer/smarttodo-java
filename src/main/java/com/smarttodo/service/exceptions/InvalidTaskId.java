package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 5/18/17.
 */
public class InvalidTaskId extends RuntimeException {
    public InvalidTaskId() {
        super("The Task id is invalid.");
    }
}
