package com.feng.sukiti.dao.request;

/**
 * Created by Simon on 2018-3-7.
 */

public class BaseRequestDao {
    private static String mAccessToken;

    public static void setAccessToken(String token) {
        mAccessToken = token;
    }
}
