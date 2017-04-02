package com.smarttodo.dao;

import com.smarttodo.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by kpfro on 4/2/2017.
 */
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
