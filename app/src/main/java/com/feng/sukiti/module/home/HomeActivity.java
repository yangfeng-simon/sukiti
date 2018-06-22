package com.feng.sukiti.module.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */

public class HomeActivity extends AppCompatActivity {
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

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;


//
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        new MainAdapter(getSupportFragmentManager());
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        AlphaIndicator alphaIndicator = (AlphaIndicator) findViewById(R.id.alphaIndicator);
        alphaIndicator.setViewPager(viewPager);
    }

    private class MainAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private String[] titles = {//
                "第一页",//
                "第二页"};

        public MainAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new HomeWeiboFragment());
            fragments.add(TextFragment.newInstance(titles[1]));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}

//mRecyclerView = (RecyclerView)findViewById(R.id.home_weibo_recyclerview);
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.home_divider_item_decoration));
//        mRecyclerView.addItemDecoration(dividerItemDecoration);
//
//        //每条微博就是一个RecyclerView的子控件，所有可能的元素都放在控件中，实际显示时没有的元素就设置为gone
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                WeiboParameters params = new WeiboParameters();
//                params.put("count",  20);
//                params.put("page", 1);
//
//                try {
//                    String json = OkHttpClientUtility.doGetRequstWithAceesToken(UrlConstants.HOME_TIMELINE, params);
//                    MessageListModel messageListModel = new Gson().fromJson(json, MessageListModel.class);
//                    messageListModel.spanAll(mContext);
//                    messageModelList = messageListModel.getList();
//                    mHandler.sendEmptyMessage(0);
//                    Log.d("yf", "count-----------"+messageListModel.getList().size());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();