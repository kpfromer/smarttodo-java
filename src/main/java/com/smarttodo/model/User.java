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

@Entity
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

}
