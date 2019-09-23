package com.test.dataprovider;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import testCase.ReadFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ExcelUtils {

    public static void main(String[] args) throws Exception {
        Object[][] objects = ExcelUtils.getData(ReadFile.class.getClassLoader().getResource("testCase.xls").getPath(), "Sheet1");

        System.out.println(Arrays.deepToString(objects));
        for (Object[] data : objects) {
            for (Object datas : data) {
                System.out.print(datas + "\t");

            }
            System.out.println();
        }


    }


    public static Object[][] getData(String excelfilepath,String sheetname) throws Exception {


        FileInputStream fileInputStream=new FileInputStream(new File(excelfilepath));

        XSSFWorkbook workbook = null;

        HSSFWorkbook workbook1 =null;
        Sheet sheet=null;
        String filextensionName=excelfilepath.substring(excelfilepath.indexOf("."));


        System.out.println("文件:"+filextensionName);
        if (filextensionName.endsWith(".xlsx")) {

//            workbook=new XSSFWorkbook(fileInputStream);
            workbook=new XSSFWorkbook(fileInputStream);
            sheet=workbook.getSheet(sheetname);
        }else if (filextensionName.endsWith(".xls")) {

            workbook1=new HSSFWorkbook(fileInputStream);
            sheet=workbook1.getSheet(sheetname);
        }else System.out.println("excel文件错误");



        int rowcount=sheet.getLastRowNum()-sheet.getFirstRowNum();


        List<String[]> records=new ArrayList<>();


        for (int i = 1; i <=rowcount; i++) {

            Row row=sheet.getRow(i);

            String fields[]=new String[row.getLastCellNum()];

            String ifrun="y";


            switch (ifrun) {
                case ("y"):

                    System.out.println(
                            "No:" + row.getRowNum() + "Row" + "\t" + "casenumber:" + "\t" + row.getCell(0) + "will be tested");

                    for (int j = 0; j < row.getLastCellNum(); j++) {

                        row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
                        fields[j] = row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();


                    }

                    records.add(fields);

                    break;

                case ("n"):
                    System.out.println(
                            "No:" + row.getRowNum() + "Row"+ "skip test" + "\t" + "casenumuber:" +"\t"+ row.getCell(0) + "Not Run");


                    break;



                case ("pass"):
                    System.out.println(
                            "No:" + row.getRowNum() + "Row"+ "skip test" + "\t" + "casenumuber:" +"\t"+ row.getCell(0) + "Not Run");


                    break;




                case ("fail"):

                    System.out.println("No:" + row.getRowNum() + "Row" + "\t" + "casenumuber:" + "\t"+ row.getCell(0)
                            + "Executed(failed), please reset");

                    break;
                default:
                    System.out.println("No:" + row.getRowNum() + "Row" + "\t" + "casenumuber:" + "\t"+ row.getCell(0)
                            + "Execution result design error, please check");
                    break;
            }


        }


        Object[][] results=new String[records.size()][];

        for (int i = 0; i < results.length; i++) {
            results[i]=records.get(i);
        }


        return results;

    }





}