package com.middleware.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.middleware.model.SMTPServer;
import com.middleware.model.User;
import com.middleware.services.RoleService;
import com.middleware.services.SMTPServerService;
import com.middleware.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/rest/v1/api")
public class RestAPI {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SMTPServerService smtpServerService;

    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String cselfresetpassword(HttpSession session, @RequestParam(required = false, value = "email") String email) {
        String results = "";
        User activateuser = userService.findUserByEmail(email);
        if (activateuser != null) {
            String newpass = "karp@2018";
            String uid = Integer.toString(activateuser.getId());
            Date nowDate = new Date();

                activateuser.setPassword(bCryptPasswordEncoder.encode("karp@2018"));
                System.out.println(activateuser.getFull_name() + " ID ya user ndo hii");
            userService.updateUser(activateuser);
            SMTPServer smtpServer = smtpServerService.getBySectionID("1");
            JsonObject json = new JsonObject();
            if (smtpServer != null) {
                String to = activateuser.getEmail();
                String subject = "KARP Reporting Server Password Reset Successful";
                String content = "Dear " + activateuser.getFull_name() + " Your Password has been reset to " + newpass + ".";
                ApplicationMailer.sendMail(to, subject, content, smtpServer);
            }
            // Gson gson = new Gson();
            // String jsons = gson.toJson(activateuser);
            json.addProperty("success", "1");
            json.addProperty("data", "[{New password send to your email}]");
            results = json.toString();


        } else {
            JsonObject json = new JsonObject();
            Gson gson = new Gson();
            String jsons = gson.toJson(activateuser);
            json.addProperty("success", "0");
            json.addProperty("data", "[{Failed Wrong email}]");
            results = json.toString();

        }
        return results;

    }
}
