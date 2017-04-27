package com.smarttodo.service.exceptions;

/**
 * Created by kpfromer on 4/26/17.
 */
public class VerificationTokenNotFoundException extends RuntimeException {
    public VerificationTokenNotFoundException() {
        super("Verification token was not found.");
    }
}
