package com.smarttodo.dao;

import com.smarttodo.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kpfromer on 4/7/17.
 */

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {


    //todo: create test
    Role findByName(String name);

}
