package com.smarttodo.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.smarttodo.model.Event;
import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Created by kpfro on 4/2/2017.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseSetup("classpath:tasks.xml")//allows for getting the test data (Database entries)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@ActiveProfiles("dev")
public class TaskDaoTest {

    @Autowired
    private TaskDao dao;


    @Before
    public void setUp() throws Exception {
        User user = new User(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    public void findAll_ShouldReturnTwo() throws Exception {
        assertThat(dao.findAll(), hasSize(2));
    }
}