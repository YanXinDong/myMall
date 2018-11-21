package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.HelpBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class HelpAdapter extends BaseAdapter {

    private Context mContext;
    private List<HelpBean.HelpCenterNavData> helpCenterNavDatas = new ArrayList<>();

    public HelpAdapter(Context context, ArrayList<HelpBean.HelpCenterNavData> helpList) {
        mContext = context;

        if(helpList != null) {
            helpCenterNavDatas = helpList;
        }

    }

    @Override
    public int getCount() {
        return helpCenterNavDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return helpCenterNavDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHdoler viewHdoler = null;
        if(convertView == null) {
            viewHdoler = new ViewHdoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_help,parent,false);
            viewHdoler.tv_help_topic = (TextView) convertView.findViewById(R.id.tv_help_topic);
            convertView.setTag(viewHdoler);
        }else{
            viewHdoler = (ViewHdoler) convertView.getTag();
        }
        viewHdoler.tv_help_topic.setText(helpCenterNavDatas.get(position).getTitle());
        return convertView;
    }

    class ViewHdoler{

        private TextView tv_help_topic;
    }
}
