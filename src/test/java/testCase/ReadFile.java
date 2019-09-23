package testCase;

import com.alibaba.fastjson.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.HttpUtil;

import java.io.*;
import java.util.Map;

public class ReadFile {


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
            Map json = (Map) JSONObject.parse(jsonStr);
            System.out.println("这个是用JSONObject类的parse方法来解析JSON字符串!!!");
            String url = (String) json.get("url");
            json.remove("url");
            String result = HttpUtil.postWithJson(url, json);
            System.out.println(result);
            Assert.assertTrue(result.toLowerCase().contains("\"code\":300"));

        } catch (IOException e) {
            e.printStackTrace();

        }


    }
    @Test
    public void readJsonFile1() {
        String path = ReadFile.class.getClassLoader().getResource("test1.json").getPath();

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
            Map json = (Map) JSONObject.parse(jsonStr);
            System.out.println("这个是用JSONObject类的parse方法来解析JSON字符串!!!");
            String url = (String) json.get("url");
            json.remove("url");
            String result = HttpUtil.postWithJson(url, json);
            System.out.println(result);
            Assert.assertTrue(result.toLowerCase().contains("\"code\":200"));

        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}