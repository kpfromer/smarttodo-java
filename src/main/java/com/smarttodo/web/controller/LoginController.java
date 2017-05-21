package com.smarttodo.web.controller;

import com.smarttodo.dto.UserDto;
import com.smarttodo.dto.email.OnRegistrationCompleteEvent;
import com.smarttodo.model.User;
import com.smarttodo.model.VerificationToken;
import com.smarttodo.service.RoleService;
import com.smarttodo.service.TokenService;
import com.smarttodo.service.UserService;
import com.smarttodo.service.exceptions.*;
import com.smarttodo.web.FlashMessage;
import com.smarttodo.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kpfromer on 4/4/17.
 */

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Model model, HttpServletRequest request) {

        model.addAttribute("user", new User());

        try {
            Object flash = request.getSession().getAttribute("flash");
            if (flash != null) {

                model.addAttribute("flash", flash);

                request.getSession().removeAttribute("flash");
            }
        } catch (Exception ex) {
        }
        return "login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String registerForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String createUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", new UserDto());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            return "register";
        }

        try {
            userService.registerNewUserAccount(userDto);
        } catch (UsernameAlreadyExistsException ex) {
            redirectAttributes.addFlashAttribute("user", new UserDto());
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(ex.getMessage(), FlashMessage.Status.FAILURE));
            return "redirect:/register";
        } catch (EmailAlreadyExistsException ex) {
            redirectAttributes.addFlashAttribute("user", new UserDto());
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(ex.getMessage(), FlashMessage.Status.FAILURE));
            return "redirect:/register";
        }

        User registered = userService.findByUsername(userDto.getUsername());

        if (registered == null) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Register Error!", FlashMessage.Status.FAILURE));
            return "redirect:/register";
        }

        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale()));
        } catch (Exception ex){
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Token Event Error!", FlashMessage.Status.FAILURE));
            return "redirect:/register";
        }

        return "redirect:/login";
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(@RequestParam("token") String token, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        VerificationToken verificationToken;

        try {
            verificationToken = tokenService.getVerificationToken(token);
        } catch (VerificationTokenNotFoundException ex){
            throw new ResourceNotFoundException();
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            //todo: add new page
            String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
            redirectAttributes.addFlashAttribute("flash", new FlashMessage(String.format("Token has expired. Please go to the following link: %s/resendRegistrationToken?token=%s", baseUrl, token), FlashMessage.Status.INFO));

            return "redirect:/login";
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);
        userService.updateRegisteredUser(user);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("You have successfully registered your account!", FlashMessage.Status.SUCCESS));
        return "redirect:/login";
    }

    @RequestMapping(value = "/resendRegistrationToken", method = RequestMethod.GET)
    public String resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String existingToken, Model model) {

        VerificationToken newToken;
        try {
            newToken = tokenService.generateNewVerificationToken(existingToken);
        } catch (VerificationTokenNotFoundException ex){
            throw new ResourceNotFoundException();
        }

        User alreadyRegisteredUser = newToken.getUser();

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(alreadyRegisteredUser, request.getLocale()));

        return "redirect:/login";
    }

}
