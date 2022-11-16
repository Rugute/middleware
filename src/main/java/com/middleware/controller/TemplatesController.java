package com.middleware.controller;

import com.middleware.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

@Controller
@RequestMapping("/downloads")
public class TemplatesController {
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/templates")
    //@ResponseBody
    public ModelAndView EmployeesDetails(HttpSession session) {
       if (session.getAttribute("user") != null) {
            //Company companydetails = (Company) session.getAttribute("companydetails");
            User userdetails = (User) session.getAttribute("user");
            Date nowDate = new Date();
            ModelAndView modelAndView = new ModelAndView();
            String realpath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "static/Resources";
            String realPathtoUploads = realpath.replaceAll("%20", " ");
            System.out.println(realpath);
            File file = new File(realPathtoUploads);
            String[] fileList = file.list();
            for (String name : fileList) {
                System.out.println(name);
                // System.out.println(name);
            }
            modelAndView.addObject("docs", file.list());
            modelAndView.setViewName("pm/templates");
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/auth/login");
        }

    }
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
        String realpath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "static/Resources/";
        String realPathtoUploads = realpath.replaceAll("%20", " ");


        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);

        File file = new File(realPathtoUploads + "/" + fileName);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
}
