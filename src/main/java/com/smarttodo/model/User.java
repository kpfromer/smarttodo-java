package com.smarttodo.model;

import com.smarttodo.core.BaseEntity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by kpfro on 4/2/2017.
 */

@Entity(name = "SmarttodoUser")
public class User extends BaseEntity implements UserDetails {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(10);

    @Column(unique = true)
    @Size(min = 4, max = 20, message = "Username must be between 8 and 20 characters.")
    private String username;

    @Column(length = 100)
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    //One user has one role
    //todo: allow for multiple roles
    @JoinColumn(name = "role_id")//gets role id
    @OneToOne
    private Role role;

    @Column(unique = true)
    @Email(message = "Email is not a well-formed.")
    @NotBlank(message = "Email can not be nothing.")
    private String email;

    //todo: allow for password resets


    public User() {
        super();
        this.enabled = false;
    }

    public User(Long id){
        super(id);
        this.enabled = false;
    }

    public User(String username, String password, boolean enabled, Role role, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //todo: add ability for accounts to expire
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //todo: add ability for accounts to be locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //todo: add ability for credentials to expire
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public static final class UserBuilder {
        private Long id;
        private String username;
        private String password;
        private boolean enabled;
        private Role role;
        private String email;

        public UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserBuilder withRole(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            User user = new User(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setEnabled(enabled);
            user.setRole(role);
            user.setEmail(email);
            return user;
        }
    }
}
