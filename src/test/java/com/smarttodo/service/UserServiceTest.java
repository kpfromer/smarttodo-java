package com.smarttodo.service;

import com.smarttodo.dao.UserDao;
import com.smarttodo.dto.UserDto;
import com.smarttodo.model.Role;
import com.smarttodo.model.User;
import com.smarttodo.service.exceptions.EmailAlreadyExistsException;
import com.smarttodo.service.exceptions.UserNotFoundException;
import com.smarttodo.service.exceptions.UsernameAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by kpfromer on 5/4/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private RoleService roleService;

    @Mock
    private Environment env;

    @Mock
    private UserDao userDao;
    
    @InjectMocks
    private UserService service = new UserServiceImpl();

    @Test(expected = UserNotFoundException.class)
    public void findByUsername_ShouldThrowUserNotFoundException() throws Exception {
        when(userDao.findByUsername("user")).thenReturn(null);
        service.findByUsername("user");
    }

    @Test
    public void registerNewUserAccount_ShouldRegisterANewUser() throws Exception {
        UserDto userDto = new UserDto.UserDtoBuilder()
                .withUsername("user")
                .withPassword("password12")
                .withMatchingPassword("password12")
                .withEmail("example@gmail.com")
                .build();

        when(userDao.findByUsername("user")).thenReturn(null);
        when(userDao.findByEmail("example@gmail.com")).thenReturn(null);
        when(env.getProperty(anyString())).thenReturn("ROLE_USER");
        when(roleService.findByName(anyString())).thenReturn(new Role(1L, "ROLE_USER"));
        service.registerNewUserAccount(userDto);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userDao).save(userArgumentCaptor.capture());

        User user = userArgumentCaptor.getValue();

        assertThat(user.getUsername(), notNullValue());
        assertThat(user.getPassword(), notNullValue());
        assertThat(user.getEmail(), notNullValue());
        assertThat(user.getRole(), notNullValue());
        assertThat(user.isEnabled(), equalTo(false));
    }

    @Test(expected = UsernameAlreadyExistsException.class)
    public void registerNewUserAccount_ShouldThrowUsernameAlreadyExistsException() throws Exception {
        UserDto userDto = new UserDto.UserDtoBuilder()
                .withUsername("user")
                .withPassword("password12")
                .withMatchingPassword("password12")
                .withEmail("example@gmail.com")
                .build();

        User user =  new User.UserBuilder()
                .withId(1L)
                .withUsername("user")
                .withPassword("dasdasd")
                .withEmail("fakeemail@gmail.com")
                .withEnabled(true)
                .build();

        when(userDao.findByUsername("user")).thenReturn(user);
        when(userDao.findByEmail("example@gmail.com")).thenReturn(null);
        service.registerNewUserAccount(userDto);
        verify(userDao, never()).save(any(User.class));
    }

    @Test(expected = EmailAlreadyExistsException.class)
    public void registerNewUserAccount_ShouldThrowEmailAlreadyExistsException() throws Exception {
        UserDto userDto = new UserDto.UserDtoBuilder()
                .withUsername("user")
                .withPassword("password12")
                .withMatchingPassword("password12")
                .withEmail("example@gmail.com")
                .build();

        User user =  new User.UserBuilder()
                .withId(1L)
                .withUsername("usedasdr31dsdasd")
                .withPassword("dasdfasdfasd")
                .withEmail("example@gmail.com")
                .withEnabled(true)
                .build();

        when(userDao.findByUsername("user")).thenReturn(null);
        when(userDao.findByEmail("example@gmail.com")).thenReturn(user);
        service.registerNewUserAccount(userDto);
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    public void updateRegisteredUser_ShouldUpdateUser() throws Exception {

        User newUser =  new User.UserBuilder()
                .withId(1L)
                .withUsername("user")
                .withPassword("password")
                .withEmail("example@gmail.com")
                .withEnabled(true)
                .build();

        User oldUser =  new User.UserBuilder()
                .withId(1L)
                .withUsername("user")
                .withPassword("password")
                .withEmail("example@gmail.com")
                .withEnabled(true)
                .build();

        when(userDao.findByUsername("user")).thenReturn(oldUser);
        service.updateRegisteredUser(newUser);
        verify(userDao).save(any(User.class));
    }

    @Test(expected = UserNotFoundException.class)
    public void updateRegisteredUser_ShouldThrowUserNotFoundException() throws Exception {
        User user =  new User.UserBuilder()
                .withId(1L)
                .withUsername("user")
                .withPassword("password")
                .withEmail("example@gmail.com")
                .withEnabled(true)
                .build();

        when(userDao.findByUsername("user")).thenReturn(null);
        service.updateRegisteredUser(user);
        verify(userDao, never()).save(any(User.class));
    }
}