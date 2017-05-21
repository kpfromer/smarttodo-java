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


    /**
     * Just catching the {@linkplain ResourceNotFoundException} exceptions and render
     * the 404.jsp error page.
     */

    @ExceptionHandler(ResourceNotFoundException.class)
    public String notFound() {
        return "notFound";
    }

    /**
     * Responsible for handling all errors and throw especial exceptions
     * for some HTTP status codes. Otherwise, it will return a map that
     * ultimately will be converted to a json error.
     */
    //todo: add test
//    @RequestMapping(ERROR_PATH)
//    public ResponseEntity<?> handleErrors(HttpServletRequest request) {
//        HttpStatus status = getStatus(request);
//
//        if (status.equals(HttpStatus.NOT_FOUND))
//            throw new ResourceNotFoundException();
//
//        return ResponseEntity.status(status).body(getErrorAttributes(request, false));
//    }

    //todo: create test
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