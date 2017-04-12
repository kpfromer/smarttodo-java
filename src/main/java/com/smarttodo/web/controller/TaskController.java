package com.smarttodo.web.controller;

import com.smarttodo.model.Task;
import com.smarttodo.service.TaskService;
import com.smarttodo.service.exceptions.TaskAlreadyExistsException;
import com.smarttodo.service.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by kpfromer on 3/25/17.
 */


@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;


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

    @RequestMapping(path = "/tasks", method = RequestMethod.POST)
    public String addTask(@Valid @ModelAttribute Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }
        taskService.save(task);
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
