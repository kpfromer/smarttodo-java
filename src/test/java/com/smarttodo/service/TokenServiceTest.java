package com.smarttodo.service;

import com.smarttodo.dao.TokenDao;
import com.smarttodo.model.User;
import com.smarttodo.model.VerificationToken;
import com.smarttodo.service.exceptions.UserNotFoundException;
import com.smarttodo.service.exceptions.VerificationTokenNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
//import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.*;

/**
 * Created by kpfromer on 4/26/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    @Mock
    private TokenDao dao;

    @InjectMocks
    private TokenService service = new TokenServiceImpl();

    @Test
    public void getUser_ShouldRetrieveUser() throws Exception {
        User user = new User.UserBuilder()
                .withId(1L)
                .withEmail("example@gmail.com")
                .withEnabled(false)
                .withUsername("user")
                .withPassword("unencrypted password")
                .build();

        VerificationToken verificationToken = new VerificationToken.VerificationTokenBuilder()
                .withId(1L)
                .withToken("b08efd06-2439-4e82-b67b-ec4c1d956256")
                .withExpiryDate(LocalDateTime.of(2014, 4, 4, 0, 0))
                .withUser(user)
                .build();

        when(dao.findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256")).thenReturn(verificationToken);
        User returnedUser = service.getUser("b08efd06-2439-4e82-b67b-ec4c1d956256");
        assertThat(returnedUser, notNullValue(User.class));
        verify(dao).findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256");
    }

    @Test(expected = UserNotFoundException.class)
    public void getUser_ShouldThrowUserNotFoundException() throws Exception {
        VerificationToken verificationToken = new VerificationToken.VerificationTokenBuilder()
                .withId(1L)
                .withToken("b08efd06-2439-4e82-b67b-ec4c1d956256")
                .withExpiryDate(LocalDateTime.of(2014, 4, 4, 0, 0))
                .withUser(null)
                .build();

        when(dao.findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256")).thenReturn(verificationToken);
        service.getUser("b08efd06-2439-4e82-b67b-ec4c1d956256");
        verify(dao).findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256");
    }

    @Test
    public void createVerificationToken_ShouldCreateVerificationToken() throws Exception {
        User user = new User.UserBuilder()
                .withId(1L)
                .withEmail("example@gmail.com")
                .withEnabled(false)
                .withUsername("user")
                .withPassword("unencrypted password")
                .build();

        service.createVerificationToken(user, "b08efd06-2439-4e82-b67b-ec4c1d956256");
        ArgumentCaptor<VerificationToken> verificationTokenArgumentCaptor = ArgumentCaptor.forClass(VerificationToken.class);
        verify(dao).save(verificationTokenArgumentCaptor.capture());
        assertThat(verificationTokenArgumentCaptor.getValue(), notNullValue(VerificationToken.class));
    }

    @Test
    public void getVerificationToken_ShouldGetVerificationToken() throws Exception {
        User user = new User.UserBuilder()
                .withId(1L)
                .withEmail("example@gmail.com")
                .withEnabled(false)
                .withUsername("user")
                .withPassword("unencrypted password")
                .build();

        VerificationToken verificationToken = new VerificationToken.VerificationTokenBuilder()
                .withId(1L)
                .withToken("b08efd06-2439-4e82-b67b-ec4c1d956256")
                .withExpiryDate(LocalDateTime.of(2014, 4, 4, 0, 0))
                .withUser(user)
                .build();

        when(dao.findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256")).thenReturn(verificationToken);
        assertThat(service.getVerificationToken("b08efd06-2439-4e82-b67b-ec4c1d956256"),
                equalTo(verificationToken));
        verify(dao).findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256");
    }

    @Test(expected = VerificationTokenNotFoundException.class)
    public void getVerificationToken_ShouldThrowVerificationTokenNotFoundException() throws Exception {
        when(dao.findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256")).thenReturn(null);
        service.getVerificationToken("b08efd06-2439-4e82-b67b-ec4c1d956256");
        verify(dao).findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256");
    }

    @Test
    public void generateNewVerificationToken_ShouldGenerateNewVerificationToken() throws Exception {
        User user = new User.UserBuilder()
                .withId(1L)
                .withEmail("example@gmail.com")
                .withEnabled(false)
                .withUsername("user")
                .withPassword("unencrypted password")
                .build();

        LocalDateTime initialExpiryDate = LocalDateTime.of(2014, 4, 4, 0, 0);
        VerificationToken verificationToken = new VerificationToken.VerificationTokenBuilder()
                .withId(1L)
                .withToken("b08efd06-2439-4e82-b67b-ec4c1d956256")
                .withExpiryDate(initialExpiryDate)
                .withUser(user)
                .build();

        when(dao.findByToken("b08efd06-2439-4e82-b67b-ec4c1d956256")).thenReturn(verificationToken);
        VerificationToken newVerificationToken = service.generateNewVerificationToken("b08efd06-2439-4e82-b67b-ec4c1d956256");

        assertThat(newVerificationToken.getExpiryDate(), notNullValue());
        assertThat(newVerificationToken.getExpiryDate(), not(initialExpiryDate));

    }
}