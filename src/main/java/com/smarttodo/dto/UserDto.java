package com.smarttodo.dto;

import com.smarttodo.model.Role;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

/**
 * Created by kpfromer on 4/8/17.
 */

public class UserDto {

    //todo: make sure username has no spaces
    @Size(min = 4, max = 20, message = "Username must be between 8 and 20 characters.")
    private String username;

    //todo: make sure password has no spaces
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;
    //todo: add password matching (http://www.baeldung.com/registration-with-spring-mvc-and-spring-security)

    @Email(message = "Email is not a well-formed.")
    @NotBlank(message = "Email can not be nothing.")
    private String email;

    public UserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
