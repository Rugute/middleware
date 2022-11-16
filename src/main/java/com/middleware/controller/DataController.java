package com.middleware.controller;

import com.middleware.model.Mapping;
import com.middleware.model.User;
import com.middleware.services.*;
import com.middleware.model.Alldata;
import com.middleware.model.Facilities;
import ke.or.karp.middleware.services.*;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping("/data")
public class DataController {
    @Autowired
    public DatasetService datasetService;
    @Autowired
    public DataElementGroupsService dataElementGroupsService;
    @Autowired
    public PeriodService periodService;
    @Autowired
    public FacilitiesService facilitiesService;
    @Autowired
    public AlldataService alldataService;
    @Autowired
    public MappingService mappingService;
    @Value("${app.3pm.url}")
    public String url;
    @Value("${app.3pm.user}")
    public String username;
    @Value("${app.3pm.pass}")
    public String password;
    @Value("${app.3pm.auth}")
    public String auth;
    @Value("${app.filepath}")
    public String filepath;


    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView postss(HttpSession session) throws IOException, JSONException {
        if (session.getAttribute("user") != null) {
            User userdetails = (User) session.getAttribute("user");
            Date nowDate = new Date();

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("period", periodService.getAllDataset());
            modelAndView.addObject("dataelementgroup", dataElementGroupsService.getAllDatasetByStatus("1"));
            modelAndView.addObject("dataset", datasetService.getAllDatasetBystatus("1"));
            modelAndView.setViewName("pm/dataupload");
            return modelAndView;
        } else {
            return new ModelAndView("redirect:/auth/login");
        }

    }

