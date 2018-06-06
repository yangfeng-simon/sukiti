package com.feng.sukiti.dao.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.feng.sukiti.dao.OkHttpClientUtility;
import com.feng.sukiti.dao.UrlConstants;
import com.feng.sukiti.dao.WeiboParameters;
import com.feng.sukiti.dao.request.BaseRequestDao;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Simon on 2018-3-7.
 */

public class LoginDao {
    private static Context mContext;

    private static LoginDao mInstance;

    private SharedPreferences mPrefs;

    private String mAccessToken;
    private String mUid;
    private long mExpireDate;

    private ArrayList<String> mNames = new ArrayList<String>();
    private ArrayList<String> mTokens = new ArrayList<String>();
    private ArrayList<Long> mExpireDates = new ArrayList<Long>();

    public  static LoginDao getInstance(Context context){
        if(mInstance==null){
            mInstance =new LoginDao(context);
        }
        return  mInstance;
    }

    private LoginDao(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        mAccessToken = mPrefs.getString("access_token", null);
        mUid = mPrefs.getString("uid", "");
        mExpireDate = mPrefs.getLong("expires_in", Long.MIN_VALUE);

        if (mAccessToken != null) {
            BaseRequestDao.setAccessToken(mAccessToken);
            OkHttpClientUtility.setAccessToken(mAccessToken);
        }
        parseMultiUser();
    }

    private void parseMultiUser() {
        String str = mPrefs.getString("names", "");
        if (str == null || str.trim().equals(""))
            return;

        mNames.addAll(Arrays.asList(str.split(",")));

        str = mPrefs.getString("tokens", "");
        if (str == null || str.trim().equals(""))
            return;

        mTokens.addAll(Arrays.asList(str.split(",")));

        str = mPrefs.getString("expires", "");
        if (str == null || str.trim().equals(""))
            return;

        String[] s = str.split(",");
        for (int i = 0; i < s.length; i++) {
            mExpireDates.add(Long.valueOf(s[i]));
        }

        if (mTokens.size() != mNames.size() ||
                mTokens.size() != mExpireDates.size() ||
                mExpireDates.size() != mNames.size()) {
            mNames.clear();
            mTokens.clear();
            mExpireDates.clear();
        }
    }

    public void login(String token, String expire) {
        mAccessToken = token;
        BaseRequestDao.setAccessToken(mAccessToken);
        OkHttpClientUtility.setAccessToken(mAccessToken);
        mExpireDate = System.currentTimeMillis() + Long.valueOf(expire) * 1000;
        mUid = getUidByToken();

        Log.d("yangfeng","------------login success");

//        登录暂时不需要，先注释
//        GroupDao groupDao=new GroupDao(mContext);
//        groupDao.getGroups();
//        groupDao.cache();



    }

    public void logout() {
        mAccessToken = null;
        mExpireDate = Long.MIN_VALUE;
        mPrefs.edit().remove("access_token").remove("expires_in").remove("uid").commit();
    }

    public void cache() {
        mPrefs.edit().putString("access_token", mAccessToken)
                .putLong("expires_in", mExpireDate)
                .putString("uid", mUid)
                .commit();
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public long getExpireDate() {
        return mExpireDate;
    }

    public static String getUidByToken() {
        try {

            String json = OkHttpClientUtility.doGetRequstWithAceesToken(UrlConstants.GET_UID, new WeiboParameters());
            return new JSONObject(json).optString("uid");
        } catch (Exception e) {
            return null;
        }
    }
}
