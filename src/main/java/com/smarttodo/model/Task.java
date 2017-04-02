package com.smarttodo.model;

import org.springframework.stereotype.Component;

/**
 * Created by kpfro on 4/2/2017.
 */


public class Task {

    private Long id;
    private String description;
    private boolean complete;

    public Task() {}

    public Task(Long id, String description, boolean complete) {
        this.id = id;
        this.description = description;
        this.complete = complete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
