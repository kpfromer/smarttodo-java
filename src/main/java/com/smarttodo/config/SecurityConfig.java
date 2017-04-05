package com.smarttodo.config;

import com.smarttodo.service.UserService;
import com.smarttodo.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.data.repository.query.spi.Function;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Map;

/**
 * Created by kpfromer on 3/24/17.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //todo: add a role dao
    //todo: add a role service


    @Autowired
    private UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
        //todo: add password encryption
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");//Ignore the security checks for all the static assets
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //todo: add correct user auth chain
        //todo: add CSRF Protection
        http
                .authorizeRequests()
                    .antMatchers("/test").permitAll()
                    .anyRequest().hasRole("USER")
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .permitAll()// allow anyone on this page
                        .successHandler(loginSuccessHandler())
                        .failureHandler(loginFailureHandler())
                .and()//Logout : https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-logout
                    .logout()//logout url default is /logout; also the request type to logout must be a post request!
                        .permitAll()// allow anyone on this page
                        .logoutSuccessUrl("/login")//you can use a handler if you want
                .and()
                    .csrf();//adds CSRF Protection for post requests
    }

    public AuthenticationSuccessHandler loginSuccessHandler(){
        /*
        * This current setup redirects the user to the home page
        * but if you wanted you can add curly brackets and have functionality to redirect to other places (maybe a project page)
        * or even better create a class that implements AuthenticationSuccessHandler with addition functionality
        * */

        return (request, response, authentication) -> response.sendRedirect("/");
    }

    public AuthenticationFailureHandler loginFailureHandler(){
        /*
        * Look at the layout.html and login.html for info about how the custom flash message works to display info
        * Look at the LoginController to see how to remove the flash message after being used
        * */
        return (request, response, exception) -> {
            request.getSession().setAttribute("flash", new FlashMessage("Incorrect username and/or password. Please try again.", FlashMessage.Status.FAILURE));
            response.sendRedirect("/login");
        };
    }

    @Bean
    public EvaluationContextExtension securityExtension(){
        return new EvaluationContextExtensionSupport() {
            @Override
            public String getExtensionId() {
                return "security";
            }

            @Override
            public Object getRootObject() {
                /*
                * This will get the security expression root object, exposing all the details
                * */
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return new SecurityExpressionRoot(authentication) {};
            }
        };
    }

}
