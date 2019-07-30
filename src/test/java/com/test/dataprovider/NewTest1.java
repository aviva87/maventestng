package com.test.dataprovider;

import org.apache.commons.lang.ArrayUtils;
import org.testng.annotations.Test;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeSuite;
public class NewTest1 <T extends Object>{
    static String excelpath="C:\\Users\\chun\\Desktop\\测试用例.xls";

    String  url = "http://aims.t.zihome.com/prodOrder/getOrderList";
    @BeforeSuite
    public  void BeforeSuite() {
        LoginDataProvider.excelpath="C:\\Users\\chun\\Desktop\\测试用例.xls";
    }

    @Test(dataProvider = "GetTestData",dataProviderClass = LoginDataProvider.class)
    public void Sheet1(Object ...params ) {
        Map map = new HashMap();
         map.put("page",params[0]);
         map.put("limit",params[1]);
         map.put("orderNo",params[2]);
        String result = HttpUtils.doPost(url,map);
        System.out.println(result);

        System.out.println("qwertyui");
    }
}