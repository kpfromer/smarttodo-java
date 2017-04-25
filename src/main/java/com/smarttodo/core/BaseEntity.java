package com.smarttodo.core;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by kpfromer on 4/12/17.
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    public BaseEntity() {
        id = null;
    }

    public BaseEntity(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
