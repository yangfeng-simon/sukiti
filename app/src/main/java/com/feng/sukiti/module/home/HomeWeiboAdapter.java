package com.feng.sukiti.module.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feng.sukiti.GlideApp;
import com.feng.sukiti.R;
import com.feng.sukiti.model.MessageModel;
import com.feng.sukiti.model.UserModel;
import com.feng.sukiti.model.VisibleModel;
import com.feng.sukiti.utils.Utility;
import com.feng.sukiti.widget.NinePhotoLayout;
import com.feng.sukiti.widget.TextViewFixTouchConsume;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Simon on 2018-5-21.
 */

public class HomeWeiboAdapter extends RecyclerView.Adapter<HomeWeiboAdapter.ViewHolder> {
    List<? extends MessageModel> weiboList;
    Context mContext;

    public HomeWeiboAdapter(List<? extends MessageModel> messageModels){
        weiboList = messageModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载布局文件
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_weibo_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MessageModel msg = weiboList.get(position);
        holder.mContentTextView.setText(msg.span);
        holder.mContentTextView.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
        String source = TextUtils.isEmpty(msg.source) ? "" : Utility.dealSourceString(msg.source);
        holder.mSourceTextView.setText(source);
        holder.mNicknameTextView.setText(weiboList.get(position).user.name + "");
        SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        String x = weiboList.get(position).created_at;
        Date date = new Date();
        try {
            date = sdf1.parse(x);
        }catch (Exception e){

        }
        holder.mTimeTextView.setText(Utility.getTimeFormatText(date));
        GlideApp.with(mContext)
                .load(weiboList.get(position).user.avatar_large)
                .circleCrop()
                .into(holder.mAvatarImageView);
        List<MessageModel.PictureUrl> pictureUrls = weiboList.get(position).pic_urls;
//        Log.d("yf", "pic urls---" + pictureUrls);
        if(pictureUrls != null && !pictureUrls.isEmpty()){
            for(MessageModel.PictureUrl pictureUrl : pictureUrls) {
                Log.d("yf", "pic urls---" + pictureUrl.getThumbnail());
                ImageView imageView = new ImageView(mContext);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.mNinePhotoLayout.addView(imageView, layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                GlideApp.with(mContext).load(pictureUrl.getThumbnail()).into(imageView);
//                holder.mNinePhotoLayout.requestLayout();
            }
        }
//        holder.mNinePhotoLayout.setPictureUrls(weiboList.get(position).pic_urls);
//        holder.mContentTextView.setText(weiboList.get(position).text);
    }

    @Override
    public int getItemCount() {
        if(weiboList != null){
            return weiboList.size();
        }else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mContentTextView;
        public TextView mNicknameTextView;
        public ImageView mAvatarImageView;
        public TextView mTimeTextView;
        public TextView mSourceTextView;
//        public NinePhotoLayout mNinePhotoLayout;
        public LinearLayout mNinePhotoLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mNicknameTextView = (TextView) itemView.findViewById(R.id.home_weibo_nickname);
            mContentTextView = (TextView) itemView.findViewById(R.id.home_weibo_content);
            mAvatarImageView = (ImageView) itemView.findViewById(R.id.home_weibo_avatar);
            mTimeTextView = (TextView) itemView.findViewById(R.id.home_weibo_time);
            mSourceTextView = (TextView) itemView.findViewById(R.id.home_weibo_source);
            mNinePhotoLayout = (LinearLayout) itemView.findViewById(R.id.home_nine_photo_layout);
        }
    }
}
