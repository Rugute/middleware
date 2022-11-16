package com.middleware.controller;


import com.middleware.model.Mapping;
import com.middleware.model.Facilities;
import com.middleware.services.FacilitiesService;
import com.middleware.services.MappingService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.middleware.controller.DashboardController.numericMonths;

public class EngineProcessor {
    @Autowired
    public MappingService mappingService;
    @Autowired
    public FacilitiesService facilitiesService;

    public int[] uploadcovid (String destFileName, File destFile, String dataset) throws IOException, JSONException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

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
        for(int x =2;x<=totalRowNum-1;x++) {
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

            for (int c = 3; c <= numberOfCells - 1; c++) {
                Cell cell = currentRow.getCell(c);
                String header = headerRow.getCell(c).getStringCellValue();
                //  System.out.println("Hello Rugute Header value " + header );
                if(county.equals("")){
                    county =currentRow.getCell(3).getStringCellValue();
                    System.out.println("My Count is this "+county);
                     //Facilities facilities = facilitiesService.getByFacilityNameLike(county);
                    List<Facilities> facilities = facilitiesService.searchByFnameLike("county");

                    int xg = facilities.size();
                  // System.out.println("Counnty Size is "+ xg);
                    countyuuid = facilities.get(0).getOrgUnitUID();
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
                        List<Mapping> mappings =   mappingService.getBycategorycombospecificID(String.valueOf(cindex));
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
           // pushToAPI(Finaldatavalues);
            Finaldatavalues ="";
            concatString2="";
            concatString="";
            jStringTwo="";

        }
        return new int[] {a1,a2,a3,a4};

       // System.out.println("Hello Rugute rows"+ totalRowNum);

    }
}
