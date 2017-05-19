package com.smarttodo.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.smarttodo.model.User;
import org.junit.Before;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.*;


/**
 * Created by kpfromer on 4/7/17.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseSetup("classpath:tasks.xml")//allows for getting the test data (Database entries)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@ActiveProfiles("dev")
public class UserDaoTest {

    @Autowired
    private UserDao dao;


    @Test
    public void findByUsername_ShouldReturnUser() throws Exception {
        assertThat(dao.findByUsername("user1"), notNullValue());
    }

    @Test
    public void userPassword_ShouldBeEncrypted() throws Exception {
        User user = new User();
        user.setPassword("password");

        assertThat(user.getPassword(), notNullValue());
        assertThat(user.getPassword(), !equals("password"));
    }
}