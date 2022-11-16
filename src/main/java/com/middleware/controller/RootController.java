package com.middleware.controller;


import com.middleware.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@Scope("session")
public class RootController {

    @Autowired
    private UserService userService;
    private String checkstring;

    /*@RequestMapping(value = {"/", "/setup"}, method = RequestMethod.GET)
    public ModelAndView setup(HttpSession sessionsetup) throws InterruptedException, IOException {
        ModelAndView modelAndView = new ModelAndView();
        return new ModelAndView("redirect:/auth/login");
    }*/
    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView loginn(HttpSession session ){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth");
        return modelAndView;

    }


}
