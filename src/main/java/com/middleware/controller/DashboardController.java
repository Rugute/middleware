package com.middleware.controller;

import com.middleware.model.*;
import com.middleware.services.*;
import ke.or.karp.middleware.model.*;
import com.middleware.model.Mapping;
import ke.or.karp.middleware.services.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
//import org.glassfish.jersey.client.http.Expect100ContinueFeature


import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@Transactional
@RequestMapping("/dashboard")
public class DashboardController {
    @Value("${app.3pm.url}")
    private String url;
    @Value("${app.3pm.user}")
    private String username;
    @Value("${app.3pm.pass}")
    private String password;
    @Value("${app.3pm.auth}")
    private String auth;
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private DataElementGroupsService dataElementGroupsService;
    @Autowired
    private PeriodService periodService;
    @Autowired
    private MappingService mappingService;
    @Autowired
    private FacilitiesService facilitiesService;
    @RequestMapping(value = "/views", method = RequestMethod.GET)
    public ModelAndView postss(HttpSession session) throws IOException, JSONException {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("url " + url + "pass " + username + "Password " + password + "auth " + auth);
        loadperiod();
        //loadDataset();
        //loadDataElementGroups();
        modelAndView.addObject("period",periodService.getAllDataset());
        modelAndView.addObject("dataelementgroup",dataElementGroupsService.getAllDatasetByStatus("1"));
        modelAndView.addObject("dataset",datasetService.getAllDatasetBystatus("1"));
        modelAndView.setViewName("submitdata");
        return modelAndView;

    }
    @RequestMapping(value = "/upload_data", method = RequestMethod.POST,consumes = { "multipart/form-data" })
    @ResponseBody
    public String upload( @RequestParam("file") MultipartFile file,
                          @RequestParam("period") String period,
                          @RequestParam("report") String report,
                          @RequestParam("dataset") String dataset
    ) throws IOException, InvalidFormatException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String progres="";
        if (file.isEmpty()) {
            progres= "Upload failed, please select file";
        }
        String fileName = file.getOriginalFilename();
        String dir=System.getProperty("user.dir");
        Date dates = new Date();
        String destFileName=dir+"/Data"+File.separator +"uploadedfiles_"+fileName;
        System.out.println(destFileName);
        File destFile = new File(destFileName);
        boolean exists = destFile.exists();
        System.out.println("File exists " + exists);
        if(exists==true){
             destFile.deleteOnExit();//delete();
        }else{
        file.transferTo(destFile);
        }
        System.out.println("Upload succeeded");
        System.out.println("Start reading EXCEL content");
        //uploadlab(destFileName,destFile,dataset);
        if(report.equals("Dv3gWMeVuHM")){
            System.out.println("Asante mungu imefika hapa report hhahahds "+report);
            uploadcovid(destFileName,destFile,dataset);
        } else if(report.equals("ARV&Meds Commodities")){
            uploadARVLab(destFileName,destFile,dataset);
        }
        else if(report.equals("P0lzsnbDM2M")){
            System.out.println("Ndo Hiyo sasa");
            uploadLab(destFileName,destFile,dataset);

        }
           else{

        }
        /*progres="tuko sawa "+ period+" kk "+dataset;
        System.out.println("imefika hapa"+ period+" kk "+dataset+" file"+file);
        System.out.println("Asante mungu imefika hapa");
        System.out.println("Asante mungu imefika hapa report "+report);
       */
        return progres;

    }
    public void uploadLab(String destFileName,File destFile,String dataset) throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Sheet sheet;
        InputStream fis = null;
        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        int nsheets = workbook.getNumberOfSheets();

        for(int s=0;s<nsheets;s++) { //nsheets
            sheet = workbook.getSheetAt(s);
            int totalRowNum = sheet.getLastRowNum();
            Row facilityRow = sheet.getRow(2);
            Row headerRow = sheet.getRow(3);
            int mflcode = (int) facilityRow.getCell(7).getNumericCellValue();
            String month = facilityRow.getCell(10).getStringCellValue();
            int year = (int) facilityRow.getCell(13).getNumericCellValue();
            String nmonth = numericMonths(month);
            String period = year + nmonth;
            Facilities facilities = facilitiesService.getByMFLCODE(mflcode);
            String orgunits = facilities.getOrgUnitUID();
            System.out.println("Workbook " + orgunits + " " + period);
            String jString = "";
            String concatString = "";
            String Finaldatavalues = "";
            String concatString2 = "";
            String oString = "";
            String jStringTwo = "";


            System.out.println("Sheet values are " + sheet.getSheetName() + " Mflcode " + mflcode + " Month " + month + " Year" + year + " period" + period);
            for (int x = 4; x <= 9; x++) { //18
                Row currentRow = sheet.getRow(x);
                String name = currentRow.getCell(0).getStringCellValue();
                System.out.println("row " + x + " Name " + name);
                int numberOfCells = currentRow.getLastCellNum();
                for(int y=1;y<=13;y++) { //13
                    String htext = headerRow.getCell(y).getStringCellValue();
                    int value = (int) currentRow.getCell(y).getNumericCellValue();
                    System.out.println("row data"+y+" "+htext+" value "+value);
                    List<com.middleware.model.Mapping> mapping = mappingService.getBycategorycombospecificIDAndDataelement(htext,name);
                    int g = mapping.size();
                    System.out.println("Header "+htext+" Size "+g+" Name "+name+"Size ");
                    String combo="";
                    String dataele="";
                    if(mapping.size()>0){
                        combo=mapping.get(0).getCategoryOptionComboUID();
                        dataele=mapping.get(0).getDataelementUID();
                    }
                    JSONObject obj2 = new JSONObject();
                    obj2.put("dataElement",dataele);
                    obj2.put("categoryOptionCombo",combo);
                    obj2.put("value",value);
                    jString=obj2.toString();
                    System.out.println(jString);
                    concatString =concatString+jString+",";
                    System.out.println("Final "+ concatString);
                }
                JSONObject obj = new JSONObject();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String DateCompleted= dateFormat.format(date);
                obj.put("dataSet", dataset);
                obj.put("completeDate", DateCompleted);
                obj.put("period", period);
                obj.put("orgUnit", orgunits);
                obj.put("attributeOptionCombo","KOoyurqK0Uf");
                jStringTwo=obj.toString();
                oString = jStringTwo.substring(0, jStringTwo.length() - 1);
                concatString2=concatString.substring(0, concatString.length() - 1);
                Finaldatavalues =oString+", \"dataValues\": [ "+concatString2+" ]}";
                System.out.println(Finaldatavalues);
                pushToAPI(Finaldatavalues);
                Finaldatavalues ="";
                concatString2="";
                concatString="";
                jStringTwo="";

            }
        }
    }
    public void uploadARVLab(String destFileName,File destFile,String dataset) throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Sheet sheet;
        InputStream fis = null;
        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook= new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        int nsheets = workbook.getNumberOfSheets();
        for(int s=0;s<nsheets;s++){ //nsheets
            sheet = workbook.getSheetAt(s);
            int totalRowNum = sheet.getLastRowNum();
            Row facilityRow =sheet.getRow(2);
            Row headerRow =sheet.getRow(3);
            int mflcode = (int) facilityRow.getCell(7).getNumericCellValue();
            String month =  facilityRow.getCell(10).getStringCellValue();
            int year = (int) facilityRow.getCell(12).getNumericCellValue();
            String nmonth = numericMonths(month);
            String period = year+nmonth;
            Facilities facilities = facilitiesService.getByMFLCODE(mflcode);
            String orgunits= facilities.getOrgUnitUID();
            String jString="";
            String concatString="";
            String Finaldatavalues="";
            String concatString2="";
            String oString="";
            String jStringTwo="";


            System.out.println("Sheet values are "+sheet.getSheetName()+" Mflcode "+mflcode+" Month "+month+" Year"+year+" period"+period);
            for(int x =4;x<=18;x++) { //18
                Row currentRow = sheet.getRow(x);
                String name = currentRow.getCell(1).getStringCellValue();
                System.out.println("row "+x+" Name "+name);
                int numberOfCells = currentRow.getLastCellNum();

                for(int y=2;y<13;y++){ //13
                    String htext = headerRow.getCell(y).getStringCellValue();
                    List<com.middleware.model.Mapping> mapping = mappingService.getBycategorycombospecificIDAndDataelement(htext,name);
                   // List<Mapping> mapping = mappingService.getBycategorycombospecificID(htext);
                    int g = mapping.size();
                    //List<Mapping> mappings =mappingService.getByID(name);
                   System.out.println("Header "+htext+" Size "+g+" Name "+name+"Size ");
                    int value = (int) currentRow.getCell(y).getNumericCellValue();
                    String combo="";
                    String dataele="";
                   if(mapping.size()>0){
                        combo=mapping.get(0).getCategoryOptionComboUID();
                        dataele=mapping.get(0).getDataelementUID();
                    }
                    JSONObject obj2 = new JSONObject();
                    obj2.put("dataElement",dataele);
                    obj2.put("categoryOptionCombo",combo);
                    obj2.put("value",value);
                    jString=obj2.toString();
                    System.out.println(jString);
                    concatString =concatString+jString+",";
                    System.out.println("Final "+ concatString);
                  //  System.out.println("row "+x+"y value "+y+" values"+ name+" "+htext+" "+value+" Cells "+numberOfCells+ "Combo "+combo+"element "+dataele);

                }
            JSONObject obj = new JSONObject();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String DateCompleted= dateFormat.format(date);
            obj.put("dataSet", dataset);
            obj.put("completeDate", DateCompleted);
            obj.put("period", period);
            obj.put("orgUnit", orgunits);
            obj.put("attributeOptionCombo","KOoyurqK0Uf");
            jStringTwo=obj.toString();
            oString = jStringTwo.substring(0, jStringTwo.length() - 1);
            concatString2=concatString.substring(0, concatString.length() - 1);
            Finaldatavalues =oString+", \"dataValues\": [ "+concatString2+" ]}";
            System.out.println(Finaldatavalues);
            pushToAPI(Finaldatavalues);
            Finaldatavalues ="";
            concatString2="";
            concatString="";
            jStringTwo="";

            }
        }

    }
    public void uploadcovid(String destFileName,File destFile,String dataset) throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        Sheet sheet;
        InputStream fis = null;
        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook= new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        sheet = workbook.getSheetAt(0);
        int totalRowNum = sheet.getLastRowNum();
        Row headerRow =sheet.getRow(0);
        for(int x =2;x<=totalRowNum-1;x++) {
            Row currentRow = sheet.getRow(x);
            int numberOfCells = currentRow.getLastCellNum();
            System.out.println("Hello Rugute cells " + numberOfCells);
            // CellAddress cellAddress = new CellAddress("C2");
            // Cell cell = currentRow.getCell(35);
            String county ="";
            String countyuuid="";
            String month ="";
            String nmonth ="";
            String period ="";
            String jString="";
            String concatString="";
            String Finaldatavalues="";
            String concatString2="";
            String oString="";
            String jStringTwo="";

            int cindex=0;

            for (int c = 3; c <= numberOfCells - 1; c++) {
                Cell cell = currentRow.getCell(c);
                String header = headerRow.getCell(c).getStringCellValue();
              //  System.out.println("Hello Rugute Header value " + header );
                if(county.equals("")){
                   county =currentRow.getCell(3).getStringCellValue();
                   // Facilities facilities = facilitiesService.getByName(county);
                    Facilities facilities = facilitiesService.getByNameLike(county);
                    countyuuid = facilities.getOrgUnitUID();
                }
                else{
                    county = county;
                    countyuuid= countyuuid;
                }
                month = currentRow.getCell(4).getStringCellValue();
                nmonth = numericMonths(month);
                Date d=new Date();
                int year = Calendar.getInstance().get(Calendar.YEAR);
                period = String.valueOf(year)+nmonth;
               // System.out.println("County and UUID "+county+" uuid "+countyuuid+" nmonth "+nmonth+"Period "+period);
                if(c>=5){
                    if(c==5){cindex=1;}else{cindex = cindex+1;}
                      int type= cell.getCellType();
                            if(type==1) {
                                String svalue = currentRow.getCell(c).getStringCellValue();
                                System.out.println("Hello Rugute rows" + currentRow.getCell(c).getAddress() + " value" + svalue+" Indexs "+cindex);

                            }
                            else if(type==3){
                                System.out.println(" Hakuna Data empty cells"+ currentRow.getCell(c).getAddress() + " Indexs "+cindex);
                            }
                            else {
                                int output = (int) currentRow.getCell(c).getNumericCellValue();
                                List<com.middleware.model.Mapping> mappings =   mappingService.getBycategorycombospecificID(String.valueOf(cindex));
                                String dataelement= mappings.get(0).getDataelementUID();
                                String combouid = mappings.get(0).getCategoryOptionComboUID();
                                System.out.println("Hello Rugute rows" + currentRow.getCell(c).getAddress() + " value" + output+" Indexs "+cindex+ " Dataelements "+ dataelement+" Combouuid "+combouid);
                                JSONObject obj2 = new JSONObject();
                                obj2.put("dataElement",dataelement);
                                obj2.put("categoryOptionCombo",combouid);
                                obj2.put("value",output);
                                jString=obj2.toString();
                                System.out.println(jString);
                                concatString =concatString+jString+",";
                                System.out.println("Final "+ concatString);
                            }
                    }
                // send per roww herre
            }

            JSONObject obj = new JSONObject();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String DateCompleted= dateFormat.format(date);
            obj.put("dataSet", dataset);
            obj.put("completeDate", DateCompleted);
            obj.put("period", period);
            obj.put("orgUnit", countyuuid);
            obj.put("attributeOptionCombo","KOoyurqK0Uf");
            jStringTwo=obj.toString();
            oString = jStringTwo.substring(0, jStringTwo.length() - 1);
            concatString2=concatString.substring(0, concatString.length() - 1);
               Finaldatavalues =oString+", \"dataValues\": [ "+concatString2+" ]}";
            System.out.println(Finaldatavalues);
             pushToAPI(Finaldatavalues);
            Finaldatavalues ="";
            concatString2="";
            concatString="";
            jStringTwo="";

        }

        System.out.println("Hello Rugute rows"+ totalRowNum);

    }
    public void uploadlab( String destFileName,File destFile,String dataset) throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Sheet sheet;
        InputStream fis = null;
        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook= new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        sheet = workbook.getSheetAt(0);
        int totalRowNum = sheet.getLastRowNum();
        System.out.println("current table total: "+totalRowNum+" line");
        int headerRown = 2;
        // int superheader=1;
        Row headerRow = sheet.getRow(headerRown);
        Row superheaderRow=sheet.getRow(1);
        String elementname = headerRow.getCell(0).getStringCellValue();
        System.out.println("Header Rugute "+elementname);
        double MFLCODE = 0.0;
        double Period = 0.0;
        String title ="";
        String svalue="";
        double dvalue=0.0;
        String jString="";
        String jStringTwo="";
        String datavalues="";
        String Finaldatavalues="";
        String concatString="";
        String concatString2="";
        String orgunit ="";
        String oString="";
        String sheader="";
        for (int row = 3; row <=totalRowNum ; row++) {
            Row currentRow = sheet.getRow(row);
            int numberOfCells =162;
            System.out.println("Rows "+row);
            if(currentRow!=null) {
                System.out.println("You are at row number "+ row);
                System.out.println("Numbers of Cells " + currentRow.getLastCellNum() + "Rows " + row);
                numberOfCells = currentRow.getLastCellNum();
                Cell mcell = currentRow.getCell(3);
                if(mcell==null){
                    MFLCODE=MFLCODE;
                    System.out.println("MFLCODE WHEN NULL "+MFLCODE);
                    Facilities facilities = facilitiesService.getByMFLCODE(MFLCODE);
                    orgunit = facilities.getOrgUnitUID();
                }else{
                    System.out.println("MFLCODE "+MFLCODE);
                    MFLCODE = currentRow.getCell(3).getNumericCellValue();
                    Facilities facilities = facilitiesService.getByMFLCODE(MFLCODE);
                    orgunit = facilities.getOrgUnitUID();
                    System.out.println("MFLCODE "+MFLCODE);
                }
                Cell pcell = currentRow.getCell(4);
                if(pcell==null){
                    System.out.println("Period When null "+Period);
                }else{
                    Period = currentRow.getCell(4).getNumericCellValue();
                    System.out.println("Period "+Period);
                }
                //Loop Through Columns
                for(int c=7;c<=numberOfCells;c++){
                    System.out.println( row + "Your are at cell "+ c);
                    title= headerRow.getCell(c).getStringCellValue();
                    // sheader =superheaderRow.getCell(c, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK).getStringCellValue();
                    /*Cell sscell = superheaderRow.getCell(c);
                    if(sscell!=null){
                        sheader = superheaderRow.getCell(c).getStringCellValue();
                        System.out.println("sheader rugute"+sheader );
                    }else{
                        System.out.println("Header type "+sheader);
                        //sheader = superheaderRow.getCell(c).getStringCellValue();
                    }*/
                    System.out.println("Header type "+sheader);

                    Cell dcell = currentRow.getCell(c);
                    if(dcell==null){
                    }else{
                        int type = dcell.getCellType();
                        String output ="";
                        System.out.println("cell value type "+ type);
                        if(type==1){
                            svalue = currentRow.getCell(c).getStringCellValue();
                            int x=0;
                            if(svalue.equals("YES")){
                                x=1;
                            }else if(svalue.equals("NO")) {
                                x = 0;

                            }else if(svalue.equals("FACILITY")){
                                x = 2;
                            }
                            else if(svalue.equals("STOCK OUT")){
                                x=1;
                            }
                            else if(svalue.equals("O")){
                                x=0;
                            }
                            else if(svalue.equals(null)){
                                x=-1;
                            }
                            else{
                                x = 0;// (int)Double.parseDouble(svalue);
                            }
                            output= String.valueOf(x);
                            System.out.println("SValue "+ svalue);
                        }else{
                            // String rawValue = currentRow.getCell(c).getNumericCellValue(Mi);
                            dvalue = currentRow.getCell(c).getNumericCellValue();
                            output=String.valueOf((int) dvalue);
                            System.out.println("DValue "+ dvalue);
                        }
                        System.out.println(title);
                        List<com.middleware.model.Mapping> mapping = mappingService.getByID(title);
                        String cuid ="";
                        String duid ="";
                        if(mapping.size()>1){

                        }else if(mapping.size()==1){
                            if(title.equals("Indicate the number of sites currently at this level:")) {
                                if (output.equals("0")) {
                                    List<com.middleware.model.Mapping> mappings = mappingService.getByID("Level 0");
                                    cuid = mappings.get(0).getCategoryOptionComboUID();
                                    duid = mappings.get(0).getDataelementUID();
                                    output = "1";
                                } else if (output.equals("1")) {
                                    List<com.middleware.model.Mapping> mappings = mappingService.getByID("Level 1");
                                    cuid = mappings.get(0).getCategoryOptionComboUID();
                                    duid = mappings.get(0).getDataelementUID();
                                    output = "1";
                                } else if (output.equals("2")) {
                                    List<com.middleware.model.Mapping> mappings = mappingService.getByID("Level 2");
                                    cuid = mappings.get(0).getCategoryOptionComboUID();
                                    duid = mappings.get(0).getDataelementUID();
                                    output = "1";
                                } else if (output.equals("3")) {
                                    List<com.middleware.model.Mapping> mappings = mappingService.getByID("Level 3");
                                    cuid = mappings.get(0).getCategoryOptionComboUID();
                                    duid = mappings.get(0).getDataelementUID();
                                    output = "1";
                                } else if (output.equals("4")) {
                                    List<com.middleware.model.Mapping> mappings = mappingService.getByID("Level 4");
                                    cuid = mappings.get(0).getCategoryOptionComboUID();
                                    duid = mappings.get(0).getDataelementUID();
                                    output = "1";
                                } else if (output.equals("Accredited")) {
                                    List<Mapping> mappings = mappingService.getByID("Accredited");
                                    cuid = mappings.get(0).getCategoryOptionComboUID();
                                    duid = mappings.get(0).getDataelementUID();
                                    output = "1";
                                }
                            }else {
                                cuid = mapping.get(0).getCategoryOptionComboUID();
                                duid = mapping.get(0).getDataelementUID();
                            }



                        }else{

                        }
                        if(mapping!=null &&  output!="-1"){
                            // String cuid = mapping.getCategoryOptionComboUID();
                            // String duid = mapping.getDataelementUID();
                            System.out.println(title +" "+cuid+" "+duid);
                            JSONObject obj2 = new JSONObject();
                            obj2.put("dataElement",duid);
                            obj2.put("categoryOptionCombo",cuid);
                            obj2.put("value",output);
                            jString=obj2.toString();
                            concatString =concatString+jString+",";
                        }
                        // String cuid = mapping.getCategoryOptionComboUID();
                        // String duid = mapping.getDataelementUID();
                        // System.out.println(title +" "+cuid+" "+duid);
                    }
                }
                // end of rows push data
                concatString2=concatString.substring(0, concatString.length() - 1);
                //  System.out.println(concatString2);
                JSONObject obj = new JSONObject();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String DateCompleted= dateFormat.format(date);
                obj.put("dataSet", dataset);
                obj.put("completeDate", DateCompleted);
                obj.put("period", Period);
                obj.put("orgUnit", orgunit);
                obj.put("attributeOptionCombo","KOoyurqK0Uf");
                jStringTwo=obj.toString();
                oString = jStringTwo.substring(0, jStringTwo.length() - 1);
                //System.out.println(oString);
                Finaldatavalues =oString+", \"dataValues\": [ "+concatString2+" ]}";
                System.out.println(Finaldatavalues);
                pushToAPI(Finaldatavalues);

            }
        }

    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView login(HttpSession session) throws IOException, JSONException {

        ModelAndView modelAndView = new ModelAndView();
        System.out.println("url " + url + "pass " + username + "Password " + password + "auth " + auth);
        loadperiod();
        //loadDataset();
        //loadDataElementGroups();
        modelAndView.addObject("period",periodService.getAllDataset());
        modelAndView.addObject("dataelementgroup",dataElementGroupsService.getAllDatasetByStatus("1"));
        modelAndView.addObject("dataset",datasetService.getAllDatasetBystatus("1"));
        modelAndView.setViewName("loginnew");
        return modelAndView;

    }
    public static String numericMonths(String s) {
       String nvalue="";
        if(s.equals("January")){nvalue="01";}
       else if(s.equals("February")){nvalue="02";}
        else if(s.equals("March")){nvalue="03";}
        else if(s.equals("April")){nvalue="04";}
        else if(s.equals("May")){nvalue="05";}
        else if(s.equals("June")){nvalue="06";}
        else if(s.equals("July")){nvalue="07";}
        else if(s.equals("August")){nvalue="08";}
        else if(s.equals("September")){nvalue="09";}
        else if(s.equals("October")){nvalue="10";}
        else if(s.equals("November")){nvalue="11";}
        else if(s.equals("December")){nvalue="12";}
        return nvalue;

    }
    /*@RequestMapping(value = "/view", method = RequestMethod.POST)
    public ModelAndView post(HttpSession session) throws IOException, JSONException {

        ModelAndView modelAndView = new ModelAndView();
        System.out.println("url " + url + "pass " + username + "Password " + password + "auth " + auth);
        loadperiod();
        //loadDataset();
        //loadDataElementGroups();
        modelAndView.addObject("period",periodService.getAllDataset());
        modelAndView.addObject("dataelementgroup",dataElementGroupsService.getAllDataset());
        modelAndView.addObject("dataset",datasetService.getAllDataset());
        modelAndView.setViewName("loginnew");
        return modelAndView;

    }*/

    public void loadperiod() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        //int x = 2018;

        for(int x=2018;x<=year;x++){
            int y =1;
             for(int g=0; g<12;g++){
                 String m;
                 if(y<10){
                  m=String.valueOf(x)+"0"+String.valueOf(y);}
                 else{
                     m=String.valueOf(x)+String.valueOf(y);
                 }
                // System.out.println("Month"+m+" Year"+x);
                 y= y+1;
                 Period period = new Period();
                 period.setDisplayid(m);
                 period.setDisplayname(m);
                 period.setYear(String.valueOf(x));
                 Period dt = periodService.getByID(m);
                 if(dt==null){
                     periodService.save(period);
                 }
             }
        //x=x+1;
        }

      //  System.out.println("rugute get period"+year);
    }
        public void loadDataElementGroups() throws IOException, JSONException {
        String uri = url+"dataElementGroups.json?paging=false";
        JSONArray datasets = null;
        System.out.println(uri);

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Authorization", "Basic " + auth);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Connection", "close");

        HttpResponse httpResponse = httpClient.execute(httpGet);
        StatusLine statusLine = httpResponse.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            httpResponse.getEntity().writeTo(outputStream);
            String responseString = outputStream.toString();
            System.out.println(responseString);
            JSONObject datasetObject = new JSONObject(responseString);
            datasets = datasetObject.getJSONArray("dataElementGroups");
            for(int i = 0; i < datasets.length(); i++) {
                JSONObject c = datasets.getJSONObject(i);
                DataElementGroups dataset = new DataElementGroups();
                String id = c.getString("id");
                String displayname = c.getString("displayName");
                dataset.setDisplayid(id);
                dataset.setDisplayname(displayname);
                DataElementGroups dt = dataElementGroupsService.getByID(id);
                if(dt==null){
                    dataElementGroupsService.save(dataset);
                }
                else{

                }
              //  System.out.println(id+" name"+displayname);

            }
        }


    }
    public void loadDataset() throws IOException, JSONException {
        String uri = url+"dataSets.json";
        JSONArray datasets = null;
       //  System.out.println(uri);

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Authorization", "Basic " + auth);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Connection", "close");

        HttpResponse httpResponse = httpClient.execute(httpGet);
        StatusLine statusLine = httpResponse.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            httpResponse.getEntity().writeTo(outputStream);
            String responseString = outputStream.toString();
            System.out.println(responseString);
            JSONObject datasetObject = new JSONObject(responseString);
            datasets = datasetObject.getJSONArray("dataSets");
            for(int i = 0; i < datasets.length(); i++) {
                JSONObject c = datasets.getJSONObject(i);
                Dataset dataset = new Dataset();
                String id = c.getString("id");
                String displayname = c.getString("displayName");
                dataset.setDisplayid(id);
                dataset.setDisplayname(displayname);
                Dataset dt = datasetService.getByID(id);
                if(dt==null){
                datasetService.save(dataset);
                }
                else{

                }
               // System.out.println(id+" name"+displayname);

            }
        }


    }


    public void pushToAPI(String JsonString) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        int resposecode=0;
        String uri = url+"dataValueSets.json";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpGet = new HttpPost(uri);
        httpGet.setHeader("Authorization", "Basic " + auth);
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Connection", "close");
        StringEntity params = new StringEntity(JsonString);
        httpGet.setEntity(params);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        StatusLine statusLine = httpResponse.getStatusLine();
        //SSLContextBuilder builder = new SSLContextBuilder();
       // builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
       // SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
      //  CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
       // HttpPost request = new HttpPost(uri);
       // StringEntity params = new StringEntity(JsonString);
       // request.setHeader("content-type", "application/json");
       // request.setHeader("Authorization", "Basic " + auth);
       // request.setHeader("Accept", "application/json");
       // request.setHeader("X-Stream" , "true");
       // request.setEntity(params);
       // HttpResponse response = httpClient.execute(request);
        System.out.println(statusLine.getStatusCode());
       // System.out.println(response);
       // System.out.println(response.getStatusLine().getStatusCode());
        resposecode = statusLine.getStatusCode();
        System.out.println(resposecode);
    }

    /*@PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException {
        if (file.isEmpty()) {
            return "Upload failed, please select file";
        }

        String fileName = file.getOriginalFilename();
        String dir=System.getProperty("user.dir");
        String destFileName=dir+File.separator +"uploadedfiles_"+fileName;
        System.out.println(destFileName);
        File destFile = new File(destFileName);
        file.transferTo(destFile);

        System.out.println("Upload succeeded");
        System.out.println("Start reading EXCEL content");

        Sheet sheet;
        InputStream fis = null;

        fis = new FileInputStream(destFileName);

        Workbook workbook = null;
        try {
            workbook= new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        sheet = workbook.getSheetAt(0);

        int totalRowNum = sheet.getLastRowNum();

        System.out.println("current table total: "+totalRowNum+" line");

        String tableString="";
        tableString+="<table border='1' style='border-collapse:collapse'>";
        for(int i=0;i<totalRowNum;i++){
            Row row = sheet.getRow(i);
            if(row!=null){
                tableString+="<tr>";
                int columnNum=row.getPhysicalNumberOfCells();
                System.out.println("The total number of columns in the row: "+columnNum);
                for(int j=0;j<columnNum;j++){
                    System.out.println("current processing "+i+" line, "+j+" column");
                    Cell cell = row.getCell(j);
                    String cellValue="";
                    if(cell!=null){
                        switch (cell.getCellType()) {
                            case STRING:
                                cellValue = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                cellValue = String.valueOf(cell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            default:
                                cellValue = "";
                                break;
                        }

                    }

                    tableString+="<td>"+cellValue+"</td>";
                    System.out.println(cellValue);
                }

            }
            tableString+="</tr>";
        }
        tableString+="</table>";
        return "upload success" +tableString;
    }*/
}
