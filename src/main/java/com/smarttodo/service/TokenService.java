package com.smarttodo.service;

import com.smarttodo.dto.UserDto;
import com.smarttodo.model.User;
import com.smarttodo.model.VerificationToken;
import com.smarttodo.service.exceptions.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;

/**
 * Created by kpfromer on 4/25/17.
 */

//todo: create test
public interface TokenService {

    User getUser(String verificationToken);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String existingToken);

}
