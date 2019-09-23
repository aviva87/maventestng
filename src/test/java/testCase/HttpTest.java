package testCase;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTest {

    @Test
    public void test1() {
        String uri = "https://gw.zihome.com/link-monitor/v1/data/offlineData";
        String result = HttpUtil.get(uri, "");
        System.out.println(result);
        Assert.assertTrue(result.toLowerCase().contains("\"code\":300"), "get assert");
        String uri1 = "https://gw.zihome.com/link-monitor/v1/data/ratio";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("type", "4");
        String result1 = HttpUtil.postWithJson(uri1, data);
        System.out.println(result1);
    }

    /**
     * Post发送form表单数据
     */
    @Test
    public void test2() {
        String uri = "https://gw.zihome.com/link-monitor/v1/data/offlineData";
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                // 创建一个提交数据的容器
                List<BasicNameValuePair> parames = new ArrayList<>();
                parames.add(new BasicNameValuePair("code", "001"));
                parames.add(new BasicNameValuePair("name", "测试"));

                HttpPost httpPost = new HttpPost(uri + "/test1");
                httpPost.setEntity(new UrlEncodedFormEntity(parames, "UTF-8"));

                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Integer ONE = 1;

    /*  public static void main(String[] args) {

         String path = ReadFile.class.getClassLoader().getResource("test.json").getPath();
         String s = readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);
         System.out.println("url"+jobj.get("url"));
         System.out.println("hid"+jobj.get("hid"));
         String sno = (String) jobj.get("sno");
         String uid = (String) jobj.get("uid");
         String rid = (String) jobj.get("rid");
         String country = (String) jobj.get("date");

         System.out.println("sno :" + sno);
         System.out.println("uid :" + uid);
         System.out.println("rid :" + rid);

        JSONArray links = jobj.getJSONArray("links");

         for (int i = 0 ; i < links.size();i++){
             JSONObject key1 = (JSONObject)links.get(i);
             String name = (String)key1.get("name");
             String url = (String)key1.get("url");
             System.out.println(name);
             System.out.println(url);
         }
     }
 */
}