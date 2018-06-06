package com.feng.sukiti.dao;

/**
 * Created by Simon on 2018-3-8.
 */

public class UrlConstants {
    public static final String SINA_BASE_URL = "https://api.weibo.com/2/";

    public static final String GET_UID = SINA_BASE_URL + "account/get_uid.json";

    // Login
    public static final String OAUTH2_ACCESS_AUTHORIZE = "https://open.weibo.cn/oauth2/authorize";

    // Statuses
    public static final String HOME_TIMELINE = SINA_BASE_URL + "statuses/home_timeline.json";

    // short url 转换成 long url
    public static final String URL_SHORT = SINA_BASE_URL + "short_url/info.json";
}
