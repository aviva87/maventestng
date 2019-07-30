package com.test.dataprovider;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class LoginDataProvider {
    public static  String excelpath;


    public static String getExcelpath() {
        return excelpath;
    }


    public static  void setExcelpath(String excelpath) {
        DataProviderClass.excelpath = excelpath;
    }

    @DataProvider()
    public static Object[][] GetTestData(Method method) throws Exception {


        Object[][] result=ExcelUtils.getData(getExcelpath(), method.getName());

        return result;

    }
}
