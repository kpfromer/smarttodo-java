package com.smarttodo.web.controller;

import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import com.smarttodo.service.TaskService;
import com.smarttodo.service.UserService;
import com.smarttodo.service.exceptions.TaskAlreadyExistsException;
import com.smarttodo.service.exceptions.TaskNotFoundException;
import com.smarttodo.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by kpfromer on 3/25/17.
 */


@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;


    @RequestMapping({"/", "/todo"})
    public String taskList(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("newTask", new Task());
        return "todo";
    }


    @RequestMapping(path = "/mark", method = RequestMethod.POST)
    public String toggleComplete(@RequestParam Long id) {
        taskService.toggleComplete(id);
        return "redirect:/";
    }

    //todo: add test
    @RequestMapping(path = "/tasks", method = RequestMethod.POST)
    public String addTask(@Valid @ModelAttribute Task task, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }

        User user;
        //todo: deal with userService exception
        try {
            user = userService.findByUsername(principal.getName());
        } catch (NullPointerException | UserNotFoundException ignored){
            return "redirect:/login";
        }

        task.setUser(user);
        taskService.saveOrUpdate(task);
        return "redirect:/";
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public String notFound(Model model, Exception ex) {
        model.addAttribute("ex", ex);
        return "error";
    }

    @ExceptionHandler(TaskAlreadyExistsException.class)
    public String alreadyExists(Model model, Exception ex) {
        model.addAttribute("ex", ex);
        return "error";
    }

    //todo: exception for Database errors
}
