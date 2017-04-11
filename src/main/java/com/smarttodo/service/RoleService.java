package com.smarttodo.service;

import com.smarttodo.model.Role;
import com.smarttodo.service.exceptions.RoleNotFoundException;

/**
 * Created by kpfromer on 4/7/17.
 */
public interface RoleService {

    Role findByName(String name) throws RoleNotFoundException;

}
