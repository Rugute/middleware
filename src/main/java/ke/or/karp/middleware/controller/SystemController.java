package ke.or.karp.middleware.controller;

import ke.or.karp.middleware.model.ReportingPeriod;
import ke.or.karp.middleware.model.SMTPServer;
import ke.or.karp.middleware.model.User;
import ke.or.karp.middleware.services.DatasetService;
import ke.or.karp.middleware.services.ReportingPeriodService;
import ke.or.karp.middleware.services.SMTPServerService;
import ke.or.karp.middleware.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    public UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SMTPServerService smtpServerService;

    @Autowired
    private ReportingPeriodService reportingPeriodService;

    @RequestMapping(value = "/users")
    //@ResponseBody
    public ModelAndView UserDetails(HttpSession session) {
        if (session.getAttribute("user") != null) {
            User userdetails = (User) session.getAttribute("user");
            Date nowDate = new Date();
            ModelAndView modelAndView = new ModelAndView();
            List<User> userList = userService.getAllUsers();

            modelAndView.addObject("userslist",userList);
            modelAndView.addObject("smg", session.getAttribute("smg"));
           // modelAndView.setViewName("users");
             modelAndView.setViewName("pm/users");
            return modelAndView;
       } else {
            return new ModelAndView("redirect:/auth/login");
        }
    }

    @RequestMapping(value = "/adduser" , method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("fullname") String fullname,
                         @RequestParam("username") String username,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password,
                         HttpSession session){
         if (session.getAttribute("user") != null) {
            // LoginController.createuser(fullname,username,email,password,session);
             //String newpass = "";
                //newpass = bCryptPasswordEncoder.encode(password);
                User userdetails = (User) session.getAttribute("user");
                Date nowDate = new Date();
                ModelAndView modelAndView = new ModelAndView();
                User user = new User();
                user.setEmail(email);
                user.setFull_name(fullname);
                user.setUsername(username);
                user.setPassword(password);
                user.setStatus(1);
                user.setCreated_by(userdetails.getId());
                user.setCreated_on(nowDate);
                userService.saveUser(user);
             SMTPServer smtpServers = smtpServerService.getByTopOne();
            // System.out.print(" Host ndo hii "+host);
             if(smtpServers !=null) {
                 String subject = " Reporting Server User ";
                 String message = "Dear "+ fullname + "\r\n";
                 message += "\r\n";
                 message +="We wish to notify you that your account have been created on the reporting server."+ "\r\n";
                 message += "\r\n";
                 message += "User credential are as follows: \n";
                 message += "Username: "+ username + "\n";
                 message += "Password: "+ password + "\n";
                 message += "Url http://portal.karp.or.ke/middleware \n";
                 message += "\r\n";
                 message += "Regards"+ "\r\n";
                 message += "Dev & Reporting Team"+ "\r\n";
                 ApplicationMailer.sendMail(email, subject, message, smtpServers);
             }

         return "added successfully.";
       } else {
            return "Login again";
        }
    }

    @RequestMapping(value = "/period")
    public ModelAndView Period(HttpSession session) {
        if (session.getAttribute("user") != null) {
            User userdetails = (User) session.getAttribute("user");
            Date nowDate = new Date();
            ModelAndView modelAndView = new ModelAndView();
            List<User> userList = userService.getAllUsers();
            modelAndView.addObject("userslist",userList);
            modelAndView.addObject("period",reportingPeriodService.getAllCurrentAndPast(5));
            modelAndView.setViewName("pm/reportingperiod");
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/auth/login");
        }
    }
}
