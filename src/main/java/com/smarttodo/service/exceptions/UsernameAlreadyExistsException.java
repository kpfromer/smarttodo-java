package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 4/8/17.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Username already exists.");
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
