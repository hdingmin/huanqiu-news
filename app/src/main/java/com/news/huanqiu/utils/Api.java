package com.news.huanqiu.utils;


import com.news.huanqiu.bean.DemoInfoBean;
import com.news.huanqiu.utils.http.HttpClient;
import com.news.huanqiu.utils.http.HttpRequestListener;
import com.news.huanqiu.utils.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hdingmin on 2017/11/28.
 */

public class Api {
    private static String HOST="http://10.73.166.128:8888/LingxiClouldMS/api/";

    private static String DEMO_LIST=HOST+"demo/page.do";

    private static HttpClient mHttpClient = new HttpClient();

    public static void getDemoList(int page, int rows,final Callback<PageResult<DemoInfoBean>> callback)
    {
        Map<String,String> map = new HashMap<>();
        map.put("page","1");
        map.put("rows","20");
        mHttpClient.postAsync(DEMO_LIST, map, new HttpRequestListener() {
            @Override
            public void success(HttpResponse response) {
            }

            @Override
            public void fail(Throwable tb) {
            }
        });
    }

    public interface Callback<T>{
        void onSuccess(T result);
        void onFailed(String reason);
    }
}
