package com.smarttodo.dao;

import com.smarttodo.model.User;
import com.smarttodo.model.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kpfromer on 4/25/17.
 */

//todo: create test
@Repository
public interface TokenDao extends CrudRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
