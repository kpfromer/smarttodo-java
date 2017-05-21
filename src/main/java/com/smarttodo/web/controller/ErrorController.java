package com.smarttodo.web.controller;

import com.smarttodo.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kpfromer on 5/20/17.
 */
@Controller
public class ErrorController extends AbstractErrorController {

    private static final String ERROR_PATH=  "/error";

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(ERROR_PATH)
    public String handleErrors(HttpServletRequest request, Model model) {
        HttpStatus status = getStatus(request);
        if (status.equals(HttpStatus.NOT_FOUND)) {
            model.addAttribute("status", "404");
            model.addAttribute("reason", "SORRY BUT WE COULD NOT FIND THIS PAGE!");
        } else {
            model.addAttribute("status", status.value());
            model.addAttribute("reason", status.getReasonPhrase());
        }
        //todo: put into property file
        return "error/error";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}