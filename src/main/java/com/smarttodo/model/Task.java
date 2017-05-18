package com.smarttodo.model;

import com.smarttodo.core.BaseEntity;
import com.smarttodo.service.exceptions.UserNotFoundException;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by kpfro on 4/2/2017.
 */

@Entity
public class Task extends BaseEntity {

    @NotNull(message = "Task description can not be null.")
    @NotBlank(message = "Task description can not be nothing.")
    private String description;

    @NotNull(message = "Task must either be completed or not.")
    private boolean complete;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    @Column(nullable = false)
    private Event event;

    public Task() {
        super();
    }

    public Task(Long id){
        super(id);
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

    public void complete() {
        event.complete();
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

        public TaskBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public Task build() {
            Task task = new Task(id);
            task.setDescription(description);
            task.setComplete(complete);
            task.setEvent(event);
            task.setUser(user);
            return task;
        }
    }
}
