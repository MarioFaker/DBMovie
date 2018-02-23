package com.honghui.dbmovie.apivisit;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;

import java.security.KeyStore;

/**
 * Created by lyw on 2018/2/17.
 */

public class ClientApi {
    public static final String Base_Url="https://api.douban.com";
    public static final String url_search="/v2/movie/search";
    public static final String url_toplist = "/v2/movie/top250";
    public static final String url_hotlist = "/v2/movie/in_theaters";
    public static final String url_details = "/v2/movie/subject";

    private static AsyncHttpClient client = null;

    public static synchronized AsyncHttpClient getClient() {
        if (client == null) {
            try {
                client = new AsyncHttpClient();
                client.setTimeout(20000);

                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                client.setSSLSocketFactory(sf);
            } catch (Exception e) {

            }
        }
        return client;
    }
}