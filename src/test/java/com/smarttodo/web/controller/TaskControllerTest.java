package com.smarttodo.web.controller;

import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import com.smarttodo.service.TaskService;
import com.smarttodo.service.UserService;
import com.smarttodo.service.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
//builds requests, acts like a fake user/headless browser
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//used to get results
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by kpfromer on 4/5/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TaskController controller;

    @Mock
    private TaskService service;

    @Mock
    private UserService userService;

    private UsernamePasswordAuthenticationToken principal;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        User user = new User.UserBuilder()
                .withId(1L)
                .withUsername("kylepfromer")
                .withPassword("example")
                .withEmail("example@gmail.com")
                .withEnabled(true)
                .build();
         principal = new UsernamePasswordAuthenticationToken(user, null);
    }

    @Test
    public void index_ShouldIncludeNewTaskAndTasksInModel() throws Exception {
        List<Task> tasks = Arrays.asList(
                new Task.TaskBuilder().withId(1L).withComplete(false).withDescription("test1").build(),
                new Task.TaskBuilder().withId(2L).withComplete(true).withDescription("test2").build()
        );
        when(service.findAll()).thenReturn(tasks);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("todo"))
                .andExpect(model().attribute("tasks", tasks))
                .andExpect(model().attribute("newTask", instanceOf(Task.class)));

        verify(service).findAll();
    }

    @Test
    public void toggleComplete_ShouldRedirectToIndexPage() throws Exception {


        /* Not needed? Works fine without...
        doAnswer(invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            return null;
        }).when(service).toggleComplete(1L);
        */

        mockMvc.perform(
                post("/mark")
                        .param("id", "1")
        ).andExpect(redirectedUrl("/"));
        verify(service).toggleComplete(1L);
    }

    @Test
    public void addTask_ShouldRedirectToIndexPage() throws Exception {

        RequestBuilder request = post("/tasks")
                .param("id", "1")
                .param("description", "Hello. ")
                .principal(principal)
                .param("complete", "on");//note if I want the complete to be false the value parameter of the param method would be ""


        mockMvc.perform(request).andExpect(redirectedUrl("/"));
        verify(service).saveOrUpdate(any(Task.class));
    }

    @Test
    public void addTask_ShouldNotSaveTaskIfDescriptionIsBlank() throws Exception {
        RequestBuilder request = post("/tasks")
                .param("id", "1")
                .param("description", "")//description is blank
                .principal(principal)
                .param("complete", "on");

        mockMvc.perform(request);

        verify(service, never()).saveOrUpdate(any(Task.class));
    }

    @Test
    public void addTask_ShouldNotSaveTaskIfDescriptionIsNull() throws Exception {
        RequestBuilder request = post("/tasks")
                .param("id", "1")//no description!
                .principal(principal)
                .param("complete", "on");

        mockMvc.perform(request);

        verify(service, never()).saveOrUpdate(any(Task.class));
    }

    @Test
    public void addTask_ShouldNotSaveTaskIfUserDoesNotExist() throws Exception {

        RequestBuilder request = post("/tasks")
                .param("id", "1")
                .param("description", "Hello world")
                .param("complete", "on")
                .principal(principal);

        when(userService.findByUsername(anyString())).thenThrow(new UserNotFoundException());
        mockMvc.perform(request).andExpect(redirectedUrl("/login"));
        verify(service, never()).saveOrUpdate(any(Task.class));

    }

}