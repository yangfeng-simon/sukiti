package com.feng.sukiti;

import android.app.Application;

/**
 * Created by Simon on 2018-3-7.
 */

public class SuKiTiApplication extends Application {
    private static SuKiTiApplication mContext;

    public static SuKiTiApplication getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }
}
