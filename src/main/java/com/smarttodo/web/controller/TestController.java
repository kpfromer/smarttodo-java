package com.smarttodo.web.controller;

import com.smarttodo.model.Task;
import com.smarttodo.model.User;
import com.smarttodo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kpfromer on 3/25/17.
 */

//Note this class will be removed; it is used just for testing purposes
@Controller
public class TestController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("newTask", new Task());
        return "todo";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {

        model.addAttribute("user", new User());

        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // "flash" session attribute must not exist...do nothing and proceed normally
        }
        return "login";
    }
}
