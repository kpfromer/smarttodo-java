package com.smarttodo.service;

import com.smarttodo.dao.TokenDao;
import com.smarttodo.model.User;
import com.smarttodo.model.VerificationToken;
import com.smarttodo.service.exceptions.UserNotFoundException;
import com.smarttodo.service.exceptions.VerificationTokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kpfromer on 4/25/17.
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenDao dao;

    @Override
    public User getUser(String verificationToken) {
        User user = dao.findByToken(verificationToken).getUser();
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        myToken.newExpiryDate();
        dao.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) throws VerificationTokenNotFoundException {
        VerificationToken token = dao.findByToken(VerificationToken);
        if (token == null) {
            throw new VerificationTokenNotFoundException();
        }
        return token;
    }

    @Override
    public VerificationToken generateNewVerificationToken(String existingToken) throws  VerificationTokenNotFoundException {
        VerificationToken newVerificationToken = getVerificationToken(existingToken);
        newVerificationToken.newExpiryDate();
        return newVerificationToken;
    }
}