    @RequestMapping(value = "/upload_data", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    public String uploads(@RequestParam("file") MultipartFile file,
                         @RequestParam("period") String period,
                         @RequestParam("report") String report,
                         @RequestParam("dataset") String dataset
    ) {

            Date nowDate = new Date();
            String progres = "";
            if (file.isEmpty()) {
                progres = "Upload failed, please select file";
            }
            String fileName = file.getOriginalFilename();
            String destFileName = filepath + fileName;

            try {
                File destFile = new File(destFileName);
                boolean exists = destFile.exists();
                if (exists = true) {
                    destFile.deleteOnExit();
                    System.out.println("Deleting values");
                }
                System.out.println("Upload succeeded " + exists + " report " + report);
                file.transferTo(new File(destFileName));
                System.out.println("Upload succeeded");
                System.out.println("Start reading EXCEL content");

                if(report.equals("Dv3gWMeVuHM")){
                    System.out.println("Asante mungu imefika hapa report hhahahds "+report);
                    progres= uploadCOVID(destFileName,destFile,dataset,period);
                } else if(report.equals("ARV&Meds Commodities")){
                  progres=   uploadARVLab(destFileName,destFile,dataset);
                }
                else if(report.equals("P0lzsnbDM2M")){
                    System.out.println("Ndo Hiyo sasa");
                    progres= uploadLabCommodities(destFileName,destFile,dataset);
                }
                else if(report.equals("ip1XNsR4YiV")){
                    System.out.println("HTS Indicators");
                    progres =  uploadHTS(destFileName,destFile,dataset,period);
                }
                else if(report.equals("SdOa2cJ7uoH")){
                    System.out.println("Prep_FY 22 Indicators");
                  progres =  uploadPREP(destFileName,destFile,dataset,period);
                }
                else if(report.equals("dYJrrwHaOxy")){
                    System.out.println("OTZ indicators");
                   progres =  uploadOTZ(destFileName,destFile,dataset,period);
                }
                else{
                    System.out.println("Prep_FY 22 Indicators teststst");
                }

            } catch (Exception e) {
                progres = e.getMessage();
                System.out.println("Error Nod hii " + progres);
                return HttpStatus.INTERNAL_SERVER_ERROR.toString();
            }
             return progres;
    }
    public String uploadLabCommodities( String destFileName,File destFile,String dataset) throws IOException, JSONException {
        String response="";
        Sheet sheet;
        InputStream fis = null;
        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        for (int s = 0; s < workbook.getNumberOfSheets(); s++) {

            sheet = workbook.getSheetAt(s);
            int totalRowNum = sheet.getLastRowNum();
            System.out.println("current table total: " + totalRowNum + " line");
            int headerRown = 2;
            // int superheader=1;
            Row headerRow = sheet.getRow(headerRown);
            Row headerTitle = sheet.getRow(3);
            Row superheaderRow = sheet.getRow(1);
            String elementname = headerRow.getCell(0).getStringCellValue();
            // System.out.println("Header Rugute "+elementname);
            double MFLCODE = 0.0;
            double Period = 0.0;
            String title = "";
            String svalue = "";
            double dvalue = 0.0;
            String jString = "";
            String jStringTwo = "";
            String datavalues = "";
            String Finaldatavalues = "";
            String concatString = "";
            String concatString2 = "";
            String orgunit = "";
            String oString = "";
            String sheader = "";
            for (int row = 4; row < 10; row++) {
                Row currentRow = sheet.getRow(row);
                int numberOfCells = 13;
                totalRowNum=10;
                System.out.println("Rows " + row);
                if (currentRow != null) {
                    System.out.println("You are at row number " + row);
                    System.out.println("Numbers of Cells " + headerRow.getLastCellNum() + "Rows " + row);
                    numberOfCells = headerRow.getLastCellNum();
                    Cell mcell = currentRow.getCell(3);
                    if (mcell == null) {
                        MFLCODE = MFLCODE;
                        System.out.println("MFLCODE WHEN NULL " + MFLCODE);
                        Facilities facilities = facilitiesService.getByMFLCODE(MFLCODE);
                        orgunit = facilities.getOrgUnitUID();
                    } else {
                        System.out.println("Sheet Name "+ sheet.getSheetName()  +"MFLCODE " + MFLCODE);
                        System.out.println("Check cell value " + headerRow.getCell(7).getNumericCellValue());
                        MFLCODE = headerRow.getCell(7).getNumericCellValue();
                        Facilities facilities = facilitiesService.getByMFLCODE(MFLCODE);
                        orgunit = facilities.getOrgUnitUID();
                        System.out.println("MFLCODE " + MFLCODE);
                    }
                    String month = headerRow.getCell(10).getStringCellValue();
                    int year = (int) headerRow.getCell(13).getNumericCellValue();
                    String nmonth = DashboardController.numericMonths(month);
                    String p = year+nmonth ;
                    Period = Integer.parseInt(p); //headerRow.getCell(10).getStringCellValue();
                    System.out.println("Period " + Period + " Month " + month + " Year " + year + " nmonth " + nmonth);
                    //}
                    //Loop Through Columns
                    for (int c = 1; c <= 13; c++) {
                        System.out.println(row + " Your are at cell " + c);
                        title = headerTitle.getCell(c).getStringCellValue();
                        String Rtitle = currentRow.getCell(0).getStringCellValue();
                        System.out.println("Title Ndo Hii " + title + "Static Cell Ndo Hii " + Rtitle);
                        System.out.println("Header type " + sheader);

                        Cell dcell = currentRow.getCell(c);
                        if (dcell == null) {
                        } else {
                            int type = dcell.getCellType();
                            String output = "";
                            System.out.println("cell value type " + type);
                            if (type == 1) {
                                svalue = currentRow.getCell(c).getStringCellValue();

                                output = svalue.substring(1);
                                System.out.println("SValue " + output);
                            } else {
                                dvalue = currentRow.getCell(c).getNumericCellValue();
                                output = String.valueOf((int) dvalue);
                                System.out.println("DValue " + dvalue);
                            }

                            System.out.println(title + "Row Cell 1" +Rtitle);
                            List<Mapping> mapping = mappingService.getBycategorycombospecificIDAndDataelement(title,Rtitle);
                            System.out.println("Size ndo hii spcpscp"+mapping.size());
                            if (mapping.size()>0) {
                                String cuid = mapping.get(0).getCategoryOptionComboUID();
                                String duid = mapping.get(0).getDataelementUID();
                                System.out.println(title + " " + cuid + " " + duid);
                                JSONObject obj2 = new JSONObject();
                                obj2.put("dataElement", duid);
                                obj2.put("categoryOptionCombo", cuid);
                                obj2.put("value", output);
                                jString = obj2.toString();
                                concatString = concatString + jString + ",";
                                //Added to DB
                                Date nowDate = new Date();
                                List<Alldata> alldataList = alldataService.getSpecificAllData(mapping.get(0).getId(),orgunit,dataset,String.valueOf(Period));
                                if(alldataList.size()>0){
                                    Alldata alldata =  alldataList.get(0);
                                    alldata.setValue(Integer.parseInt(output));
                                    alldata.setModified_by(1);
                                    alldata.setModified_on(nowDate);
                                    alldataService.save(alldata);
                                }else{
                                    Alldata alldata = new Alldata();
                                    alldata.setMid(mapping.get(0).getId());
                                    alldata.setCategorycombo(cuid);
                                    alldata.setDataelement(duid);
                                    alldata.setOrgunit(orgunit);
                                    alldata.setDataset(dataset);
                                    alldata.setPeriod(String.valueOf(Period));
                                    alldata.setValue(Integer.parseInt(output));
                                    alldata.setCreated_by(1);
                                    alldata.setCreated_on(nowDate);
                                    alldataService.save(alldata);
                                }
                                // System.out.println(" Concat Hakuna kitu "+concatString);
                            }

                        }
                    }


                }
                // end of rows push data
                concatString2 = concatString.substring(0, concatString.length() - 1);
                //  System.out.println(concatString2);
                JSONObject obj = new JSONObject();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String DateCompleted = dateFormat.format(date);
                obj.put("dataSet", dataset);
                obj.put("completeDate", DateCompleted);
                obj.put("period", Period);
                obj.put("orgUnit", orgunit);
                obj.put("attributeOptionCombo", "KOoyurqK0Uf");
                jStringTwo = obj.toString();
                oString = jStringTwo.substring(0, jStringTwo.length() - 1);
                //System.out.println(oString);
                Finaldatavalues = oString + ", \"dataValues\": [ " + concatString2 + " ]}";
                System.out.println(Finaldatavalues);
                try {
                    pushToAPI(Finaldatavalues);
                    response="1";
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                    response = e.getMessage();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    response = e.getMessage();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                    response = e.getMessage();
                }
            }
            System.out.println("End of sheet" + s);
        }
       return  response;
    }

    public String uploadPREP(String destFileName, File destFile, String dataset,String period) throws IOException, JSONException {
        String response="";
        // try{
        Date nowDate = new Date();
        Sheet sheet;
        InputStream fis = null;

        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook= new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        int tsheets = workbook.getNumberOfSheets();
        for(int s=2;s<tsheets;s++) {
            double mflcode = 0.0;
            String agegroup = "";
            String age = "";
            String gender = "";
            String title = "";
            String ftitle = "";
            String CCID = "", DEID = "";
            int value = 0;
            String jString = "";
            String concatString = "";
            String Finaldatavalues = "";
            String concatString2 = "";
            String oString = "";
            String jStringTwo = "";
            String orgunit = "";
            sheet = workbook.getSheetAt(s);
            String sheetname = sheet.getSheetName();
            System.out.println("Sheet name " + sheetname);
            int numberOfRows = sheet.getLastRowNum();
            System.out.println("Number of Rows " + numberOfRows);
            Row headerfacility = sheet.getRow(6);
            Row headerage = sheet.getRow(7);
            Row headergender = sheet.getRow(8);

            mflcode = headerfacility.getCell(2).getNumericCellValue();
            System.out.println("MFLCODE "+mflcode);
            Facilities facilities = facilitiesService.getByMFLCODE(mflcode);
            String  countyuuid= facilities.getOrgUnitUID();
            for(int r=10; r<numberOfRows;r++) {
                // Row rowdata = sheet.getRow(r);

                for (int c = 0; c < 10; c++) {
                    String ind =  sheet.getRow(r).getCell(1).getStringCellValue();
                    String disaggregation =  sheet.getRow(r).getCell(3).getStringCellValue();
                    if(ind.equals("")){
                        int yy = r-1;
                        while(ind.equals("")){
                            ind = sheet.getRow(yy).getCell(1).getStringCellValue();
                            yy = yy-1;
                        }
                        // ind = sheet.getRow(r-1).getCell(1).getStringCellValue();
                    }
                    if (c>=4) {
                        String g = headergender.getCell(c).getStringCellValue();
                        String y = headerage.getCell(c).getStringCellValue();
                        if(y.equals("")){
                            y =  headerage.getCell(c-1).getStringCellValue();
                        }
                        int val = (int) sheet.getRow(r).getCell(c).getNumericCellValue();
                        String output = String.valueOf(val);
                        Cell cell = sheet.getRow(r).getCell(c);
                        CellStyle cellStyle = cell.getCellStyle();
                        Color color = cellStyle.getFillBackgroundColorColor();
                        System.out.println("Indicators "+ind +" row "+r + " Disaggregation "+disaggregation+" Gender " +g+" Year "+ y +" Value"+ val+ " BG Color "+color);
                        List<Mapping> mappings = mappingService.getByAgeGendercategorycombospecificIDAndDataelement(y,g,disaggregation,ind);
                        if(mappings.size()>0){
                            CCID = mappings.get(0).getCategoryOptionComboUID();
                            DEID = mappings.get(0).getDataelementUID();
                            System.out.println("SHEET NAME "+ sheetname + " CCID "+CCID +" DEID "+DEID);
                            List<Alldata> alldataList = alldataService.getSpecificAllData(mappings.get(0).getId(),countyuuid,dataset,period);
                            if(alldataList.size()>0){
                                Alldata alldata =  alldataList.get(0);
                                alldata.setValue(val);
                                alldata.setModified_by(1);
                                alldata.setModified_on(nowDate);
                                alldataService.save(alldata);
                            }else{
                                Alldata alldata = new Alldata();
                                alldata.setMid(mappings.get(0).getId());
                                alldata.setCategorycombo(CCID);
                                alldata.setDataelement(DEID);
                                alldata.setOrgunit(countyuuid);
                                alldata.setDataset(dataset);
                                alldata.setPeriod(period);
                                alldata.setValue(val);
                                alldata.setCreated_by(1);
                                alldata.setCreated_on(nowDate);
                                alldataService.save(alldata);
                            }
                            // System.out.println("Hello Rugute rows" + currentRow.getCell(c).getAddress() + " value" + output+" Indexs "+cindex+ " Dataelements "+ dataelement+" Combouuid "+combouid);
                            JSONObject obj2 = new JSONObject();
                            obj2.put("dataElement",DEID);
                            obj2.put("categoryOptionCombo",CCID);
                            obj2.put("value",output);
                            jString=obj2.toString();
                            System.out.println(jString);
                            concatString =concatString+jString+",";
                            System.out.println("Final "+ concatString);
                        }
                    }

                }
            }
            System.out.println("Final "+ concatString);
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
            try {
                pushToAPI(Finaldatavalues);
                response="1";
            } catch (KeyStoreException e) {
                e.printStackTrace();
                response=e.getMessage();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                response =e.getMessage();
            } catch (KeyManagementException e) {
                e.printStackTrace();
                response =e.getMessage();
            }
            Finaldatavalues ="";
            concatString2="";
            concatString="";
            jStringTwo="";

        }
        return response;
    }

    public String uploadHTS (String destFileName, File destFile, String dataset,String period) throws IOException, JSONException {
        String response="";
        // try{
        Date nowDate = new Date();
        Sheet sheet;
        InputStream fis = null;

        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook= new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        int tsheets = workbook.getNumberOfSheets();
        for(int s=0;s<tsheets;s++) {
            double mflcode = 0.0;
            String agegroup = "";
            String age = "";
            String gender = "";
            String title = "";
            String ftitle = "";
            String CCID = "", DEID = "";
            int value = 0;
            String jString = "";
            String concatString = "";
            String Finaldatavalues = "";
            String concatString2 = "";
            String oString = "";
            String jStringTwo = "";
            String orgunit = "";
            sheet = workbook.getSheetAt(s);
            String sheetname = sheet.getSheetName();
            System.out.println("Sheet name " + sheetname);
            int numberOfRows = sheet.getLastRowNum();
            System.out.println("Number of Rows " + numberOfRows);

            int crow = 0;
            int hrow = 0;
            int arow = 0;
            int groww = 0;

            if (sheetname.equals("STI") || sheetname.equals("OPD") || sheetname.equals("IPD")) {
                crow = 5;
                groww = 4;
                arow = 3;
                hrow = 2;
            } else if (sheetname.equals("MNCH")) {
                crow = 4;
                arow = 3;
                hrow = 2;
                groww = 3;
            } else if (sheetname.equals("FP")) {
                crow = 5;
                groww = 4;
                arow = 3;
                hrow = 1;
            } else if (sheetname.equals("VMMC")) {
                crow = 4;
                groww = 2;
                arow = 1;
                hrow = 1;
            } else {
                crow = 4;
                groww = 3;
                arow = 2;
                hrow = 1;
            }
            if (sheetname.equals("VMMC") || sheetname.equals("FP")) {


            } else {
                Row staticheaderRow = sheet.getRow(hrow);
                Row staticRowGender = sheet.getRow(groww);
                Row staticRowAgeGroup = sheet.getRow(arow);

                for (int r = crow; r < numberOfRows; r++) {

                    Row currentRow = sheet.getRow(r);
                    System.out.println("Curren row " + r);
                    if (currentRow == null) {

                    } else {
                        int type = currentRow.getCell(4).getCellType();
                        System.out.println("Cell type " + type + "Address" + currentRow.getCell(4).getAddress());
                        if (type != 0) {
                        } else {
                            mflcode = currentRow.getCell(4).getNumericCellValue();
                            Facilities facilities = facilitiesService.getByMFLCODE(mflcode);
                            orgunit = facilities.getOrgUnitUID();
                            int numberOfCells = staticRowGender.getLastCellNum();
                            agegroup = staticRowAgeGroup.getCell(6).getStringCellValue();
                            //String cellAddress = currentRow.get
                            System.out.println("Facility MFLCODE " + mflcode + " Age group " + agegroup);
                            System.out.println("Period  " + period);
                            System.out.println("Row number " + numberOfRows + "Current row " + r + "Last row " + crow + "Last Column number " + numberOfCells);
                            for (int c = 6; c < numberOfCells; c++) {
                                if ((sheetname.equals("IPD") && c == 6) || (sheetname.equals("FP") && c == 6) || (sheetname.equals("OPD") && c == 6) || (sheetname.equals("STI") && c == 6)) {
                                    System.out.println("Cell 6 with workload");
                                    ftitle ="workload";
                                    List<Mapping> mappings = mappingService.findByCATEGORYCOMBOSPECIFICAndDENMAPPER("Workload",sheetname);
                                    if (mappings.size() > 0) {
                                        System.out.println("Workload is present"+ mappings.get(0).getCategoryOptionComboUID());
                                        CCID = mappings.get(0).getCategoryOptionComboUID();
                                        DEID = mappings.get(0).getDataelementUID();



                                        List<Alldata> alldataList = alldataService.getSpecificAllData(mappings.get(0).getId(), orgunit, dataset, period);
                                        if (alldataList.size() > 0) {
                                            Alldata alldata = alldataList.get(0);
                                            alldata.setValue(value);
                                            alldata.setModified_by(1);
                                            alldata.setModified_on(nowDate);
                                            alldataService.save(alldata);
                                        } else {
                                            Alldata alldata = new Alldata();
                                            alldata.setMid(mappings.get(0).getId());
                                            alldata.setCategorycombo(CCID);
                                            alldata.setDataelement(DEID);
                                            alldata.setOrgunit(orgunit);
                                            alldata.setDataset(dataset);
                                            alldata.setPeriod(period);
                                            alldata.setValue(value);
                                            alldata.setCreated_by(1);
                                            alldata.setCreated_on(nowDate);
                                            alldataService.save(alldata);
                                        }

                                        JSONObject obj2 = new JSONObject();
                                        obj2.put("dataElement", DEID);
                                        obj2.put("categoryOptionCombo", CCID);
                                        obj2.put("value", value);
                                        jString = obj2.toString();
                                        // System.out.println(jString);
                                        concatString = concatString + jString + ",";
                                        JSONObject obj = new JSONObject();
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date = new Date();
                                        String DateCompleted = dateFormat.format(date);
                                        obj.put("dataSet", dataset);
                                        obj.put("completeDate", DateCompleted);
                                        obj.put("period", period);
                                        obj.put("orgUnit", orgunit);
                                        obj.put("attributeOptionCombo", "KOoyurqK0Uf");
                                        jStringTwo = obj.toString();
                                        oString = jStringTwo.substring(0, jStringTwo.length() - 1);
                                        concatString2 = concatString.substring(0, concatString.length() - 1);
                                        Finaldatavalues = oString + ", \"dataValues\": [ " + concatString2 + " ]}";
                                        //System.out.println(Finaldatavalues);
                                        try {
                                            pushToAPI(Finaldatavalues);
                                            response ="1";
                                        } catch (KeyStoreException e) {
                                            response =e.getMessage();
                                            e.printStackTrace();
                                        } catch (NoSuchAlgorithmException e) {
                                            response =e.getMessage();
                                            e.printStackTrace();
                                        } catch (KeyManagementException e) {
                                            response =e.getMessage();
                                            e.printStackTrace();
                                        }
                                        Finaldatavalues = "";
                                        concatString2 = "";
                                        concatString = "";
                                        jStringTwo = "";
                                    }


                                } else {
                                    System.out.println("Mising Workload");
                                    age = staticRowAgeGroup.getCell(c).getStringCellValue();
                                    if (age.equals("")) {
                                        int cc = c - 1;
                                        agegroup = staticRowAgeGroup.getCell(cc).getStringCellValue();
                                    } else {
                                        agegroup = age;
                                    }
                                    String agegroupp = agegroup.replace("Yrs", "");
                                    String fagegroup = "";
                                    if (agegroupp.equals("<15 ") || agegroupp.equals("<15 Yr")) {
                                        fagegroup = "0-14";
                                    } else {
                                        fagegroup = agegroupp.replaceAll("\\s", "");
                                    }
                                    title = staticheaderRow.getCell(c).getStringCellValue();
                                    System.out.println("Cell Address " + staticRowAgeGroup.getCell(c).getAddress() + "Sheet Name " + sheetname);

                                    if (title.equals("")) {
                                        for (int t = c; title.equals(""); t--) {
                                            title = staticheaderRow.getCell(t).getStringCellValue();
                                        }
                                    }
                                    ftitle = title;
                                    gender = staticRowGender.getCell(c).getStringCellValue();
                                    //Get values
                                    int ct = currentRow.getCell(c).getCellType();
                                    if (ct != 0) {

                                    } else {
                                        value = (int) currentRow.getCell(c).getNumericCellValue();
                                        List<Mapping> mappingList = null;
                                        if (sheetname.equals("MNCH")) {// && (ftitle.equals("Under 5") || ftitle.equals() ) ){
                                            if (ftitle.equals("Under 5") || ftitle.equals("Malnutrition Clinic (<5 Years)")) {
                                                fagegroup = agegroup;
                                            }
                                            System.out.println(ftitle + " age group " + agegroup);
                                            mappingList = mappingService.findByComboYearandDENMAPPER(ftitle, fagegroup, sheetname);

                                        } else {
                                            mappingList = mappingService.getByAgeGendercategorycombospecificIDAndDataelement(fagegroup, gender, sheetname, ftitle);

                                        }
                                        if (mappingList.size() > 0) {
                                            CCID = mappingList.get(0).getCategoryOptionComboUID();
                                            DEID = mappingList.get(0).getDataelementUID();

                                            List<Alldata> alldataList = alldataService.getSpecificAllData(mappingList.get(0).getId(), orgunit, dataset, period);
                                            if (alldataList.size() > 0) {
                                                Alldata alldata = alldataList.get(0);
                                                alldata.setValue(value);
                                                alldata.setModified_by(1);
                                                alldata.setModified_on(nowDate);
                                                alldataService.save(alldata);
                                            } else {
                                                Alldata alldata = new Alldata();
                                                alldata.setMid(mappingList.get(0).getId());
                                                alldata.setCategorycombo(CCID);
                                                alldata.setDataelement(DEID);
                                                alldata.setOrgunit(orgunit);
                                                alldata.setDataset(dataset);
                                                alldata.setPeriod(period);
                                                alldata.setValue(value);
                                                alldata.setCreated_by(1);
                                                alldata.setCreated_on(nowDate);
                                                alldataService.save(alldata);
                                            }

                                            JSONObject obj2 = new JSONObject();
                                            obj2.put("dataElement", DEID);
                                            obj2.put("categoryOptionCombo", CCID);
                                            obj2.put("value", value);
                                            jString = obj2.toString();
                                            // System.out.println(jString);
                                            concatString = concatString + jString + ",";
                                            //   System.out.println("Final "+ concatString);

                                        } else {

                                        }

                                    }

                                    System.out.println("Cell Address " + staticRowAgeGroup.getCell(c).getAddress() + "Sheet Name " + sheetname);
                                    System.out.println("Age value " + fagegroup + " Gender " + gender + " Title " + ftitle + " value " + value + " Ctype " + ct + " CCID " + CCID + " DEID " + DEID + " Facility MFLCODE " + mflcode + " Current row " + r + "MFLCODE CHECK " + currentRow.getCell(4).getNumericCellValue());
                                    //System.out.println("Final "+ concatString);


                                }
                            }
                        }


                        //Push to API Per Row
                        List<Mapping> mapping = mappingService.findByDENMAPPER(sheetname);
                        if (mapping.size() > 0) {
                            JSONObject obj = new JSONObject();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            String DateCompleted = dateFormat.format(date);
                            obj.put("dataSet", dataset);
                            obj.put("completeDate", DateCompleted);
                            obj.put("period", period);
                            obj.put("orgUnit", orgunit);
                            obj.put("attributeOptionCombo", "KOoyurqK0Uf");
                            jStringTwo = obj.toString();
                            oString = jStringTwo.substring(0, jStringTwo.length() - 1);
                            concatString2 = concatString.substring(0, concatString.length() - 1);
                            Finaldatavalues = oString + ", \"dataValues\": [ " + concatString2 + " ]}";
                            //System.out.println(Finaldatavalues);
                            try {
                                pushToAPI(Finaldatavalues);
                                response ="1";
                            } catch (KeyStoreException e) {
                                response =e.getMessage();
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                response =e.getMessage();
                                e.printStackTrace();
                            } catch (KeyManagementException e) {
                                response =e.getMessage();
                                e.printStackTrace();
                            }
                            System.out.println("Yoe are at Row " + r + " Sheet " + sheetname);

                            Finaldatavalues = "";
                            concatString2 = "";
                            concatString = "";
                            jStringTwo = "";
                            response = "1";
                        }

                        DEID = "";
                        CCID = "";
                        mflcode = 0.0;
                    }
                }
            }
        }
            /* }catch (Exception e){
                 System.out.println("Error found, Details "+e.getMessage());
                response="2";
             }*/

        return  response;

    }

    public String uploadCOVID (String destFileName, File destFile, String dataset,String periodd) throws IOException, JSONException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        String response="";
        Date nowDate = new Date();
        Sheet sheet;
        InputStream fis = null;
        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook= new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        int tsheets = workbook.getNumberOfSheets();
        int a1=tsheets;
        int a2=0;
        sheet = workbook.getSheetAt(0);
        int totalRowNum = sheet.getLastRowNum();

        int a3=totalRowNum;
        int a4=0;
        int currrow=0;
        Row headerRow =sheet.getRow(0);
        for(int x =2;x<11;x++) {
            currrow=x;
            a4=currrow;
            Row currentRow = sheet.getRow(x);
            int numberOfCells = currentRow.getLastCellNum();
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

            //Replaced 35 with numberOfCells

            for (int c = 3; c <numberOfCells; c++) {
                Cell cell = currentRow.getCell(c);
                String header = headerRow.getCell(c).getStringCellValue();
                System.out.println("Cell Number " + c +" rowcells "+numberOfCells);
                System.out.println("Row number "+ x+" Total rows "+ totalRowNum );
                //  System.out.println("Hello Rugute Header value " + header );
                if(county.equals("")){
                    county =currentRow.getCell(3).getStringCellValue();
                    System.out.println("My County is this "+county);
                    //Facilities facilities = facilitiesService.getByFacilityNameLike(county);
                    List<Facilities> facilities = facilitiesService.searchByFnameLike(county);

                    int xg = facilities.size();
                    System.out.println("Counnty Size is "+ xg);
                    countyuuid = facilities.get(0).getOrgUnitUID();
                }
                else{
                    county = county;
                    countyuuid= countyuuid;
                }
                month = currentRow.getCell(4).getStringCellValue();
                nmonth = DashboardController.numericMonths(month);
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
                        List<Mapping> mappings =   mappingService.findByCATEGORYCOMBOSPECIFICAndDENMAPPER(String.valueOf(cindex),"COVID");
                        String dataelement= mappings.get(0).getDataelementUID();
                        String combouid = mappings.get(0).getCategoryOptionComboUID();
                        List<Alldata> alldataList = alldataService.getSpecificAllData(mappings.get(0).getId(),countyuuid,dataset,period);
                        if(alldataList.size()>0){
                            Alldata alldata =  alldataList.get(0);
                            alldata.setValue(output);
                            alldata.setModified_by(1);
                            alldata.setModified_on(nowDate);
                            alldataService.save(alldata);
                        }else{
                            Alldata alldata = new Alldata();
                            alldata.setMid(mappings.get(0).getId());
                            alldata.setCategorycombo(combouid);
                            alldata.setDataelement(dataelement);
                            alldata.setOrgunit(countyuuid);
                            alldata.setDataset(dataset);
                            alldata.setPeriod(period);
                            alldata.setValue(output);
                            alldata.setCreated_by(1);
                            alldata.setCreated_on(nowDate);
                            alldataService.save(alldata);
                        }
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
            obj.put("period", periodd);
            obj.put("orgUnit", countyuuid);
            obj.put("attributeOptionCombo","KOoyurqK0Uf");
            jStringTwo=obj.toString();
            oString = jStringTwo.substring(0, jStringTwo.length() - 1);
            concatString2=concatString.substring(0, concatString.length() - 1);
            Finaldatavalues =oString+", \"dataValues\": [ "+concatString2+" ]}";
            System.out.println(Finaldatavalues);
            pushToAPI(Finaldatavalues);
            response="1";
            /*try {
                pushToAPI(Finaldatavalues);
                response="1";

            } catch (KeyStoreException e) {
                response=e.getMessage();
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                response=e.getMessage();
                e.printStackTrace();
            } catch (KeyManagementException e) {
                response=e.getMessage();
                e.printStackTrace();
            }*/
            Finaldatavalues ="";
            concatString2="";
            concatString="";
            jStringTwo="";

        }
        return response;
    }

    public String uploadOTZ (String destFileName, File destFile, String dataset,String periodd) throws IOException, JSONException{
        String response="";
        Date nowDate = new Date();
        Sheet sheet;
        InputStream fis = null;

        fis = new FileInputStream(destFileName);
        Workbook workbook = null;
        try {
            workbook= new XSSFWorkbook(destFile);
        } catch (Exception ex) {
            workbook = new HSSFWorkbook(fis);
        }
        int tsheets = workbook.getNumberOfSheets();
        System.out.println("Total Sheets "+tsheets);
        sheet = workbook.getSheetAt(1);
        int totalRowNum = sheet.getLastRowNum();
        System.out.println("Total rows "+ totalRowNum);
        double mflcode = 0.0;
        for (int row = 4; row <totalRowNum ; row++) {
            Row currentRow = sheet.getRow(row);
            mflcode =  currentRow.getCell(4,Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
             Facilities facilities = facilitiesService.getByMFLCODE(mflcode);
             String  orgunit= facilities.getOrgUnitUID();
            System.out.println("mflcode "+ mflcode );//+" uuid "+ countyuuid);
            int numberOfCells = currentRow.getLastCellNum();
            int index =1;
            String jString="";
            String concatString="";
            String jStringTwo="";
            String oString="";
            String concatString2="";
            String Finaldatavalues="";
            for(int c=6;c<numberOfCells;c++){

                if(index>266){
                    System.out.println("Out of index" + index);
                }else {
                    Cell cell = currentRow.getCell(c);
                    CellStyle cellStyle = cell.getCellStyle();
                    Color color = cellStyle.getFillBackgroundColorColor();
                    if(color!=null){
                       // index = index-1;
                    }
                    else {

                        List<Mapping> mappings = mappingService.findByCATEGORYCOMBOSPECIFICAndDENMAPPER(String.valueOf(index), "OTZ");
                        String dataelement = mappings.get(0).getDataelementUID();
                        String combouid = mappings.get(0).getCategoryOptionComboUID();
                        double output = currentRow.getCell(c, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
                        JSONObject obj2 = new JSONObject();
                        obj2.put("dataElement", dataelement); //DEID
                        obj2.put("categoryOptionCombo", combouid); //CCID
                        obj2.put("value", output);
                        jString = obj2.toString();
                        // System.out.println(cell);
                        System.out.println(jString);
                        concatString = concatString + jString + ",";
                        System.out.println("Row " + row + " Current cell value " + c + " index " + index + " Value " + output + " Check " + mappings.get(0).getDATAELEMENTSPECIF() + " Gender " + mappings.get(0).getCATEGORYCOMBOGENDER() + " Color " + color);
                        index = index + 1;
                    }
                }
            }
            JSONObject obj = new JSONObject();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String DateCompleted= dateFormat.format(date);
            obj.put("dataSet", dataset);
            obj.put("completeDate", DateCompleted);
            obj.put("period", periodd);
            obj.put("orgUnit",orgunit );
            obj.put("attributeOptionCombo","KOoyurqK0Uf");
            jStringTwo=obj.toString();
            oString = jStringTwo.substring(0, jStringTwo.length() - 1);
            concatString2=concatString.substring(0, concatString.length() - 1);
            Finaldatavalues =oString+", \"dataValues\": [ "+concatString2+" ]}";
            System.out.println(Finaldatavalues);
            try {
                pushToAPI(Finaldatavalues);
                response="1";
            } catch (KeyStoreException e) {
                response = e.getMessage();
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                response = e.getMessage();
                e.printStackTrace();
            } catch (KeyManagementException e) {
                response = e.getMessage();
                e.printStackTrace();
            }
            Finaldatavalues ="";
            concatString2="";
            concatString="";



        }
        return response;
    }
    public String uploadARVLab(String destFileName,File destFile,String dataset) throws IOException, JSONException {
        String response="";
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
            System.out.println("Sheet Name " + sheet.getSheetName());
            int mflcode = (int) facilityRow.getCell(7).getNumericCellValue();
            //  String mflcode = facilityRow.getCell(7).getStringCellValue();
            String month =  facilityRow.getCell(10).getStringCellValue();
            int year = (int) facilityRow.getCell(12).getNumericCellValue();
            //String year =  facilityRow.getCell(12).getStringCellValue();
            String nmonth = DashboardController.numericMonths(month);
            String period = year+nmonth;
            Facilities facilities = facilitiesService.getByMFLCODE((mflcode));
            String orgunits = facilities.getOrgUnitUID();
            String jString="";
            String concatString="";
            String Finaldatavalues="";
            String concatString2="";
            String oString="";
            String jStringTwo="";
            System.out.println("Sheet values are "+sheet.getSheetName()+" Mflcode "+mflcode+" Month "+month+" Year"+year+" period"+period);
            for(int x =4;x<=18;x++) {
                Row currentRow = sheet.getRow(x);
                Row headerTitle = sheet.getRow(3);
                for(int c=2;c<13;c++) {
                    String title = headerTitle.getCell(c).getStringCellValue();
                    String name = currentRow.getCell(1).getStringCellValue();
                    int output = 0;
                    Cell dcell = currentRow.getCell(c);
                    if (dcell == null) {
                        output = 0;

                    } else {
                        int type = dcell.getCellType();
                        output = 0;
                        System.out.println("cell value type " + type);
                        if (type == 1) {
                            output = 0;// currentRow.getCell(c).getStringCellValue();

                            //  output = svalue.substring(1);
                            System.out.println("SValue " + output);
                        } else {

                            output = (int) currentRow.getCell(c).getNumericCellValue();

                        }
                    }

                    List<Mapping> mapping = mappingService.getBycategorycombospecificIDAndDataelement(title, name);

                    if (mapping.size() > 0) {
                        String cuid = mapping.get(0).getCategoryOptionComboUID();
                        String duid = mapping.get(0).getDataelementUID();
                        System.out.println(title + " " + cuid + " " + duid);
                        JSONObject obj2 = new JSONObject();
                        obj2.put("dataElement", duid);
                        obj2.put("categoryOptionCombo", cuid);
                        obj2.put("value", output);
                        jString = obj2.toString();
                        concatString = concatString + jString + ",";
                        //Added to DB
                        Date nowDate = new Date();
                        List<Alldata> alldataList = alldataService.getSpecificAllData(mapping.get(0).getId(), orgunits, dataset, String.valueOf(period));
                        if (alldataList.size() > 0) {
                            Alldata alldata = alldataList.get(0);
                            alldata.setValue(output);
                            alldata.setModified_by(1);
                            alldata.setModified_on(nowDate);
                            alldataService.save(alldata);
                        } else {
                            Alldata alldata = new Alldata();
                            alldata.setMid(mapping.get(0).getId());
                            alldata.setCategorycombo(cuid);
                            alldata.setDataelement(duid);
                            alldata.setOrgunit(orgunits);
                            alldata.setDataset(dataset);
                            alldata.setPeriod(String.valueOf(period));
                            alldata.setValue(output);
                            alldata.setCreated_by(1);
                            alldata.setCreated_on(nowDate);
                            alldataService.save(alldata);


                        }
                    }else{
                        System.out.println("Missing Mapping "+title+" "+name);
                    }

                    System.out.println(output+" "+period+" "+orgunits+" "+title + " Name "+ name);
                    System.out.println(concatString);


                }

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
            try {
                pushToAPI(Finaldatavalues);
                response="1";
            } catch (KeyStoreException e) {
                response=e.getMessage();
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                response=e.getMessage();
                e.printStackTrace();
            } catch (KeyManagementException e) {
                response=e.getMessage();
                e.printStackTrace();
            }

            Finaldatavalues ="";
            concatString2="";
            concatString="";
            jStringTwo="";

        }
        return   response;
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
        System.out.println(statusLine.getStatusCode());
        resposecode = statusLine.getStatusCode();
        System.out.println(resposecode);
    }

}