package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.AllSearchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanHailong on 15/10/19.
 */
public class TagAdapter<T> extends BaseAdapter {

    private final Context mContext;
    private final List<T> mDataList;

    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        TextView textView;

        T t = mDataList.get(position);

        if (t instanceof String) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent,false);
            textView = (TextView) view.findViewById(R.id.tv_tag);
            textView.setText((String) t);
        }else if(t instanceof AllSearchBean.KeyWordsBean) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_all_search, parent,false);
            textView = (TextView) view.findViewById(R.id.tv_recently_viewed);
            textView.setText(((AllSearchBean.KeyWordsBean) t).getName());
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent,false);
        }
        return view;
    }

    public void onlyAddAll(List<T> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<T> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }
}
