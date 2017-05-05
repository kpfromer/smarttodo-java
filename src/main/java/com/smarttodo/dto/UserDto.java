package com.smarttodo.dto;

import com.smarttodo.dto.validation.PasswordMatches;
import com.smarttodo.model.Role;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by kpfromer on 4/8/17.
 */

@PasswordMatches(message = "Passwords must match.")
public class UserDto {

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Only letters, and numbers are allowed.")
    @Size(min = 4, max = 20, message = "Username must be between 8 and 20 characters.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9\\-_!@#$%^&*()?]+$", message = "Only letters, numbers, and special characters are allowed.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    private String matchingPassword;

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

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }


    public static final class UserDtoBuilder {
        private String username;
        private String password;
        private String matchingPassword;
        private String email;

        public UserDtoBuilder() {
        }

        public static UserDtoBuilder anUserDto() {
            return new UserDtoBuilder();
        }

        public UserDtoBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserDtoBuilder withMatchingPassword(String matchingPassword) {
            this.matchingPassword = matchingPassword;
            return this;
        }

        public UserDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserDto build() {
            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setPassword(password);
            userDto.setMatchingPassword(matchingPassword);
            userDto.setEmail(email);
            return userDto;
        }
    }
}
