package com.BFMe.BFMBuyer.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Description：最近浏览数据处理
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/4/17 12:28
 */

public class HistorySearchDAO {
    private Context mContent;
    private final HistorySearchDB mHelper;
    private final SQLiteDatabase writableDatabase;

    public HistorySearchDAO(Context context) {
        this.mContent = context;
        mHelper = new HistorySearchDB(context);
        writableDatabase = mHelper.getWritableDatabase();
    }

    //添加数据
    public long addHistorySearch(String content){

        ContentValues values = new ContentValues();
        values.put(RecentViewedTable.VIEWED_CONTENT_BOUND,content);
        long insert = writableDatabase.insert(RecentViewedTable.TAB_NAME_BOUND, null, values);
        return insert;
    };
    //清空数据
    public long deleteHistorySearch(){
        return writableDatabase.delete(RecentViewedTable.TAB_NAME_BOUND, null, null);
    }
    //查询数据
    public ArrayList<String> seleteHistorySearch(){
        ArrayList<String>  viewedList = new ArrayList<>();
        SQLiteDatabase readableDatabase = mHelper.getReadableDatabase();
        Cursor query = readableDatabase.query(RecentViewedTable.TAB_NAME_BOUND, null, null, null, null, null, null);
        while(query.moveToNext()){
            String string = query.getString(query.getColumnIndex(RecentViewedTable.VIEWED_CONTENT_BOUND));
            viewedList.add(string);
        }
        query.close();
        return viewedList;
    }

    //删除指定数据
    public long deleteExistSearch(String content){
      return writableDatabase.delete(RecentViewedTable.TAB_NAME_BOUND, RecentViewedTable.VIEWED_CONTENT_BOUND+"=?", new String[]{content});
    }
}
