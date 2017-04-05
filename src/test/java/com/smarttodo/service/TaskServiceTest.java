package com.smarttodo.service;

import com.smarttodo.dao.TaskDao;
import com.smarttodo.model.Task;
import com.smarttodo.service.exceptions.TaskNotFoundException;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
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

    /*
    *  when(dao.findById(1L)).thenReturn(new Favorite());
        assertThat(service.findById(1L), instanceOf(Favorite.class));
        verify(dao).findById(1L);
    * */
    
    @Test
    public void findAll_ShouldReturnOne() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Task());
        assertThat(service.findById(1L), instanceOf(Task.class));
        verify(dao).findOne(1L);
    }

    /*
    * @Test(expected = FavoriteNotFoundException.class)
    public void findById_ShouldThrowFavoriteNotFoundException () throws Exception {
        when(dao.findById(1L)).thenReturn(null);
        service.findById(1L);
        verify(dao).findById(1L);

    * */

    @Test(expected = TaskNotFoundException.class)
    public void findById_ShouldThrowTaskNotFoundException() throws Exception {
        when(dao.findOne(1L)).thenReturn(null);
        service.findById(1L);
        verify(dao).findOne(1L);
    }
}