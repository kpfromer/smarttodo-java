package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 4/7/17.
 */
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
        super("Role was not found.");
    }
}
