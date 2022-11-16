package com.middleware.controller;

import com.middleware.model.SMTPServer;
import com.middleware.model.User;
import com.middleware.model.Role;
import com.middleware.services.SMTPServerService;
import com.middleware.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Controller
@Transactional
@RequestMapping("/auth")
@Scope("session")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SMTPServerService smtpServerService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth");
        return modelAndView;
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ModelAndView processlogin(@RequestParam(required=false, value="email") String username,
                                     @RequestParam(required=false, value="password") String password,
                                     HttpSession session,
                                     HttpServletRequest request
                                        ) {

            ModelAndView modelAndView = new ModelAndView();

           User usernameExist = userService.findUserByEmailORUsername(username,username);
         System.out.println("Imeingia hapa kabisa");
           if (usernameExist != null) {
                System.out.println( "Entered password "+bCryptPasswordEncoder.encode(password));
                System.out.println( "Retrived password "+usernameExist.getPassword());

                boolean authcheck = bCryptPasswordEncoder.matches(password, usernameExist.getPassword());
                if (authcheck == true) {
                    session.setAttribute("user",usernameExist);

                    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
                    for (Role role : usernameExist.getRoles()) {
                        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
                    }

                    UsernamePasswordAuthenticationToken authReq =
                            new UsernamePasswordAuthenticationToken(usernameExist.getUsername(),password,grantedAuthorities);

                    System.out.println("Imeingia hapa");

                    return new ModelAndView("redirect:/reporting/home");

                } else {
                    //modelAndView.addObject("encodepass", authcheck);
                   // modelAndView.addObject("userName", "Welcome check one" + usernameExist.getFull_name() + ' ' + password);
                }
            }
            else {
                System.out.println("Hakuna users");
                modelAndView.addObject("smg", "d1");
                modelAndView.addObject("userName", "Wrong Username or Password");
            }

            modelAndView.setViewName("auth");
            return modelAndView;

    }

    @GetMapping(value="/resetpassword/{email:.+}}")
    @ResponseBody
    public String selfresetpassword(
            @PathVariable String email){
        User activateuser = userService.findUserByEmail(email);
        if (activateuser !=null){
            String newpass = "";
            String uid = Integer.toString(activateuser.getId());
            newpass = bCryptPasswordEncoder.encode("karp@2018");
            activateuser.setPassword(newpass);
            userService.updateUser(activateuser);
            SMTPServer smtpServer = smtpServerService.getBySectionID("1");
            if(smtpServer !=null) {
                String to = activateuser.getEmail();
                String subject = "Reporting Server Password Reset Successful";
                String content = "Dear "+ activateuser.getFull_name() + " Your Password has been reset to "+ newpass +".";
                ApplicationMailer.sendMail(to, subject, content, smtpServer);
            }
          return  "s1";

        }

        return  "s1";

    }

    @GetMapping(value="/deactivate/{email}")
    @ResponseBody
    public String deactivate(
            HttpSession session,
            @PathVariable String email){
        User activateuser = userService.findUserByEmail(email);
        if (activateuser !=null){
            String newpass = "";
            String uid = Integer.toString(activateuser.getId());
            newpass = bCryptPasswordEncoder.encode("karp@2018");
            //activateuser.setPassword(newpass);
            activateuser.setStatus(0);
            userService.updateUser(activateuser);
            SMTPServer smtpServer = smtpServerService.getBySectionID("1");
            if(smtpServer !=null) {
                String to = activateuser.getEmail();
                String subject = "Reporting Server Deactivation";
                String content = "Dear "+ activateuser.getFull_name() + " Your account has been deactivated.";
                ApplicationMailer.sendMail(to, subject, content, smtpServer);
            }
            session.setAttribute("smg","s1");
            return  "s1";

        }
        return  "s1";


    }
    @GetMapping(value="/activate/{email}")
    @ResponseBody
    public String activate(
            HttpSession session,
            @PathVariable String email){
        User activateuser = userService.findUserByEmail(email);
        if (activateuser !=null){
            String newpass = "";
            String uid = Integer.toString(activateuser.getId());
            newpass = bCryptPasswordEncoder.encode("karp@2018");
            //activateuser.setPassword(newpass);
            activateuser.setStatus(1);
            userService.updateUser(activateuser);
            SMTPServer smtpServer = smtpServerService.getBySectionID("1");
            if(smtpServer !=null) {
                String to = activateuser.getEmail();
                String subject = "Reporting Server Activation";
                String content = "Dear "+ activateuser.getFull_name() + " Your account has been activated.";
                ApplicationMailer.sendMail(to, subject, content, smtpServer);
            }
            session.setAttribute("smg","s1");
            return  "s1";

        }
        return  "s1";

    }

}


