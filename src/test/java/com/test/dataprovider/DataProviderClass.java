package com.test.dataprovider;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;


public class DataProviderClass {


    public static  String excelpath;


    public String getExcelpath() {
        return excelpath;
    }


    public static  void setExcelpath(String excelpath) {
        DataProviderClass.excelpath = excelpath;
    }


    @DataProvider(name="CommonData")
    public Object[][] GetTestData(Method method) throws Exception {
        System.out.println(1);

        Object[][] result=ExcelUtils.getData(getExcelpath(), method.getName());


        return result;

    }









}