package com.BFMe.BFMBuyer.model;

import android.content.Context;

/**
 * 处理数据库
 */
public class Model {

    private Context mContext;
    //用于操作数据的类
    private RecentViewedDAO recentViewedDA;
    private HistorySearchDAO historySearchDAO;
    public static Model model = null;

    private Model(Context context) {
        this.mContext = context;
    }

    public static Model getInstance(Context context) {
        if (model == null) {
            model = new Model(context);
        }
        return model;
    }

    public RecentViewedDAO getRecentViewedDAO() {
        return new RecentViewedDAO(mContext);
    }

    public HistorySearchDAO getHistorySearchDAO() {
        return new HistorySearchDAO(mContext);
    }
}
