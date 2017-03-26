package com.smarttodo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kpfromer on 3/25/17.
 */
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping("/")
    public String home(){
        return "Hello!";
    }
}
