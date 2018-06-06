package com.feng.sukiti.module.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.feng.sukiti.MainActivity;
import com.feng.sukiti.R;
import com.feng.sukiti.dao.UrlConstants;
import com.feng.sukiti.dao.login.LoginDao;
import com.feng.sukiti.module.home.HomeActivity;
import com.feng.sukiti.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.feng.sukiti.BuildConfig.DEBUG;

/**
 * Created by Simon on 2018-3-7.
 */

public class WebLoginActivity extends AppCompatActivity {
    public static final String TAG = WebLoginActivity.class.getSimpleName();

    public static final String WEICO_SCOPE = "email,direct_messages_read,direct_messages_write," +
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
    public static final String WEICO_CLIENT_ID = "211160679";
    public static final String WEICO_REDIRCT_URL = "http://oauth.weico.cc";
    public static final String WEICO_APP_KEY = "1e6e33db08f9192306c4afa0a61ad56c";
    public static final String WEICO_PACKNAME = "com.eico.weico";

    @BindView(R.id.web_login)
    WebView mWebView;

//    @BindView(R.id.toolbar)
//    Toolbar mToolBar;

    private boolean isDoingLogin = false;

    private LoginDao mLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_login);

        ButterKnife.bind(this);

        mLogin = LoginDao.getInstance(this);

//        setSupportActionBar(mToolBar);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(new MyWebViewClient());

        mWebView.loadUrl(getOauthLoginPage());

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (isUrlRedirected(url)) {
                view.stopLoading();
                Log.d(TAG, "shouldOverrideUrlLoading...");
                handleRedirectedUrl(url);
            } else {
                view.loadUrl(url);
            }

            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!url.equals("about:blank") && isUrlRedirected(url)) {
                view.stopLoading();
                Log.d(TAG, "onPageStarted...");
                handleRedirectedUrl(url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }

    }

    private void handleRedirectedUrl(String url) {
        Log.d(TAG, "handleRedirectedUrl...");

        if (!url.contains("error")) {
            int tokenIndex = url.indexOf("access_token=");
            int expiresIndex = url.indexOf("expires_in=");
            String token = url.substring(tokenIndex + 13, url.indexOf("&", tokenIndex));
            String expiresIn = url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex));

            if (DEBUG) {
                Log.d(TAG, "url = " + url);
                Log.d(TAG, "token = " + token);
                Log.d(TAG, "expires_in = " + expiresIn);
            }

            if (!isDoingLogin)
                new LoginTask().execute(token, expiresIn);


        } else {
            showLoginFail();
        }
    }

    private class LoginTask extends AsyncTask<String, Void, Long>
    {
        private ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDoingLogin=true;
            progDialog = new ProgressDialog(WebLoginActivity.this);
            progDialog.setMessage(getResources().getString(R.string.loadding_user_data));
            progDialog.setCancelable(false);
            progDialog.show();
        }

        @Override
        protected Long doInBackground(String... params) {
            if (DEBUG) {
                Log.d(TAG, "doInBackground...");
            }
            mLogin.login(params[0], params[1]);
            return mLogin.getExpireDate();

        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            progDialog.dismiss();
            isDoingLogin=false;

            if ( mLogin.getAccessToken() != null) {
                if (DEBUG) {
                    Log.d(TAG, "Access Token:" + mLogin.getAccessToken());
                    Log.d(TAG, "Expires in:" + mLogin.getExpireDate());
                }
                mLogin.cache();

            } else if (mLogin.getAccessToken() == null) {
                showLoginFail();
                return;
            }
            String msg = String.format(getResources().getString(R.string.expires_in), Utility.expireTimeInDays(result));
            // Expire date
            Toast.makeText(WebLoginActivity.this, msg, Toast.LENGTH_LONG).show();
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(WebLoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

    }

    public static boolean isUrlRedirected(String url) {
        return url.startsWith(WEICO_REDIRCT_URL);
    }

    private void showLoginFail() {
        // Wrong username or password
        new AlertDialog.Builder(WebLoginActivity.this)
                .setMessage(R.string.login_fail)
                .setCancelable(true)
                .create()
                .show();
    }

    public static String getOauthLoginPage() {
        return UrlConstants.OAUTH2_ACCESS_AUTHORIZE + "?" + "client_id=" + WEICO_CLIENT_ID
                + "&response_type=token&redirect_uri=" + WEICO_REDIRCT_URL
                + "&key_hash=" + WEICO_APP_KEY + (TextUtils.isEmpty(WEICO_PACKNAME) ? "" : "&packagename=" + WEICO_PACKNAME)
                + "&display=mobile" + "&scope=" + WEICO_SCOPE;
    }
}
