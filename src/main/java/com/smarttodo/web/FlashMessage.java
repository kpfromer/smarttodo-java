package com.smarttodo.web;

public class FlashMessage {
    private String message;
    private Status status;

    public FlashMessage(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static enum Status {
        SUCCESS,
        INFO,
        FAILURE
    }

    public String getStatusColor(){
        if (this.getStatus() == Status.SUCCESS) {
            return "green";
        } else if (this.getStatus() == Status.INFO){
            return "deep-purple";
        } else {
            return "red";
        }
    }
}