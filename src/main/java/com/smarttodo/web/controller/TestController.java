package com.smarttodo.web.controller;

import com.smarttodo.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kpfromer on 3/25/17.
 */

//Note this class will be removed; it is used just for testing purposes
@Controller
public class TestController {

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("tasks", new ArrayList<>(Arrays.asList(
                new Task(1L, "Spring", false),
                new Task(2L, "Java", true)
        )));
        model.addAttribute("newTask", new Task());
        return "todo";
    }

}
