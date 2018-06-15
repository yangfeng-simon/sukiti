package com.feng.sukiti.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
        switch (count){
            case 1:
                View childView = getChildAt(0);
                setMeasuredDimension(widthSpecSize, childView.getMeasuredHeight());
                break;
            case 2:
                setMeasuredDimension(widthSpecSize, (widthSpecSize-blankWidth)/2);
                break;
            case 4:
                setMeasuredDimension(widthSpecSize, widthSpecSize);
                break;
            default:
                setMeasuredDimension(widthSpecSize, count/3 * (widthSpecSize-2*blankWidth)/3+(count/3-1)*blankWidth + blankWidth); //
                break;
        }
        Log.d("yf", "on measure----" + widthSpecSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        if(count < 1){
            return;
        }
        switch (count){
            case 1:
                View child = getChildAt(0);
                child.layout(0,0, child.getMeasuredWidth(), child.getMeasuredHeight());
                break;
            case 2:
                getChildAt(0).layout(0, 0, (right-blankWidth)/2, (right-blankWidth)/2);
                getChildAt(1).layout((right-blankWidth)/2+blankWidth, 0, right, (right-blankWidth)/2);
                break;
            case 4:
                for(int j = 0; j < count; j++){
                    getChildAt(j).layout(j%2 * ((right-blankWidth)/2 + blankWidth), j/2 * ((right-blankWidth)/2 + blankWidth) ,j%2 * ((right-blankWidth)/2 + blankWidth) + (right-blankWidth)/2, j/2 * ((right-blankWidth)/2 + blankWidth) + (right-blankWidth)/2);
                }
                break;
            default:
                for(int j = 0; j < count; j++){
                    getChildAt(j).layout(j%3 * ((right-2*blankWidth)/3 + blankWidth), j/3 * ((right-2*blankWidth)/3 + blankWidth) ,j%3 * ((right-2*blankWidth)/3 + blankWidth) + (right-2*blankWidth)/3, j/3 * ((right-2*blankWidth)/3 + blankWidth) + (right-2*blankWidth)/3);
                }
                break;
        }
        Log.d("yf", "on layout----" + right);
    }
}
