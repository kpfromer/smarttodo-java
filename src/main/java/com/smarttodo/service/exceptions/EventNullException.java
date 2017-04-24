package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 4/22/17.
 */
public class EventNullException extends RuntimeException {
    public EventNullException() {
        super("Event can not be null.");
    }
}
