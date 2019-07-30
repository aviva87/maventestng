package com.test.dataprovider;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {


    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";

    private HttpUtils() {
    }

    public static String doGet(String url, Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
            httpGet.addHeader("Cookie", "JSESSIONID=0316E3729E6DC9CB642F1C8865259EF9; uid=tl5BUOPBCdY5O0YT78U96K-46YIXa4pXK-oYSSWgGBftasAkH26dITFAOYu0VVdnyTFfKCvddiTx10glLweCHWq0As-a44wWotNBa15mB04kjPEdWPw7t_L-8hIXzO0N46q6lKWMZtcNcthwFhifM2Tv_VVqszP42wvJIWO39e1ALjYPnyq6ygBtp5PuoDfLE_5XPO3ZvxV-H2F7cdTj");
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGet(String url) {
        return doGet(url, null);
    }

    public static String doPost(String url, Map<String, Object> param) {
        String cookie = "pgv_pvi=5539610624; pgv_si=s4970830848; JSESSIONID=84846CF76273FE321107B3FC333CE7C4; uid=tl5BUOPBCdY5O0YT78U96K-46YIXa4pXK-oYSSWgGBftasAkH26dITFAOYu0VVdnyTFfKCvddiTx10glLweCHWq0As-a44wWotNBa15mB04kjPEdWPw7t_L-8hIXzO0N46q6kKOPb9kMed11FhifM2Tv_VVqszP50g3JJGO39boWc2UNmCzqmgI-qsq5oTfPEPpXP7ndvhZ4FTdyctHg";

        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, (String) param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);
                httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
                httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
//                httpPost.addHeader("Cookie", "pgv_pvi=1828640768; pgv_si=s654112768; JSESSIONID=1461748C7A9EFCE1040B4CDBDB229160; uid=tl5BUOPBCdY5O0YT78U96K-46YIXa4pXK-oYSSWgGBftasAkH26dITFAOYu0VVdnyTFfKCvddiTx10glLweCHWq0As-a44wWotNBa15mB04kjPEdWPw7t_L-8hIXzO0N46q6l66LZt8Kdt9yFhifM2Tv_VVqszP42wvJIWO39eUWJzZckyjumwA4opu0ozLNEqsIbb_YtxB9HmcnJYe2");
                httpPost.addHeader("Cookie",cookie);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String doPostJson(String url, String json, String token_header) throws Exception {
        // 创建Httpclient对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = httpClientBuilder.build();
        CloseableHttpResponse response = null;
        String resultString = "";
        String cookie = "pgv_pvi=1828640768; pgv_si=s654112768; JSESSIONID=62E26AE4193E97E30CD5D9215DC6ADC8; uid=tl5BUOPBCdY5O0YT78U96K-46YIXa4pXK-oYSSWgGBftasAkH26dITFAOYu0VVdnyTFfKCvddiTx10glLweCHWq0As-a44wWotNBa15mB04kjPEdWPw7t_L-8hIXzO0N46q6l6-OatcNddFwFhifM2Tv_VVqszP42wvJIWO39e5HdWdbznm7m1Jpqpq-9GTHQahXPbzWuEJ6FmMjdNfj";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            httpPost.setHeader("HTTP Method", "POST");
            httpPost.setHeader("Connection", "Keep-Alive");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Cookie",cookie);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
            multipartEntityBuilder.addTextBody("page", "1");
            multipartEntityBuilder.addTextBody("limit", "10");
            multipartEntityBuilder.addTextBody("orderNo", "Zs201907290003");
            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);

//            StreamUtils.copyToString(this.getRequest().getInputStream(), StandardCharsets.UTF_8);
            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }


    public static String sendPost(String url, String jsonStr) {
        String result = null;
        // 字符串编码
        StringEntity entity = new StringEntity(jsonStr, Consts.UTF_8);
        // 设置content-type
        entity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        // 防止被当成攻击添加的
        httpPost.addHeader("User-Agent", userAgent);
        // 接收参数设置
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Cookie", "pgv_pvi=1828640768; pgv_si=s654112768; JSESSIONID=1461748C7A9EFCE1040B4CDBDB229160; uid=tl5BUOPBCdY5O0YT78U96K-46YIXa4pXK-oYSSWgGBftasAkH26dITFAOYu0VVdnyTFfKCvddiTx10glLweCHWq0As-a44wWotNBa15mB04kjPEdWPw7t_L-8hIXzO0N46q6l66LZt8Kdt9yFhifM2Tv_VVqszP42wvJIWO39eUWJzZckyjumwA4opu0ozLNEqsIbb_YtxB9HmcnJYe2");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭CloseableHttpResponse
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;

    }
}