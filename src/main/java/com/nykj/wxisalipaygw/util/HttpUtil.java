package com.nykj.wxisalipaygw.util;

import com.nykj.wxisalipaygw.constants.GlobalConstants;
import com.nykj.wxisalipaygw.constants.StatusCodeConstants;
import com.nykj.wxisalipaygw.exception.ApiCallException;
import com.nykj.wxisalipaygw.exception.ArgumentException;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StringUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * Created by Verson on 2016/5/13.
 */
public class HttpUtil {
    /**
     * HTTP GET 请求
     * @param url
     * @param paramsJson
     * @param contentType
     * @return
     * @throws Exception
     */
    public static String httpGet(String url,JSONObject paramsJson,String contentType) throws Exception{
        if(StringUtils.isEmpty(url)){
            throw new ArgumentException(StatusCodeConstants.ARGUMENT_EXCEPTION,"请求的url为空");
        }

        StringBuilder combineUrl = new StringBuilder(url);

        //参数组装
        if(paramsJson != null){
            Iterator it = paramsJson.keys();
            String key = null;
            if(url.lastIndexOf("?") != -1 && it.hasNext()){
                combineUrl.append("?").append(key).append("=").append(paramsJson.getString(key));
            }
            while(it.hasNext()){
                key = (String)it.next();
                combineUrl.append("&").append(key).append("=").append(paramsJson.getString(key));
            }
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(combineUrl.toString());
        if(!StringUtils.isEmpty(contentType)){
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE,contentType);
        }
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String resContent = null;
        try {
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                throw new ApiCallException(StatusCodeConstants.API_CALL_EXCEPTION,"GET接口调用失败");
            }
            resContent = extractContent(response.getEntity());
        } finally {
            response.close();
            httpGet.abort();
        }
        return resContent;
    }

    /**
     * HTTP POST 请求
     * @param url
     * @param paramsJson
     * @param contentType
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, JSONObject paramsJson,String contentType) throws Exception{
        if(StringUtils.isEmpty(url)){
            throw new ArgumentException(StatusCodeConstants.ARGUMENT_EXCEPTION,"请求的url为空");
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if(!StringUtils.isEmpty(contentType)){
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE,contentType);
        }
        if(paramsJson != null){
            httpPost.setEntity(new StringEntity(paramsJson.toString()));
        }
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String resContent = null;
        try {
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                throw new ApiCallException(StatusCodeConstants.API_CALL_EXCEPTION,"POST接口调用失败");
            }
            resContent = extractContent(response.getEntity());
        } finally {
            response.close();
            httpPost.abort();
        }
        return resContent;
    }

    /**
     * 获取输入流内容
     * @param entity
     * @return
     * @throws Exception
     */
    public static String extractContent(HttpEntity entity) throws Exception{
        if(entity == null){
            return null;
        }
        InputStream is = entity.getContent();

        //注意，此处设置解析消息体的字符集编码，如响应端未设置内容编码，则默认使用UTF-8编码
        String contentEncoding = GlobalConstants.CHARSET_UTF8;
        ContentType contentType = ContentType.get(entity);
        if(contentType != null){
            Charset charset = contentType.getCharset();
            contentEncoding = charset.displayName();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is, contentEncoding));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = br.readLine()) != null){
                sb.append(line);
            }
        } finally {
            is.close();
        }
        return sb.toString();
    }
}