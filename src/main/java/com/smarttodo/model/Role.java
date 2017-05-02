package com.smarttodo.model;

import com.smarttodo.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by kpfro on 4/2/2017.
 */


//todo: maybe add "paid users" that get more features - in short more Roles
@Entity
public class Role extends BaseEntity {

    private String name;

    public Role() {
        super();
    }

    public Role(Long id){
        super(id);
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
