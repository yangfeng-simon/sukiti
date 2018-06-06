package com.feng.sukiti.model;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by Simon on 2018-3-7.
 */

public abstract class BaseListModel<I, L> implements Parcelable {
    public int total_number = 0;
    public long previous_cursor = 0, next_cursor = 0;

    public abstract int getSize();
    public abstract I get(int position);
    public abstract List<? extends I> getList();

    /*
      @param toTop If true, add to top, else add to bottom
      @param values All values needed to be added
    */
    public abstract void addAll(boolean toTop, L values);

    @Override
    public L clone() {
        try {
            BaseListModel<I, L> ret = this.getClass().newInstance();
            ret.addAll(false, (L) this);
            return (L) ret;
        } catch (Exception e) {
            return null;
        }
    }
}
