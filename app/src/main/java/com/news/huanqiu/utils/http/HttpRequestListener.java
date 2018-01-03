package com.news.huanqiu.utils.http;

/**
 * Created by hdingmin on 2017/11/28.
 */

public interface HttpRequestListener {
    void success(HttpResponse response);
    void fail(Throwable tb);
}
