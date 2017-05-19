package com.smarttodo.web.controller;

import com.smarttodo.dto.EditedTextAndEvent;
import com.smarttodo.dto.TaskDto;
import com.smarttodo.model.Event;
import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import com.smarttodo.service.TaskService;
import com.smarttodo.service.UserService;
import com.smarttodo.service.exceptions.DescriptionNullException;
import com.smarttodo.service.exceptions.TaskAlreadyExistsException;
import com.smarttodo.service.exceptions.TaskNotFoundException;
import com.smarttodo.service.exceptions.UserNotFoundException;
import com.smarttodo.web.FlashMessage;
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
        //todo: fix order of items changing!
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("newTask", new TaskDto());
        return "todo";
    }


    @RequestMapping(path = "/mark", method = RequestMethod.POST)
    public String toggleComplete(@RequestParam Long id) {
        taskService.toggleComplete(id);
        return "redirect:/";
    }

    //todo: add test
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateTask(@Valid @ModelAttribute TaskDto taskDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors() || taskDto.getId() == null){
            return "redirect:/";
        }

        Task task = taskService.findById(taskDto.getId());

        EditedTextAndEvent textAndEvent = taskDto.getTextAndEvent();

        task.setComplete(taskDto.isComplete());
        task.setDescription(textAndEvent.getEditedText());
        task.setEvent(textAndEvent.getEvent());

        //todo: catch null description
        taskService.saveOrUpdate(task);
        return "redirect:/";
    }

    @RequestMapping(path = "/tasks", method = RequestMethod.POST)
    public String addTask(@Valid @ModelAttribute TaskDto taskDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }

        EditedTextAndEvent textAndEvent = taskDto.getTextAndEvent();

        Task task = new Task.TaskBuilder()
                .withDescription(textAndEvent.getEditedText())
                .withEvent(textAndEvent.getEvent())
                .withComplete(false)
                .build();

        User user;
        try {
            user = userService.findByUsername(principal.getName());
        } catch (NullPointerException | UserNotFoundException ignored){
            return "redirect:/login";
        }

        task.setUser(user);
        //todo: catch null description
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
