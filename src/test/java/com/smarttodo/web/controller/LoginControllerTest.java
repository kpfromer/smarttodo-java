package com.smarttodo.web.controller;

import com.smarttodo.dto.UserDto;
import com.smarttodo.dto.email.OnRegistrationCompleteEvent;
import com.smarttodo.model.User;
import com.smarttodo.model.VerificationToken;
import com.smarttodo.service.TokenService;
import com.smarttodo.service.UserService;
import com.smarttodo.service.exceptions.EmailAlreadyExistsException;
import com.smarttodo.service.exceptions.UsernameAlreadyExistsException;
import com.smarttodo.service.exceptions.VerificationTokenNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by kpfro on 4/8/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class LoginControllerTest {

    @Autowired
    private Environment env;

    private MockMvc mockMvc;

    @InjectMocks
    private LoginController controller;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private ApplicationEventPublisher eventPublisher;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix(env.getProperty("smarttodo.template.prefix"));
        viewResolver.setSuffix(env.getProperty("smarttodo.template.suffix"));
        viewResolver.setOrder(1);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void loginForm_ShouldIncludeNewUserInModel() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("user", instanceOf(User.class)));

    }

    @Test
    public void registerForm_ShouldIncludeNewUserDtoInModel() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("user", instanceOf(UserDto.class)));
    }

    @Test
    public void createUser_ShouldCreateUserWithValidCredentials() throws Exception {

        RequestBuilder request = post("/register")
                .param("username", "username12")
                .param("password", "password12")
                .param("matchingPassword", "password12")
                .param("email", "example@gmail.com")
                .with(csrf());

        User user = new User.UserBuilder()
                .withId(1L)
                .withEnabled(true)
                .withEmail("example@gmai.com")
                .withUsername("user")
                .withPassword("password")
                .build();

        doNothing().when(userService).registerNewUserAccount(any(UserDto.class));
        when(userService.loadUserByUsername(anyString())).thenReturn(new User());
        when(userService.findByUsername(anyString())).thenReturn(user);
        doNothing().when(eventPublisher).publishEvent(any(OnRegistrationCompleteEvent.class));

        mockMvc.perform(request).andExpect(redirectedUrl("/login"));
        verify(userService).registerNewUserAccount(any(UserDto.class));
        verify(eventPublisher).publishEvent(any(OnRegistrationCompleteEvent.class));
    }

    @Test
    public void createUser_ShouldRedirectToRegisterIfUserWasNotCreatedProperly() throws Exception {

        RequestBuilder request = post("/register")
                .param("username", "username12")
                .param("password", "password12")
                .param("matchingPassword", "password12")
                .param("email", "example@gmail.com")
                .with(csrf());

        doNothing().when(userService).registerNewUserAccount(any(UserDto.class));
        when(userService.loadUserByUsername(anyString())).thenReturn(new User());
        when(userService.findByUsername(anyString())).thenReturn(null);

        mockMvc.perform(request).andExpect(redirectedUrl("/register"));
        verify(userService).registerNewUserAccount(any(UserDto.class));
        verify(eventPublisher, never()).publishEvent(any(OnRegistrationCompleteEvent.class));
    }

    @Test
    public void createUser_ShouldRedirectToRegisterWhenUsernameAlreadyExistsExceptionIsThrown() throws Exception {

        RequestBuilder request = post("/register")
                .param("username", "username12")
                .param("password", "password12")
                .param("matchingPassword", "password12")
                .param("email", "example@gmail.com")
                .with(csrf());

        //Can't use when method since userService.registerNewUserAccount return type is void
        doThrow(new UsernameAlreadyExistsException()).when(userService).registerNewUserAccount(any(UserDto.class));

        mockMvc.perform(request).andExpect(redirectedUrl("/register"));
        verify(userService).registerNewUserAccount(any(UserDto.class));
    }

    @Test
    public void createUser_ShouldRedirectToRegisterWhenEmailAlreadyExistsExceptionIsThrown() throws Exception {


        RequestBuilder request = post("/register")
                .param("username", "username12")
                .param("password", "password12")
                .param("matchingPassword", "password12")
                .param("email", "example@gmail.com")
                .with(csrf());

        //Can't use when method since userService.registerNewUserAccount return type is void
        doThrow(new EmailAlreadyExistsException()).when(userService).registerNewUserAccount(any(UserDto.class));

        mockMvc.perform(request).andExpect(redirectedUrl("/register"));
        verify(userService).registerNewUserAccount(any(UserDto.class));
    }

