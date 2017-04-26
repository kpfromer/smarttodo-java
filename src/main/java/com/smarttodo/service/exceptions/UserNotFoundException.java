package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 4/25/17.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User was not found.");
    }
}
