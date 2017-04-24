package com.smarttodo.service;

import com.smarttodo.dao.TaskDao;
import com.smarttodo.model.Event;
import com.smarttodo.model.Task;
import com.smarttodo.service.exceptions.EventNullException;
import com.smarttodo.service.exceptions.TaskAlreadyExistsException;
import com.smarttodo.service.exceptions.TaskNotFoundException;
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
//builds requests, acts like a fake user/headless browser
//used to get results


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
                .build();

        service.save(task);
        try {
            verify(dao).saveForCurrentUser(task);
        } catch (MockitoAssertionError e) {
            throw new MockitoAssertionError("was expecting a call to dao.saveForCurrentUser with a valid Task object");
        }
    }

    @Test
    public void save_ShouldCreateTaskThatHasCorrectEventDayOfWeek() throws Exception {
        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(false)
                .withDescription("Hello World next tue")
                .build();

        service.save(task);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(dao).saveForCurrentUser(captor.capture());

        Event serviceEvent = captor.getValue().getEvent();

        assertEquals(serviceEvent.getCurrentSetDate().getDayOfWeek(), DayOfWeek.TUESDAY);

    }

    @Test
    public void save_ShouldCreateTaskThatReoccursWithNoEndDate() throws Exception {

        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(false)
                .withDescription("Hello World every monday")
                .build();

        service.save(task);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(dao).saveForCurrentUser(captor.capture());

        Event serviceEvent = captor.getValue().getEvent();

        assertEquals(serviceEvent.isCompleted(), false);
        assertEquals(serviceEvent.isRecurring(), true);
        assertEquals(serviceEvent.getEndDate(), null);
        assertThat(serviceEvent.getStartDate(), notNullValue(LocalDate.class));
        assertThat(serviceEvent.getCurrentSetDate(), notNullValue(LocalDate.class));

    }

    @Test
    public void save_ShouldCreateTaskThatReoccursWithEndDate() throws Exception {

        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(false)
                .withDescription("Hello World every monday until 12/6/2500")
                .build();

        service.save(task);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(dao).saveForCurrentUser(captor.capture());

        Event serviceEvent = captor.getValue().getEvent();

        assertEquals(serviceEvent.isCompleted(), false);
        assertEquals(serviceEvent.isRecurring(), true);
        assertEquals(serviceEvent.getEndDate(), LocalDate.of(2500, 12, 6));
        assertThat(serviceEvent.getStartDate(), notNullValue(LocalDate.class));
        assertThat(serviceEvent.getCurrentSetDate(), notNullValue(LocalDate.class));

    }

    @Test
    public void save_ShouldCreateTaskThatDoesNotReoccur() throws Exception {

        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(false)
                .withDescription("Hello World next sunday")
                .build();

        service.save(task);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(dao).saveForCurrentUser(captor.capture());

        Event serviceEvent = captor.getValue().getEvent();

        assertEquals(serviceEvent.isCompleted(), false);
        assertEquals(serviceEvent.isRecurring(), false);
        assertEquals(serviceEvent.getEndDate(), null);
        assertEquals(serviceEvent.getStartDate(), null);
        assertThat(serviceEvent.getCurrentSetDate(), notNullValue(LocalDate.class));


    }

    @Test
    public void save_ShouldCreateTaskWithEmptyEventWhenEventIsNull() throws Exception {

        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(false)
                .withDescription("Hello World")
                .withEvent(null)
                .build();

        service.save(task);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);

        verify(dao).saveForCurrentUser(captor.capture());

        Event serviceEvent = captor.getValue().getEvent();

        assertThat(serviceEvent, notNullValue(Event.class));

    }

    @Test
    public void save_ShouldSaveTaskWithNullId() throws Exception {
        Task task = new Task.TaskBuilder()
                .withComplete(true)
                .withDescription("Hello World")
                .build();
        when(dao.exists(null)).thenThrow(new IllegalArgumentException("Id can't be null"));
        service.save(task);
        try {
            verify(dao).saveForCurrentUser(task);
        } catch (MockitoAssertionError e) {
            throw new MockitoAssertionError("was expecting a call to dao.saveForCurrentUser with a null Id");
        }
    }

    @Test(expected = TaskAlreadyExistsException.class)
    public void save_ShouldThrowTaskAlreadyExistsException() throws Exception {
        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(true)
                .withDescription("Hello World")
                .build();
        when(dao.exists(1L)).thenReturn(true);
        service.save(task);
        verify(dao).exists(1L);
    }

    @Test
    public void update_ShouldUpdateTask() throws Exception {
        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(true)
                .withDescription("Hello World")
                .build();

        when(dao.exists(1L)).thenReturn(true);
        service.update(task);
        verify(dao).exists(1L);
        verify(dao).updateForCurrentUser(task);
    }

    //todo: test event update

    @Test(expected = TaskNotFoundException.class)
    public void update_ShouldThrowTaskNotFoundException() throws Exception {
        Task task = new Task.TaskBuilder()
                .withId(1L)
                .withComplete(true)
                .withDescription("Hello World")
                .build();

        when(dao.exists(1L)).thenReturn(false);
        service.update(task);
        verify(dao).exists(1L);
    }

    @Test(expected = TaskNotFoundException.class)
    public void updateShouldThrowTaskNotFoundExceptionWithNullId() throws Exception {
        Task task = new Task.TaskBuilder()
                .withId(null)
                .withComplete(true)
                .withDescription("Hello World")
                .build();

        when(dao.exists(1L)).thenReturn(false);
        service.update(task);
        verify(dao).exists(1L);
    }

    @Test
    public void toggleComplete_ShouldToggleComplete() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Task.TaskBuilder()
                .withComplete(true)
                .withEvent(new Event())
                .build());
        service.toggleComplete(1L);
        assertEquals("toggleComplete should toggle the value of complete", false, dao.findOne(1L).isComplete());
        verify(dao).updateForCurrentUser(any(Task.class));
    }

    //todo: add test about multiple day complete

    @Test(expected = EventNullException.class)
    public void toggleComplete_ShouldThrowEventNullException() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Task.TaskBuilder()
                .withComplete(true).build());
        service.toggleComplete(1L);
        verify(dao).updateForCurrentUser(any(Task.class));
    }

    @Test(expected = TaskNotFoundException.class)
    public void toggleComplete_ShouldThrowTaskNotFoundException() throws Exception {
        when(dao.findOne(1L)).thenReturn(null);
        service.toggleComplete(1L);
        verify(dao.findOne(1L));
    }

}