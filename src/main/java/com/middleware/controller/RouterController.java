package com.middleware.controller;

import com.middleware.model.User;
import com.middleware.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Transactional
@Controller
@RequestMapping("/router")
public class RouterController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public ModelAndView process(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        String views = "";
        if ((session.getAttribute("user").toString() == null))
        {
             return new ModelAndView("redirect:/auth/login");
        }
        else
            {
                session.setAttribute("user", user);
                views = "redirect:/dashboard/view";

            }
        return new ModelAndView(views);
    }
}
