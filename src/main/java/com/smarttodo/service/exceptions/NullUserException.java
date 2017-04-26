package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 4/25/17.
 */
public class NullUserException extends RuntimeException {
    public NullUserException() {
        super("User does not exist.");
    }
}
