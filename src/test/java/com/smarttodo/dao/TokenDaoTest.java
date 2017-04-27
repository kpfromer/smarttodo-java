package com.smarttodo.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.smarttodo.model.VerificationToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import static org.hamcrest.Matchers.notNullValue;

import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Created by kpfromer on 4/26/17.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseSetup("classpath:tasks.xml")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class TokenDaoTest {

    @Autowired
    private TokenDao dao;

    @Test
    public void findByToken_ShouldRetrieveVerificationToken() throws Exception {
        assertThat(dao.findByToken("24c8e463-e786-408b-95e4-e36bd29d2439"), notNullValue(VerificationToken.class));
    }
}