package com.smarttodo.model;

import com.smarttodo.core.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by kpfro on 4/2/2017.
 */

@Entity
public class Task extends BaseEntity {

    //todo: UI Use chips for duedate in the text field: http://materializecss.com/chips.html


    //todo: add test for non nullables
    //todo: make sure this are not nullable
    @NotNull(message = "Task description can not be null.")
    @NotBlank(message = "Task description can not be nothing.")
    private String description;

    //todo: make sure this are not nullable
    @NotNull(message = "Task must either be completed or not.")
    private boolean complete;

    //todo: make sure this are not nullable
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    @Column(nullable = false)
    private Event event;

    public Task() {
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public static final class TaskBuilder {
        private Long id;
        private String description;
        private boolean complete;
        private User user;
        private Event event;

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

        public TaskBuilder withEvent(Event event){
            this.event = event;
            return this;
        }


        public Task build() {
            Task task = new Task();
            task.setId(id);
            task.setDescription(description);
            task.setComplete(complete);
            task.setEvent(event);
            return task;
        }
    }
}
