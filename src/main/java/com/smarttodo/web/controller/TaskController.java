package com.smarttodo.web.controller;

import com.smarttodo.model.Task;
import com.smarttodo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by kpfromer on 3/25/17.
 */

//todo: add @PreAuthorize("ROLE_USER") to methods that need it (since we will have methods that catch errors)
@Controller
public class TaskController {
    //todo:create a test class

    @Autowired
    private TaskService taskService;

    
    @RequestMapping({"/", "/todo"})
    public String taskList(Model model){
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
    public String addTask(@ModelAttribute Task task) {
        //todo: add valid functionality
        taskService.save(task);
        return "redirect:/";
    }
    //todo: catch errors

}
