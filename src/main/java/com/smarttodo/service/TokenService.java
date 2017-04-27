package com.smarttodo.service;

import com.smarttodo.dto.UserDto;
import com.smarttodo.model.User;
import com.smarttodo.model.VerificationToken;
import com.smarttodo.service.exceptions.EmailAlreadyExistsException;
import com.smarttodo.service.exceptions.UserNotFoundException;
import com.smarttodo.service.exceptions.VerificationTokenNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by kpfromer on 4/25/17.
 */

public interface TokenService {

    User getUser(String verificationToken) throws UserNotFoundException;

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken) throws VerificationTokenNotFoundException;

    VerificationToken generateNewVerificationToken(String existingToken) throws VerificationTokenNotFoundException;

}
