package com.feng.sukiti.module.home;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feng.sukiti.R;
import com.feng.sukiti.dao.OkHttpClientUtility;
import com.feng.sukiti.dao.UrlConstants;
import com.feng.sukiti.dao.WeiboParameters;
import com.feng.sukiti.model.MessageListModel;
import com.feng.sukiti.model.MessageModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Simon on 2018-6-21.
 */

public class HomeWeiboFragment extends Fragment {
    private Context mContext;
    List<? extends MessageModel> messageModelList;
    HomeWeiboAdapter mHomeWeiboAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            mHomeWeiboAdapter = new HomeWeiboAdapter(messageModelList);
            mRecyclerView.setAdapter(mHomeWeiboAdapter);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        //获取最后一个可见view的位置
                        int lastItemPosition = linearManager.findLastVisibleItemPosition();
                        //获取第一个可见view的位置
                        int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                        if (foodsArrayList.get(firstItemPosition) instanceof Foods) {
//                            int foodTypePosion = ((Foods) foodsArrayList.get(firstItemPosition)).getFood_stc_posion();
//                            FoodsTypeListview.getChildAt(foodTypePosion).setBackgroundResource(R.drawable.choose_item_selected);
//                        }
                        Log.d("position","---"+lastItemPosition + "   " + firstItemPosition);
                    }

                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if(mContext != null){
            Log.d("hwf", "context ------ is null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_weibo, null);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView = (RecyclerView)getView().findViewById(R.id.home_weibo_recyclerview);

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.home_divider_item_decoration));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //每条微博就是一个RecyclerView的子控件，所有可能的元素都放在控件中，实际显示时没有的元素就设置为gone
        new Thread(new Runnable() {
            @Override
            public void run() {
                WeiboParameters params = new WeiboParameters();
                params.put("count",  100);
                params.put("page", 1);

                try {
                    String json = OkHttpClientUtility.doGetRequstWithAceesToken(UrlConstants.HOME_TIMELINE, params);
                    MessageListModel messageListModel = new Gson().fromJson(json, MessageListModel.class);
                    messageListModel.spanAll(mContext);
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
