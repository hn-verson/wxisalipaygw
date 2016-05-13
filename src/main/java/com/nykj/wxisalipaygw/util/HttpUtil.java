package com.nykj.wxisalipaygw.util;

import com.nykj.wxisalipaygw.constants.GlobalConstants;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Verson on 2016/5/13.
 */
public class HttpUtil {
    public static String httpGet(String url) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);
        StringBuilder sb = new StringBuilder();
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(instream, GlobalConstants.CHARSET_UTF8));
                String line;
                try {
                    while((line = br.readLine()) != null){
                        sb.append(line);
                    }
                } finally {
                    instream.close();
                }
            }
        } finally {
            response.close();
            httpget.abort();
        }
        return sb.toString();
    }
}