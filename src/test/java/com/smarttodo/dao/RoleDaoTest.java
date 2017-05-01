package com.smarttodo.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.smarttodo.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

//import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by kpfromer on 4/10/17.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseSetup("classpath:tasks.xml")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@ActiveProfiles("dev")
public class RoleDaoTest {

    @Autowired
    private RoleDao dao;

    @Test
    public void findByName_ShouldReturnRole() throws Exception {
        Role actual = dao.findByName("ROLE_USER");
        assertThat(actual.getId(), equalTo(1L));
        assertThat(actual.getName(), equalTo("ROLE_USER"));
    }
}