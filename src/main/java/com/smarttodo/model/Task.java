package com.smarttodo.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by kpfro on 4/2/2017.
 */

@Entity
public class Task {

    //todo: add duedate
    //todo: UI Use chips for duedate in the text field: http://materializecss.com/chips.html


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private boolean complete;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Task() {}

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public static final class TaskBuilder {
        private Long id;
        private String description;
        private boolean complete;
        private User user;

        public TaskBuilder() {
        }

        public static TaskBuilder aTask() {
            return new TaskBuilder();
        }

        public TaskBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TaskBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder withComplete(boolean complete) {
            this.complete = complete;
            return this;
        }


        public Task build() {
            Task task = new Task();
            task.setId(id);
            task.setDescription(description);
            task.setComplete(complete);
            return task;
        }
    }
}
