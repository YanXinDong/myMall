package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.BFMe.BFMBuyer.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Description：发布话题
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/8 15:24
 */
public class CreateTopicAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList mList = new ArrayList();

    public CreateTopicAdapter(Context context, ArrayList list) {
        this.mContext = context;
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
    }

    @Override
    public int getCount() {
        if(mList.size()<=4) {
            return mList.size();
        }else{
            return 4;
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CreateTopicViewHolder holder = null;
        if (convertView == null) {
            holder = new CreateTopicViewHolder();
            convertView = View.inflate(mContext, R.layout.item_create_topic, null);
            holder.iv_topic_pic = (ImageView) convertView.findViewById(R.id.iv_topic_pic);
            convertView.setTag(holder);
        } else {
            holder = (CreateTopicViewHolder) convertView.getTag();
        }
        Glide
                .with(mContext)
                .load(mList.get(position))
                .into(holder.iv_topic_pic);
        holder.iv_topic_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddPicClickListener.onAddPicClickListener(position);
            }
        });
        return convertView;
    }


    class CreateTopicViewHolder {
        ImageView iv_topic_pic;
    }

    public interface OnAddPicClickListener {
        void onAddPicClickListener(int position);
    }

    public OnAddPicClickListener onAddPicClickListener;

    public void setOnAddPicClickListener(OnAddPicClickListener onAddPicClickListener) {
        this.onAddPicClickListener = onAddPicClickListener;
    }

    public void changePic(int position, String pic) {
        mList.remove(position);
        mList.add(position, pic);
    }

    public void addPic(int position, String pic) {
        mList.add(position, pic);
    }

    public void deletePic(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    public ArrayList getPicList() {
        return mList;
    }
}
