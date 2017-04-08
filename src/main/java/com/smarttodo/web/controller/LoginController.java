package com.smarttodo.web.controller;

import com.smarttodo.model.User;
import com.smarttodo.service.RoleService;
import com.smarttodo.service.UserService;
import com.smarttodo.service.exceptions.RoleNotFoundException;
import com.smarttodo.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by kpfromer on 4/4/17.
 */

@Controller
public class LoginController {

    //todo: create test

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {

        model.addAttribute("user", new User());

        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {}
        return "login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String registerForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    //todo: for test make sure password is encrypted!
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String createUser(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        //todo: add validation
        //todo: add autologin
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("user", new User());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            return "register";
        }

        if(userService.findByUsername(user.getUsername()) != null){
            redirectAttributes.addAttribute("user", new User());
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("User already exists.", FlashMessage.Status.FAILURE));
            return "redirect:/register";
        }
        userService.save(user);
        return "redirect:/login";
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public String roleNotFound(Model model, Exception ex){
        model.addAttribute("ex", ex);
        return "error";
    }
}
