package com.feng.sukiti.dao;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.feng.sukiti.BuildConfig.DEBUG;

/**
 * Created by Simon on 2018-3-7.
 */

public class OkHttpClientUtility {
    private static final String TAG = OkHttpClientUtility.class.getSimpleName();

    private static String mAccessToken;

    private final static OkHttpClient client = new OkHttpClient();

    public static void setAccessToken(String token) {
        mAccessToken = token;
    }

    public static String doGetRequstWithAceesToken(String url, WeiboParameters params) throws IOException {
        params.put("access_token", mAccessToken);
        return doGetRequst(url,params);
    }

    public  static String doGetRequst(String url ,WeiboParameters param) throws IOException {
        String send=param.encode();
        url=url+"?"+send;
        return  doGetRequst(url);
    }

    public static String doGetRequst(String url) throws IOException {

        if (DEBUG) {
            Log.i(TAG, url);
        }
        Log.d(TAG,"url-----"+url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();


        if (response.isSuccessful()) {

            String result=response.body().string();
            Log.d(TAG,"result-----"+url);
            if (DEBUG) {
                Log.i(TAG, result);
            }
            return result;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
