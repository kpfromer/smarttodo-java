package com.smarttodo.service;

import com.smarttodo.dao.RoleDao;
import com.smarttodo.model.Role;
import com.smarttodo.service.exceptions.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kpfromer on 4/7/17.
 */

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao dao;

    @Override
    public Role findByName(String name) throws RoleNotFoundException {

        Role role = dao.findByName(name);

        if(role == null){
            throw new RoleNotFoundException();
        }

        return role;
    }
}
