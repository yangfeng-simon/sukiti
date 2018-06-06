package com.feng.sukiti.module.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.feng.sukiti.R;
import com.feng.sukiti.dao.OkHttpClientUtility;
import com.feng.sukiti.dao.UrlConstants;
import com.feng.sukiti.dao.WeiboParameters;
import com.feng.sukiti.model.MessageListModel;
import com.feng.sukiti.model.MessageModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * 首页
 */

public class HomeActivity extends Activity {
    List<? extends MessageModel> messageModelList;
    HomeWeiboAdapter mHomeWeiboAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mHomeWeiboAdapter = new HomeWeiboAdapter(messageModelList);
            mRecyclerView.setAdapter(mHomeWeiboAdapter);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.home_weibo_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.home_divider_item_decoration));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //每条微博就是一个RecyclerView的子控件，所有可能的元素都放在控件中，实际显示时没有的元素就设置为gone
        new Thread(new Runnable() {
            @Override
            public void run() {
                WeiboParameters params = new WeiboParameters();
                params.put("count",  20);
                params.put("page", 1);

                try {
                    String json = OkHttpClientUtility.doGetRequstWithAceesToken(UrlConstants.HOME_TIMELINE, params);
                    MessageListModel messageListModel = new Gson().fromJson(json, MessageListModel.class);
                    messageModelList = messageListModel.getList();
                    mHandler.sendEmptyMessage(0);
                    Log.d("yf", "count-----------"+messageListModel.getList().size());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
