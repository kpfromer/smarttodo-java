package com.smarttodo.dao;

import com.smarttodo.model.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by kpfro on 4/2/2017.
 */
@Repository
public interface TaskDao extends CrudRepository<Task, Long> {

    /*
    This all ways confused me, look at the link and securityExtension in SecurityConfig
    https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions
    */

    @Query("select t from Task t where t.user.id=:#{principal.id}")
    List<Task> findAll();
}
