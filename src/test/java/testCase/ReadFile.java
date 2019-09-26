package testCase;

import com.alibaba.fastjson.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.HttpUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReadFile {
    Map json =null;
    @Test
    public void readJsonFile() {
        String path = ReadFile.class.getClassLoader().getResource("test.json").getPath();
        String jsonStr = "";
        try {
            File jsonFile = new File(path);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
             json = (Map) JSONObject.parse(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(dependsOnMethods = "readJsonFile",priority = 0)
    public void controlDeviceByOperCode() {
        if(json.isEmpty()){
            Assert.fail("参数为空");
        }
        String url = "http://homeapi.q.zihome.com/v2/device/controlDeviceByOperCode";
        String result = HttpUtil.postWithJson(url, json);
        System.out.println(result);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));
    }
    @Test(priority = 1)
    public void checkSnoRepeat() {
        if(json.isEmpty()){
        }
        String url = "http://qlink.zihome.com/link-control-record/v1/check/checkSnoRepeat";
        Map map = new HashMap();
        map.put("sno",json.get("sno"));
        map.put("AccessToken","12486CD41ACF4B1B97FF0A30CAFD508F");
        String result = HttpUtil.postWithJson(url, map);
        System.out.println(result);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));
    }
    @Test(priority = 2)
    public void queryMacByDevUuid() {
        if(json.isEmpty()){
        }
        String url = "http://qlink.zihome.com/link-route/v1/device/queryMacByDevUuid";
        Map map = new HashMap();
        map.put("devUuid",json.get("devUuid"));
        String result = HttpUtil.postWithJson(url, map);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));
    }
    @Test(priority = 3)
    public void getDeviceState() {
        if(json.isEmpty()){
            Assert.fail("参数为空");
        }
        String url = "http://qlink.zihome.com/link-snapshot/v1/state/getDeviceState";
        Map map = new HashMap();
        map.put("devId",json.get("devId"));
        map.put("prodTypeId",json.get("prodTypeId"));
        String result = HttpUtil.postWithJson(url, map);
        System.out.println(result);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));
    }
    @Test(priority = 4)
    public void query() {
        if(json.isEmpty()){
        }
        String url = "http://qlink.zihome.com/base-info/tProdOper/query";
        Map map = new HashMap();
        map.put("prodOperCode",json.get("prodOperCode"));
        String result = HttpUtil.postWithJson(url, map);
        System.out.println(result);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));
    }
    @Test(priority = 5)
    public void control() {
        if(json.isEmpty()){
            Assert.fail("参数为空");
        }
        String url = "http://qlink.zihome.com/link-handle-gateway/v1/operate/device/control";
      /* json.remove("command1");
        json.remove("data1");
        json.remove("msgType");*/
        json.put("msgType",json.get("msgType1"));
        String result = HttpUtil.postWithJson(url, json);
        System.out.println(result);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));
    }
    @Test(dependsOnMethods = "readJsonFile",priority = 6)
    public void result() {
        if(json.isEmpty()){
            Assert.fail("参数为空");
        }
        String url = "http://10.216.4.155:8081/v1/gateway/device/control/result";
      /*  json.remove("command");
        json.remove("data");
        json.remove("msgType1");*/
        json.put("GATEWAY-TOKEN","8543f86145cf4033a5cbe96ce4c0eba8");
        json.put("command",json.get("command3"));
        json.put("data",json.get("data1"));
        String result = HttpUtil.postWithJson(url, json);
        System.out.println(result);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));
    }
    @Test(dependsOnMethods = "readJsonFile",priority = 7)
    public void deviceNotify() {
        if(json.isEmpty()){
           Assert.fail("参数为空");
         }
        String url = "http://10.216.4.155:8081/v1/gateway/device/notify";
        /*json.remove("command");
        json.remove("data1");
        json.remove("msgType");*/
        json.put("GATEWAY-TOKEN","8543f86145cf4033a5cbe96ce4c0eba8");
        json.put("msgType",json.get("msgType1"));
        json.put("command",json.get("command1"));
        String result = HttpUtil.postWithJson(url, json);
        System.out.println(result);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));
    }
}