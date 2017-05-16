package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 5/16/17.
 */
public class DescriptionNullException extends RuntimeException {
    public DescriptionNullException() {
        super("Description can not be null.");
    }
}
