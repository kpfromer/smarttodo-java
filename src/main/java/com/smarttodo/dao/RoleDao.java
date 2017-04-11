package com.smarttodo.dao;

import com.smarttodo.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kpfromer on 4/7/17.
 */

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {

    Role findByName(String name);

}
