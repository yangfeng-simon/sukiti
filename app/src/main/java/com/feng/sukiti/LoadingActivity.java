package com.feng.sukiti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.feng.sukiti.dao.emotions.EmotionsDao;
import com.feng.sukiti.dao.login.LoginDao;
import com.feng.sukiti.module.home.HomeActivity;
import com.feng.sukiti.module.login.WebLoginActivity;
import com.feng.sukiti.utils.Utility;

/**
 * Created by Simon on 2018-3-7.
 */

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginDao loginDao = LoginDao.getInstance(this);
        EmotionsDao.getInstance();

        if (needsLogin(loginDao)) {
            loginDao.logout();
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, WebLoginActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, HomeActivity.class);
//            i.putExtra(Intent.EXTRA_INTENT,getIntent().getIntExtra(Intent.EXTRA_INTENT,0));
            startActivity(i);
            finish();
        }
    }

    private boolean needsLogin(LoginDao login) {
        return login.getAccessToken() == null || Utility.isTokenExpired(login.getExpireDate());
    }
}
