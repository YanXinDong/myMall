package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.AboutBFMeActivity;
import com.BFMe.BFMBuyer.javaBean.MapAdInfoBean;

import java.util.ArrayList;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/2/7 16:05
 */
public class MapInfoAdapter extends BaseAdapter {
    private ArrayList<MapAdInfoBean> mListAddress;
    private Context mContext;
    public MapInfoAdapter(ArrayList<MapAdInfoBean> listAddress, AboutBFMeActivity aboutBFMeActivity) {
        this.mListAddress = listAddress;
        this.mContext = aboutBFMeActivity;

    }

    @Override
    public int getCount() {
        return mListAddress.size();
    }

    @Override
    public Object getItem(int position) {
        return mListAddress.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHdoler viewHdoler = null;
        if(viewHdoler == null) {
            viewHdoler = new ViewHdoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_map,parent,false);
            viewHdoler.title = (TextView) convertView.findViewById(R.id.title);
            viewHdoler.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(viewHdoler);
        }else{
            viewHdoler = (ViewHdoler) convertView.getTag();
        }
        viewHdoler.title.setText(mListAddress.get(position).getTitle());
        viewHdoler.tvAddress.setText(mListAddress.get(position).getAddress());
        return convertView;
    }

    class ViewHdoler{

        private TextView title;
        private TextView tvAddress;



    }
}
