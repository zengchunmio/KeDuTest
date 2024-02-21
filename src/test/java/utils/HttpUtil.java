package utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtil {
    /**
     * 发送一个post请求
     * @param url       接口地址
     * @param params    接口参数
     * @return          返回httpResponse
     */
    public static HttpResponse post(String url, String params){
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Authorization","");
        StringEntity body = new StringEntity(params, "utf-8");
        post.setEntity(body);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 发送post请求，设置请求头Authorization
     * @param url       接口地址
     * @param params    接口参数
     * @param token     请求头Authorization
     * @return          返回httpResponse
     */
    public static HttpResponse post(String url, String params,String token){
        //声明一个方法，这个方法就是post方法
        HttpPost post = new HttpPost(url);
        //设置请求头信息 设置header
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Authorization",token);
        //将参数信息添加到方法中
        StringEntity body = new StringEntity(params, "utf-8");
        post.setEntity(body);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 返回响应体Json
     * @param   response
     */
    public static String getResponse(HttpResponse response){
        int statusCode = response.getStatusLine().getStatusCode();
        Header[] allHeaders = response.getAllHeaders();
        HttpEntity entity = response.getEntity();
        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}