//    Validation Checks

    @Test
    public void createUser_ShouldIncludeNewUserDtoAndBindingResultInModelWhenValidationErrors() throws Exception {
        RequestBuilder requestInvalidUsername = post("/register")
                .param("username", "@#$")
                .param("password", "password12")
                .param("matchingPassword", "password12")
                .param("email", "example@gmail.com")
                .with(csrf());

        RequestBuilder requestInvalidPassword = post("/register")
                .param("username", "username")
                .param("password", "][][][];';',.,.,.<>")
                .param("matchingPassword", "][][][];';',.,.,.<>")
                .param("email", "example@gmail.com")
                .with(csrf());

        RequestBuilder requestInvalidEmail = post("/register")
                .param("username", "username")
                .param("password", "password12")
                .param("matchingPassword", "password12")
                .param("email", "dja;sfadsf")
                .with(csrf());

        RequestBuilder requestDifferentMatchingPassword = post("/register")
                .param("username", "username")
                .param("password", "password12")
                .param("matchingPassword", "passworaddD@DAD")
                .param("email", "example@gmail.com")
                .with(csrf());

        RequestBuilder[] requests = {requestInvalidUsername, requestInvalidPassword, requestInvalidEmail, requestDifferentMatchingPassword};

        for (RequestBuilder request : requests) {
            mockMvc.perform(request)
                    .andExpect(view().name("register"))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("user", instanceOf(UserDto.class)))
                    .andExpect(model().attribute("org.springframework.validation.BindingResult.user", instanceOf(BindingResult.class)));
            verify(userService, never()).registerNewUserAccount(any(UserDto.class));
        }

    }

    //todo: add createUser test

    @Test
    public void confirmRegistration_ShouldConfirmRegistration() throws Exception {
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
                .withExpiryDate(LocalDateTime.of(3000, 4, 4, 0, 0))//notice the year!
                .withUser(user)
                .build();

        when(tokenService.getVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .thenReturn(verificationToken);
        doNothing().when(userService).updateRegisteredUser(any(User.class));

        mockMvc.perform(get("/registrationConfirm").param("token", "b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .andExpect(redirectedUrl("/login"));
        verify(tokenService).getVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a");
        verify(userService).updateRegisteredUser(any(User.class));
    }

    @Test
    public void confirmRegistration_ShouldRenderBadTokenWhenVerificationTokenNotFoundExceptionIsThrown() throws Exception {
        when(tokenService.getVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .thenThrow(new VerificationTokenNotFoundException());
        doNothing().when(userService).updateRegisteredUser(any(User.class));

        mockMvc.perform(get("/registrationConfirm").param("token", "b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .andExpect(status().isOk())
                .andExpect(view().name("badToken"))
                .andExpect(model().attribute("message", notNullValue()));
        verify(tokenService).getVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a");
        verify(userService, never()).updateRegisteredUser(any(User.class));
    }

    @Test
    public void confirmRegistration_ShouldRenderBadTokenWhenTokenIsExpired() throws Exception {
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
                .withExpiryDate(LocalDateTime.of(1990, 4, 4, 0, 0))//notice the year!
                .withUser(user)
                .build();

        when(tokenService.getVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .thenReturn(verificationToken);
        doNothing().when(userService).updateRegisteredUser(any(User.class));

        mockMvc.perform(get("/registrationConfirm").param("token", "b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .andExpect(status().isOk())
                .andExpect(view().name("badToken"))
                .andExpect(model().attribute("message", notNullValue()))
                .andExpect(model().attribute("expired", true))
                .andExpect(model().attribute("token","b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"));

        verify(tokenService).getVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a");
        verify(userService, never()).updateRegisteredUser(any(User.class));
    }

    @Test
    public void resendRegistrationToken_ShouldResendRegistrationToken() throws Exception {

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
                .withExpiryDate(LocalDateTime.of(1990, 4, 4, 0, 0))//notice the year!
                .withUser(user)
                .build();


        when(tokenService.generateNewVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .thenReturn(verificationToken);
        doNothing().when(eventPublisher).publishEvent(any(OnRegistrationCompleteEvent.class));

        mockMvc.perform(get("/resendRegistrationToken").param("token", "b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .andExpect(redirectedUrl("/login"));

        verify(tokenService).generateNewVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a");
        verify(eventPublisher).publishEvent(any(OnRegistrationCompleteEvent.class));
    }

    @Test
    public void resendRegistrationToken_ShouldRenderBadTokenWhenVerificationTokenNotFoundExceptionIsThrown() throws Exception {

        when(tokenService.generateNewVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .thenThrow(new VerificationTokenNotFoundException());

        mockMvc.perform(get("/resendRegistrationToken").param("token", "b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a"))
                .andExpect(status().isOk())
                .andExpect(view().name("badToken"))
                .andExpect(model().attribute("message", notNullValue()));

        verify(tokenService).generateNewVerificationToken("b3c8ce26-ff00-4c77-b3b1-b4e8c69de37a");
        verify(eventPublisher, never()).publishEvent(any(OnRegistrationCompleteEvent.class));
    }
}