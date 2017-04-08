package com.smarttodo.dao;

import com.smarttodo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kpfro on 4/2/2017.
 */
@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
