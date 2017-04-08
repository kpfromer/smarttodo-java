package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 4/8/17.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email already exists.");
    }
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
