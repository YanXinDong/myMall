package com.BFMe.BFMBuyer.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Description：最近浏览数据表
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/4/17 14:12
 */

public class RecentViewedDB extends SQLiteOpenHelper {

    public RecentViewedDB(Context context) {
        super(context, "bfme_buyyer", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecentViewedTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
