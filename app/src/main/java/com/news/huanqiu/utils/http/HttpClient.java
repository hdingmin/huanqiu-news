package com.news.huanqiu.utils.http;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hdingmin on 2017/11/25.
 */

public class HttpClient {
    private ExecutorService singleThreadPool = null;
    private HttpRequestListener httpRequestListener;
    public HttpClient()
    {
        singleThreadPool= Executors.newSingleThreadExecutor();
    }
    public void postAsync(String url,File file,HttpRequestListener listener)
    {
    }
    public void postAsync(String url,Map<String,String> formMap,HttpRequestListener listener)
    {
        String body="";
        Iterator<Map.Entry<String,String>> iterator=formMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String,String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            body+=key+"="+value+"&";
        }
        body = body.substring(0,body.length()-1);
        postAsync(url,body,listener);
    }
    public void postAsync(final String url,final String body,final HttpRequestListener listener) {
        Runnable runnable  = new Runnable() {
            @Override
            public void run() {
                HttpResponse response = new HttpResponse();
                try
                {
                    byte[] bytes = body.toString().getBytes();
                    URL urlPost = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) urlPost.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setFixedLengthStreamingMode(bytes.length);
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    OutputStream out = connection.getOutputStream();
                    try{
                        out.write(bytes);
                    }finally {
                        close(out);
                    }
                    InputStream in =  connection.getInputStream();
                    String content = getString(in);
                    response.setContent(content);
                    response.setState(connection.getResponseCode());
                    if(listener!=null)
                        listener.success(response);
                }catch (Exception ex)
                {
                    if(listener!=null)
                        listener.fail(ex);
                }
            }
        };
        singleThreadPool.execute(runnable);
    }
    protected String getString(InputStream stream) throws IOException {
        String newLine;
        if (stream == null) {
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        do {
            newLine = reader.readLine();
            if (newLine != null)
                content.append(newLine).append('\n');
        } while (newLine != null);
        if (content.length() > 0) {
            content.setLength(content.length() - 1);
        }
        return content.toString();
    }
    protected void close(Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (IOException e) {

        }
    }
}
