package com.smarttodo.web.controller;

import com.smarttodo.model.Task;
import com.smarttodo.service.TaskService;
import org.apache.poi.ss.formula.functions.T;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
//builds requests, acts like a fake user/headless browser
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

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
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
                .param("complete", "on");//note if I want the complete to be false the value parameter of the param method would be ""


        mockMvc.perform(request).andExpect(redirectedUrl("/"));
        verify(service).save(any(Task.class));
    }

    //todo: add test to test if addTask with invalid data is handled
}