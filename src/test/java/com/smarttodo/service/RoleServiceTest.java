package com.smarttodo.service;

import com.smarttodo.dao.RoleDao;
import com.smarttodo.model.Role;
import com.smarttodo.model.Task;
import com.smarttodo.service.exceptions.RoleNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by kpfromer on 4/10/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private RoleDao dao;

    @InjectMocks
    private RoleService service = new RoleServiceImpl();

    @Test
    public void findByName_ShouldReturnRole() throws Exception {

        when(dao.findByName("ROLE_USER")).thenReturn(new Role());

        assertThat(service.findByName("ROLE_USER"), instanceOf(Role.class));

        verify(dao).findByName("ROLE_USER");
    }

    @Test(expected = RoleNotFoundException.class)
    public void findByName_ShouldThrowRoleNotFoundException() throws Exception {

        when(dao.findByName("ROLE_USER")).thenReturn(null);

        service.findByName("ROLE_USER");

        verify(dao).findByName("ROLE_USER");
    }
}