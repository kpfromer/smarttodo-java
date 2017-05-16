package com.smarttodo.service;

import com.smarttodo.dao.TaskDao;
import com.smarttodo.model.Event;
import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import com.smarttodo.service.exceptions.*;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
//todo: replace junit assertions with hamcrest assertions
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Created by kpfro on 4/2/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    @Mock
    private TaskDao dao;

    @InjectMocks
    private TaskService service = new TaskServiceImpl();


    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        List<Task> taskList = Arrays.asList(
                new Task(),
                new Task()
        );
        when(dao.findAll()).thenReturn(taskList);

        assertEquals("findAll should return two Tasks", 2, Lists.newArrayList(service.findAll()).size());

        verify(dao).findAll();
    }

    @Test
    public void findById_ShouldReturnTask() throws Exception {
        Task task = new Task.TaskBuilder()
                .withEvent(new Event())
                .build();

        when(dao.findOne(1L)).thenReturn(task);
        assertThat(service.findById(1L), instanceOf(Task.class));
        verify(dao).findOne(1L);
    }

    @Test(expected = EventNullException.class)
    public void findById_ShouldThrowEventNullException() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Task());
        assertThat(service.findById(1L), instanceOf(Task.class));
        verify(dao).findOne(1L);
    }

    @Test(expected = TaskNotFoundException.class)
    public void findById_ShouldThrowTaskNotFoundException() throws Exception {
        when(dao.findOne(1L)).thenReturn(null);
        service.findById(1L);
        verify(dao).findOne(1L);
    }

    @Test
    public void save_ShouldSaveTask() throws Exception {
        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(true)
                .withDescription("Hello World")
                .withUser(new User(1L))
                .build();

        service.saveOrUpdate(task);
        try {
            verify(dao).save(task);
        } catch (MockitoAssertionError e) {
            throw new MockitoAssertionError("was expecting a call to dao.saveForCurrentUser with a valid Task object");
        }
    }

    @Test
    public void save_ShouldCreateTaskWithEmptyEventWhenEventIsNull() throws Exception {

        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(false)
                .withUser(new User(1L))
                .withDescription("Hello World")
                .withEvent(null)
                .build();

        service.saveOrUpdate(task);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(dao).save(captor.capture());

        Event serviceEvent = captor.getValue().getEvent();

        assertThat(serviceEvent, notNullValue(Event.class));

    }

    @Test
    public void save_ShouldSaveTaskWithNullId() throws Exception {
        Task task = new Task.TaskBuilder()
                .withComplete(true)
                .withDescription("Hello World")
                .withUser(new User(1L))
                .build();

        service.saveOrUpdate(task);
        try {
            verify(dao).save(task);
        } catch (MockitoAssertionError e) {
            throw new MockitoAssertionError("was expecting a call to dao.saveForCurrentUser with a null Id");
        }
    }

    @Test(expected = UserNotFoundException.class)
    public void save_ShouldThrowUserNotFoundException() throws Exception {
        Task task = new Task.TaskBuilder()
                .withComplete(true)
                .withDescription("Hello World")
                .build();

        service.saveOrUpdate(task);
        verify(dao, never()).save(any(Task.class));
    }

    @Test(expected = DescriptionNullException.class)
    public void save_ShouldThrowDescriptionNullException() throws Exception {

        Task taskNullDescription = new Task.TaskBuilder()
                .withComplete(true)
                .withDescription(null)
                .withUser(new User(1L))
                .build();

        Task taskEmptyDescription = new Task.TaskBuilder()
                .withComplete(true)
                .withDescription("")
                .withUser(new User(1L))
                .build();

        service.saveOrUpdate(taskNullDescription);
        service.saveOrUpdate(taskEmptyDescription);
        verify(dao, never()).save(any(Task.class));
    }

    @Test
    public void toggleComplete_ShouldToggleComplete() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Task.TaskBuilder()
                .withComplete(true)
                .withEvent(new Event())
                .withUser(new User(1L))
                .withDescription("hello world")
                .build());
        service.toggleComplete(1L);
        assertEquals("toggleComplete should toggle the value of complete", false, dao.findOne(1L).isComplete());
        verify(dao).save(any(Task.class));
    }

    //todo: add test about multiple day complete

    @Test(expected = EventNullException.class)
    public void toggleComplete_ShouldThrowEventNullException() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Task.TaskBuilder()
                .withComplete(true).build());
        service.toggleComplete(1L);
        verify(dao).save(any(Task.class));
    }

    @Test(expected = TaskNotFoundException.class)
    public void toggleComplete_ShouldThrowTaskNotFoundException() throws Exception {
        when(dao.findOne(1L)).thenReturn(null);
        service.toggleComplete(1L);
        verify(dao.findOne(1L));
    }

}