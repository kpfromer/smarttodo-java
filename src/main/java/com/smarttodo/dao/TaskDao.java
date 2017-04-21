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

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO task (user_id, description, complete, id, completed, currentSetDate, startDate, endDate, recurring) VALUES (:#{principal.id},:#{#task.description},:#{#task.complete},:#{#task.id}, :#{#task.event.completed}, :#{#task.event.currentSetDate}, :#{#task.event.startDate}, :#{#task.event.endDate}, :#{#task.event.recurring})")
    void saveForCurrentUser(@Param("task") Task task);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE task SET description=:#{#task.description}, complete=:#{#task.complete} WHERE id=:#{#task.id}")
    void updateForCurrentUser(@Param("task") Task task);
}
