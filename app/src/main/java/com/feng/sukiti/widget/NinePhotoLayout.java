package com.feng.sukiti.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feng.sukiti.GlideApp;
import com.feng.sukiti.model.MessageModel;

import java.util.List;

/**
 * Created by Simon on 2018-6-12.
 */

public class NinePhotoLayout extends ViewGroup {
    private int blankWidth = 8;
    private List<MessageModel.PictureUrl> pictureUrls;

    public NinePhotoLayout(Context context){
        super(context);
    }

    public NinePhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NinePhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int count = getChildCount();
        for (int i = 0; i < count; i++){
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
        if(count < 1){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
//        if(pictureUrls != null && !pictureUrls.isEmpty()){
//            count = pictureUrls.size();
//        }
        switch (count){
            case 1:
                View childView = getChildAt(0);
                setMeasuredDimension(widthSpecSize, childView.getMeasuredHeight());
                break;
            case 2:
                setMeasuredDimension(widthMeasureSpec, (widthMeasureSpec-blankWidth)/2);
                break;
            case 4:
                setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
                break;
            default:
                setMeasuredDimension(widthMeasureSpec, count/3 * (widthMeasureSpec-2*blankWidth)/3+(count/3-1)*blankWidth);
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        if(count < 1){
            return;
        }
//        if(pictureUrls != null && !pictureUrls.isEmpty()){
//            count = pictureUrls.size();
//        }
        switch (count){
            case 1:
                View child = getChildAt(0);
                child.layout(0,0, child.getMeasuredWidth(), child.getMeasuredHeight());
//                GlideApp.with(getContext()).load(pictureUrls.get(0).getThumbnail()).into((ImageView)child);
                break;
            case 2:
                getChildAt(0).layout(0, 0, (right-blankWidth)/2, (right-blankWidth)/2);
//                GlideApp.with(getContext()).load(pictureUrls.get(0).getThumbnail()).into((ImageView)getChildAt(0));
                getChildAt(1).layout((right-blankWidth)/2+blankWidth, 0, right, (right-blankWidth)/2);
//                GlideApp.with(getContext()).load(pictureUrls.get(1).getThumbnail()).into((ImageView)getChildAt(1));
                break;
            case 4:
                for(int j = 0; j < count; j++){
                    getChildAt(j).layout(j%2 * ((right-blankWidth)/2 + blankWidth), j/2 * ((right-blankWidth)/2 + blankWidth) ,j%2 * ((right-blankWidth)/2 + blankWidth) + (right-blankWidth)/2, j/2 * ((right-blankWidth)/2 + blankWidth) + (right-blankWidth)/2);
//                    GlideApp.with(getContext()).load(pictureUrls.get(j).getThumbnail()).into((ImageView)getChildAt(j));
                }
                break;
            default:
                for(int j = 0; j < count; j++){
                    getChildAt(j).layout(j%3 * ((right-2*blankWidth)/3 + blankWidth), j/3 * ((right-2*blankWidth)/3 + blankWidth) ,j%3 * ((right-2*blankWidth)/3 + blankWidth) + (right-2*blankWidth)/3, j/3 * ((right-2*blankWidth)/3 + blankWidth) + (right-2*blankWidth)/3);
//                    GlideApp.with(getContext()).load(pictureUrls.get(j).getThumbnail()).into((ImageView)getChildAt(j));
                }
                break;
        }
    }

    public void setPictureUrls(List<MessageModel.PictureUrl> urls){
        pictureUrls = urls;

        if(pictureUrls != null && !pictureUrls.isEmpty()){
//            for(MessageModel.PictureUrl pictureUrl : pictureUrls) {
//                ImageView imageView = new ImageView(getContext());
//                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                addView(imageView, layoutParams);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                GlideApp.with(getContext()).load(pictureUrl).into(imageView);
//            }
            if(pictureUrls.size() < 10) {
                for(int i = 0; i < pictureUrls.size(); i++){
                    GlideApp.with(getContext()).load(pictureUrls.get(i).getThumbnail()).into((ImageView)getChildAt(i));
                }
//                requestLayout();
            }
        }
    }
}
