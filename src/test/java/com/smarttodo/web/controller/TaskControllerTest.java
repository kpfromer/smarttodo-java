package com.smarttodo.web.controller;

import com.smarttodo.dto.TaskDto;
import com.smarttodo.model.Event;
import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import com.smarttodo.service.TaskService;
import com.smarttodo.service.UserService;
import com.smarttodo.service.exceptions.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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
                .andExpect(model().attribute("newTask", instanceOf(TaskDto.class)));

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
                .param("complete", "on")
                .param("eventText", "");//note if I want the complete to be false the value parameter of the param method would be ""


        mockMvc.perform(request).andExpect(redirectedUrl("/"));
        verify(service).saveOrUpdate(any(Task.class));
    }

    @Test
    public void addTask_ShouldNotSaveTaskIfDescriptionIsBlank() throws Exception {
        RequestBuilder request = post("/tasks")
                .param("id", "1")
                .param("description", "")//description is blank
                .principal(principal)
                .param("complete", "on")
                .param("eventText", "");

        mockMvc.perform(request);

        verify(service, never()).saveOrUpdate(any(Task.class));
    }

    @Test
    public void addTask_ShouldNotSaveTaskIfDescriptionIsNull() throws Exception {
        RequestBuilder request = post("/tasks")
                .param("id", "1")//no description!
                .principal(principal)
                .param("complete", "on")
                .param("eventText", "");

        mockMvc.perform(request);

        verify(service, never()).saveOrUpdate(any(Task.class));
    }

    @Test
    public void addTask_ShouldNotSaveTaskIfUserDoesNotExist() throws Exception {

        RequestBuilder request = post("/tasks")
                .param("id", "1")
                .param("description", "Hello world")
                .param("complete", "on")
                .param("eventText", "")
                .principal(principal);

        when(userService.findByUsername(anyString())).thenThrow(new UserNotFoundException());
        mockMvc.perform(request).andExpect(redirectedUrl("/login"));
        verify(service, never()).saveOrUpdate(any(Task.class));

    }

    @Test
    public void addTask_ShouldNotSaveTaskIfEventTextDoesNotExist() throws Exception {

        RequestBuilder request = post("/tasks")
                .param("id", "1")
                .param("description", "Hello world")
                .param("complete", "on")
                .principal(principal);

        mockMvc.perform(request).andExpect(redirectedUrl("/"));
        verify(service, never()).saveOrUpdate(any(Task.class));

    }
    
    @Test
    public void addTask_ShouldCreateTaskThatHasCorrectEventDayOfWeek() throws Exception {

        RequestBuilder requestDateInDescription = post("/tasks")
                .param("id", "1")
                .param("description", "Hello World next tue")
                .principal(principal)
                .param("eventText", "");

        RequestBuilder requestDateInEventText = post("/tasks")
                .param("id", "1")
                .param("description", "Hello World")
                .principal(principal)
                .param("eventText", "next tue");

        RequestBuilder[] requests = {requestDateInDescription, requestDateInEventText};

        for(RequestBuilder request : requests) {

            ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

            mockMvc.perform(request);

            verify(service, atLeastOnce()).saveOrUpdate(captor.capture());

            Event serviceEvent = captor.getValue().getEvent();

            assertEquals(serviceEvent.getCurrentSetDate().getDayOfWeek(), DayOfWeek.TUESDAY);
        }

    }

    @Test
    public void addTask_ShouldCreateTaskThatReoccursWithNoEndDate() throws Exception {

        RequestBuilder requestDateInDescription = post("/tasks")
                .param("id", "1")
                .param("description", "Hello World every monday")
                .principal(principal)
                .param("eventText", "");

        RequestBuilder requestDateInEventText = post("/tasks")
                .param("id", "1")
                .param("description", "Hello World")
                .principal(principal)
                .param("eventText", "every monday");

        RequestBuilder[] requests = {requestDateInDescription, requestDateInEventText};

        for(RequestBuilder request : requests) {

            ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

            mockMvc.perform(request);

            verify(service, atLeastOnce()).saveOrUpdate(captor.capture());

            Event serviceEvent = captor.getValue().getEvent();

            assertEquals(serviceEvent.isCompleted(), false);
            assertEquals(serviceEvent.isRecurring(), true);
            assertEquals(serviceEvent.getEndDate(), null);
            assertThat(serviceEvent.getStartDate(), notNullValue(LocalDate.class));
            assertThat(serviceEvent.getCurrentSetDate(), notNullValue(LocalDate.class));
        }
    }

    @Test
    public void addTask_ShouldCreateTaskThatReoccursWithEndDate() throws Exception {

        RequestBuilder requestDateInDescription = post("/tasks")
                .param("id", "1")
                .param("description", "Hello World every monday until 12/6/2500")
                .principal(principal)
                .param("eventText", "");

        RequestBuilder requestDateInEventText = post("/tasks")
                .param("id", "1")
                .param("description", "Hello World")
                .principal(principal)
                .param("eventText", "every monday until 12/6/2500");

        RequestBuilder[] requests = {requestDateInDescription, requestDateInEventText};

        for(RequestBuilder request : requests) {

            ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

            mockMvc.perform(request);

            verify(service, atLeastOnce()).saveOrUpdate(captor.capture());

            Event serviceEvent = captor.getValue().getEvent();

            assertEquals(serviceEvent.isCompleted(), false);
            assertEquals(serviceEvent.isRecurring(), true);
            assertEquals(serviceEvent.getEndDate(), LocalDate.of(2500, 12, 6));
            assertThat(serviceEvent.getStartDate(), notNullValue(LocalDate.class));
            assertThat(serviceEvent.getCurrentSetDate(), notNullValue(LocalDate.class));
        }
    }

    @Test
    public void addTask_ShouldCreateTaskThatDoesNotReoccur() throws Exception {

        RequestBuilder requestDateInDescription = post("/tasks")
                .param("id", "1")
                .param("description", "Hello World next sunday")
                .principal(principal)
                .param("eventText", "");

        RequestBuilder requestDateInEventText = post("/tasks")
                .param("id", "1")
                .param("description", "Hello World")
                .principal(principal)
                .param("eventText", "next sunday");

        RequestBuilder[] requests = {requestDateInDescription, requestDateInEventText};

        for(RequestBuilder request : requests) {

            ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

            mockMvc.perform(request);

            verify(service, atLeastOnce()).saveOrUpdate(captor.capture());

            Event serviceEvent = captor.getValue().getEvent();

            assertEquals(serviceEvent.isCompleted(), false);
            assertEquals(serviceEvent.isRecurring(), false);
            assertEquals(serviceEvent.getEndDate(), null);
            assertEquals(serviceEvent.getStartDate(), null);
            assertThat(serviceEvent.getCurrentSetDate(), notNullValue(LocalDate.class));
        }
    }

}