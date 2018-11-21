package com.BFMe.BFMBuyer.ugc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.bean.MyCommentItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：我的评论adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/12 18:47
 */
public class MyCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyCommentItem> mItemList = new ArrayList<>();
    public MyCommentAdapter(List<MyCommentItem> itemList) {
        if(itemList != null && itemList.size() > 0) {
            mItemList.clear();
            mItemList.addAll(itemList);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData();
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void cleanData() {
        mItemList.clear();
    }

    public void addData(List<MyCommentItem> myCommentItems) {
        addData(0,myCommentItems);
    }

    public void addData(int position,List<MyCommentItem> myCommentItems) {
        if(myCommentItems != null && myCommentItems.size() > 0) {
            mItemList.addAll(position,myCommentItems);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rl_my_comment;
        private ImageView iv_icon;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;
        private Context mContext;
        public ViewHolder(View itemView) {
            super(itemView);
            rl_my_comment = (RelativeLayout) itemView.findViewById(R.id.rl_my_comment);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            mContext = itemView.getContext();
        }

        public void setData() {
            final MyCommentItem item = mItemList.get(getLayoutPosition() - 1);
            Glide
                    .with(mContext)
                    .load(item.getImageUrl())
                    .into(iv_icon);
            tv_title.setText(item.getTopicContent());
            tv_content.setText(item.getCommentContent());
            tv_time.setText(item.getCreateDate());

            rl_my_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(item.getId());
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(long topicId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
