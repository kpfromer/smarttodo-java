package com.smarttodo.web.controller;

import com.smarttodo.config.TemplateConfig;
import com.smarttodo.dto.UserDto;
import com.smarttodo.model.User;
import com.smarttodo.service.UserService;
import com.smarttodo.service.exceptions.EmailAlreadyExistsException;
import com.smarttodo.service.exceptions.UsernameAlreadyExistsException;
import org.apache.poi.ss.formula.functions.T;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Created by kpfro on 4/8/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private LoginController controller;

    @Mock
    private UserService service;

    @Before
    public void setUp() throws Exception {

//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();


        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //todo: get with property source
        viewResolver.setPrefix("classpath:/templates/");
        //todo: get with property source
        viewResolver.setSuffix(".html");
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
                .param("email", "example@gmail.com")
                .with(csrf());

        doNothing().when(service).registerNewUserAccount(any(UserDto.class));

        mockMvc.perform(request).andExpect(redirectedUrl("/login"));
        verify(service).registerNewUserAccount(any(UserDto.class));
    }

    @Test
    public void createUser_ShouldRedirectToRegisterWhenUsernameAlreadyExistsExceptionIsThrown() throws Exception {

        RequestBuilder request = post("/register")
                .param("username", "username12")
                .param("password", "password12")
                .param("email", "example@gmail.com")
                .with(csrf());

        //Can't use when method since service.registerNewUserAccount return type is void
        doThrow(new UsernameAlreadyExistsException()).when(service).registerNewUserAccount(any(UserDto.class));

        mockMvc.perform(request).andExpect(redirectedUrl("/register"));
        verify(service).registerNewUserAccount(any(UserDto.class));
    }

    @Test
    public void createUser_ShouldRedirectToRegisterWhenEmailAlreadyExistsExceptionIsThrown() throws Exception {


        RequestBuilder request = post("/register")
                .param("username", "username12")
                .param("password", "password12")
                .param("email", "example@gmail.com")
                .with(csrf());

        //Can't use when method since service.registerNewUserAccount return type is void
        doThrow(new EmailAlreadyExistsException()).when(service).registerNewUserAccount(any(UserDto.class));

        mockMvc.perform(request).andExpect(redirectedUrl("/register"));
        verify(service).registerNewUserAccount(any(UserDto.class));
    }

    @Test
    public void createUser_ShouldIncludeNewUserDtoAndBindingResultInModelWhenValidationErrors() throws Exception {
        RequestBuilder request = post("/register")
                .param("username", "@#$")
                .param("password", "][][][];';',.,.,.<>")
                .param("email", "dja;sfadsf")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(view().name("register"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", instanceOf(UserDto.class)))
                .andExpect(model().attribute("org.springframework.validation.BindingResult.user", instanceOf(BindingResult.class)));

    }
    //todo: test for individual validation error checks
}