package com.feng.sukiti.dao.timeline;

import android.database.Cursor;

import com.feng.sukiti.model.BaseListModel;
import com.feng.sukiti.utils.Constants;

/**
 * Created by Simon on 2018-3-7.
 */

public interface ITimelineBaseDao {
    void loadFromCache();
    void cache();
    void load(boolean isRefresh);
    Cursor query();
    BaseListModel getList();
    Constants.LOADING_STATUS getStatus();
}
