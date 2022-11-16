package com.middleware.controller;

import com.middleware.model.DatabaseInfo;
import com.middleware.model.User;
import com.middleware.services.DatabaseinfoService;
import com.middleware.services.FacilitiesService;
import com.middleware.services.MonthsService;
import com.middleware.services.YearsService;
import com.middleware.model.Facilities;
import ke.or.karp.middleware.services.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

@Controller
@Transactional
@RequestMapping("/databasemanager")
public class DatabaseController {
    @Autowired
    public FacilitiesService facilitiesService;
    @Autowired
    public YearsService yearsService;
    @Autowired
    public MonthsService monthsService;
    @Value("${app.dbpath}")
    public String dbpath;
    @Value("${spring.datasource.username}")
    public String username;
    @Value("${spring.datasource.password}")
    public String password;
    @Value("${spring.datasource.jdbcUrl}")
    public String dburl;

    @Value("${app.mysqlpath}")
    public String mysqlpath;

    @Autowired
    public DatabaseinfoService databaseinfoService;
    @Autowired
    private ServletContext servletContext;


    @RequestMapping(value = "/restoredb", method = RequestMethod.GET)
    public ModelAndView restoredb(HttpSession session) throws IOException, JSONException {
        System.out.println("Imefika Hapa");
        if (session.getAttribute("user") != null) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("years", yearsService.getAllDataset());
            modelAndView.addObject("months", monthsService.getAllDataset());
            modelAndView.addObject("falicities", facilitiesService.searchByFtypeLike("F"));

            modelAndView.setViewName("restoredb");
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/auth/login");
        }

    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("facility") String facility,
                         @RequestParam("year") String year,
                         @RequestParam("month") String month,
                         HttpSession session
    ) throws IOException, InvalidFormatException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String progres = "";
        User userdetails = (User) session.getAttribute("user");
        //String fileName = file.getOriginalFilename();
        //File newfile = new File(dbpath+"Rugutetest");
        //file.transferTo(newfile);
        long size = file.getSize();
        System.out.println("File size "+ size);

            String nfilename = "openmrsm" + facility + month + year + ".sql";
            String fpath = dbpath + nfilename;
            Date nowDate = new Date();
            try {

                //Unzipping the file


                file.transferTo(new File(dbpath + nfilename));
                //Path path = Paths.get(nfilename);

                //long bytes = Files.size(path);
                long bytes = file.getSize();
                long gbc = (1024*3);
                long gb = bytes/gbc;

                long kilobytes = (bytes / 1024);
                long megabytes = (kilobytes / 1024);
                long gigabytes = (megabytes / 1024);

                DatabaseInfo databaseInfo = new DatabaseInfo();
                Facilities facilities = facilitiesService.getByMFLCODE(Double.parseDouble(facility));
                databaseInfo.setName(facilities.getFacilityname());
                databaseInfo.setMflcode(facility);
                databaseInfo.setReuploaded("No");
                databaseInfo.setCreated_by(userdetails.getId());
                databaseInfo.setUrl(fpath);
                databaseInfo.setStatus("Pending restore");
               // databaseInfo.setDbsize(String.format("%.2f,d GB", gigabytes));
                databaseInfo.setDbsize(String.valueOf(megabytes+" MB"));
                databaseInfo.setDbname(nfilename);
                databaseInfo.setCreated_on(nowDate);
                //databaseInfo.set;
                databaseinfoService.save(databaseInfo);

            } catch (Exception e) {
                return HttpStatus.INTERNAL_SERVER_ERROR.toString();
            }
            //modelMap.addAttribute("filename", file);
            System.out.println("Upload imefika hapa" + userdetails.getFull_name() + " " + facility + " " + year + " " + month);
            return "File uploaded successfully.";

            // return "fileUploadView";

    }

    @RequestMapping(value = "/facilities", method = RequestMethod.GET)
    public ModelAndView facilities(HttpSession session) throws IOException, JSONException {
      //  System.out.println("Imefika Hapa");
        if (session.getAttribute("user") != null) {
            ModelAndView modelAndView = new ModelAndView();
            List<Facilities> facilitiesList = facilitiesService.searchByFtypeLike("F");
            List<Facilities> contiesList = facilitiesService.searchByFtypeLike("C");
            modelAndView.addObject("facilities", facilitiesService.searchByFtypeLike("F"));
            modelAndView.addObject("counties", facilitiesService.searchByFtypeLike("C"));
            modelAndView.addObject("countfacilities", facilitiesList.size());
            modelAndView.addObject("countcounties", contiesList.size());
            modelAndView.setViewName("pm/masterfacilities");
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/auth/login");
        }

    }

    @RequestMapping(value = "/masterdatabase", method = RequestMethod.GET)
    public ModelAndView masterdatabase(HttpSession session) throws IOException, JSONException {
        System.out.println("Imefika Hapa");
        if (session.getAttribute("user") != null) {
            ModelAndView modelAndView = new ModelAndView();
            List<Facilities> facilitiesList = facilitiesService.searchByFtypeLike("F");
            List<Facilities> contiesList = facilitiesService.searchByFtypeLike("C");
            List<DatabaseInfo> databaseInfos = databaseinfoService.getAllDataset();

            modelAndView.addObject("dbs", databaseInfos);
            modelAndView.addObject("facilities", facilitiesService.searchByFtypeLike("F"));
            modelAndView.addObject("counties", facilitiesService.searchByFtypeLike("C"));
            modelAndView.addObject("countfacilities", facilitiesList.size());
            modelAndView.addObject("countcounties", contiesList.size());
            modelAndView.setViewName("masterdatabase");
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/auth/login");
        }

    }
    @RequestMapping(value = "/masterdatabases", method = RequestMethod.GET)
    public ModelAndView masterdatabases(HttpSession session) throws IOException, JSONException {
        System.out.println("Imefika Hapa");
        if (session.getAttribute("user") != null) {
            ModelAndView modelAndView = new ModelAndView();
            List<Facilities> facilitiesList = facilitiesService.searchByFtypeLike("F");
            List<Facilities> contiesList = facilitiesService.searchByFtypeLike("C");
            List<DatabaseInfo> databaseInfos = databaseinfoService.getAllDataset();

            modelAndView.addObject("dbs", databaseInfos);
            modelAndView.addObject("facilities", facilitiesService.searchByFtypeLike("F"));
            modelAndView.addObject("counties", facilitiesService.searchByFtypeLike("C"));
            modelAndView.addObject("countfacilities", facilitiesList.size());
            modelAndView.addObject("countcounties", contiesList.size());
            modelAndView.addObject("years", yearsService.getAllDataset());
            modelAndView.addObject("months", monthsService.getAllDataset());
            modelAndView.addObject("falicities", facilitiesService.searchByFtypeLike("F"));

            modelAndView.setViewName("pm/databases");
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/auth/login");
        }

    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity downloadFileFromLocal(@PathVariable String fileName) {
        String fpath = dbpath;
        // String realpath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "static/HRresources/";
        String realPathtoUploads = dbpath + fileName;

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);

        File file = new File(realPathtoUploads);
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

    @GetMapping("/restore/{fileName:.+}")
    @ResponseBody
    public String restoreFileFromLocal(@PathVariable String fileName) throws IOException, InterruptedException {
        String fpath = dbpath;
        String smg = "";
        String realPathtoUploads = dbpath + fileName;
        String nfilename = fileName.substring(0, fileName.length() - 4);

        try {

            Connection connection = DriverManager.getConnection(dburl, username, password);
            System.out.println("New file name is " + nfilename+" "+ realPathtoUploads);
            String sql = "CREATE DATABASE " + nfilename;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            String dbname = nfilename+".sql";


            new Thread(new Runnable() {
                @Override
                public void run() {
                    DatabaseInfo databaseInfo = databaseinfoService.getByDbname(dbname);
                    databaseInfo.setStatus("Restoring");
                    databaseinfoService.save(databaseInfo);

                    restorebd(nfilename, realPathtoUploads);

                    DatabaseInfo databaseInfo1 = databaseinfoService.getByDbname(dbname);
                    databaseInfo1.setStatus("Restored");
                    databaseinfoService.save(databaseInfo1);
                }
            }).start();


            //  String dbname = nfilename+".sql";



            smg = "Database created successfully";
        } catch (SQLException e) {
            String dbname = nfilename+".sql";
            System.out.println("New file name is " + nfilename+" "+ realPathtoUploads);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DatabaseInfo databaseInfo = databaseinfoService.getByDbname(dbname);
                    databaseInfo.setStatus("Restoring");
                    databaseinfoService.save(databaseInfo);

                    restorebd(nfilename, realPathtoUploads);

                    DatabaseInfo databaseInfo1 = databaseinfoService.getByDbname(dbname);
                    databaseInfo1.setStatus("Restored");
                    databaseInfo1.setReuploaded("Yes");
                    databaseinfoService.save(databaseInfo1);
                }
            }).start();

            smg = e.getMessage()+ "Mysql database already exist";


        }
        return smg;
    }
    public String restorebd(String nfilename, String realPathtoUploads ){
        String smg = "";

       // String[] executeCmd = new String[]{"mysql", "--user=" + username, "--password=" + password, nfilename, "-e", " source " + realPathtoUploads};
        String[] executeCmd = new String[]{mysqlpath, "--user=" + username, "--password=" + password, nfilename,"-e", " source " + realPathtoUploads};

        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("Backup restored successfully");
                smg = "Backup restored successfully";//return true;
            } else {
                smg = "Could not restore the backup";

            }
            return smg;//"Restored Successfully";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return smg;//"Restored Successfully";
    }

    @GetMapping("/drop/{fileName}")
   // @ResponseBody
    public ModelAndView dropdatabase(@PathVariable String fileName) throws IOException, InterruptedException {
        String fpath = dbpath;
        final String smg = "";
       // String realPathtoUploads = dbpath + fileName;
        String nfilename = fileName.substring(0, fileName.length() - 4);
        System.out.println("Droping databases");
         new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Connection connection = DriverManager.getConnection(dburl, username, password);
                        System.out.println("New file name is " + fileName);
                        String sql = "DROP DATABASE " + nfilename;
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(sql);
                        statement.close();
                        DatabaseInfo databaseInfo = databaseinfoService.getByDbname(fileName);
                        databaseinfoService.delete(databaseInfo);
                        // smg = "Database created successfully";
                    } catch (SQLException e) {

                        DatabaseInfo databaseInfo = databaseinfoService.getByDbname(fileName);
                        databaseinfoService.delete(databaseInfo);
                        //  smg = e.getMessage()+ "Mysql database already exist";
                        System.out.println("Error " + fileName +" "+e.getMessage());
                    }
                    // return smg;
                }
            }).start();

        return new ModelAndView("redirect:/databasemanager/masterdatabases");
       // return "Done";
    }
}

