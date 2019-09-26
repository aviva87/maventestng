package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.Map;

public class HttpUtil {


    /**
     * Get方法
     */
    public static  String get(String url, String paramStr) {
        String result = null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpGet httpGet = new HttpGet(url);

                client = HttpClients.createDefault();
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                return result;
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
        return result;
    }

    /**
     * Post发送form表单数据
     */
    public String postWithForm(String url, List<BasicNameValuePair> list) {
        String result = null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));

                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                return result;
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
        return result;
    }

    /**
     * Post发送json数据
     */
    public static String postWithJson(String url, Map data) {
        String result = null;
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                HttpPost httpPost = new HttpPost(url);
                if(data.containsKey("AccessToken")){
                    httpPost.setHeader("AccessToken",data.get("AccessToken").toString());
                   data.remove("AccessToken");
                }
                if(data.containsKey("GATEWAY-TOKEN")){
                    httpPost.setHeader("GATEWAY-TOKEN",data.get("GATEWAY-TOKEN").toString());
                    data.remove("GATEWAY-TOKEN");
                }
                //httpPost.setHeader("Accesstoken",data.get("Accesstoken").toString());
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(data),
                        ContentType.create("text/json", "UTF-8")));

                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                return result;
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
        return  result;
    }
}