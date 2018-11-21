package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.ImageLargeActivity;
import com.BFMe.BFMBuyer.javaBean.CommentBean;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.bumptech.glide.Glide.with;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/25 18:53
 */
public class CommentAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<CommentBean.CommentData> commentList =new ArrayList<>();

    public CommentAdapter(Context ctx, ArrayList<CommentBean.CommentData> commentLists) {
        this.ctx = ctx;
        if(commentLists!=null && commentLists.size()>=0){
            commentList.clear();
            commentList.addAll(commentLists);
        }
    }

    ;

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public CommentBean.CommentData getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.list_item_comment, null);
            holder = new ViewHolder();
            holder.ivUserPhoto = (ImageView) convertView.findViewById(R.id.iv_user_photo);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.llStart = (com.BFMe.BFMBuyer.view.RatingBar) convertView.findViewById(R.id.ll_start);
            holder.gv_product_comment = (GridView) convertView.findViewById(R.id.gv_product_comment);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommentBean.CommentData info = getItem(position);
        holder.tvUserName.setText(info.UserName);//用户名称
        holder.tvTime.setText(info.ReviewDate);//评价时间
        holder.tvComment.setText(info.ReviewContent);//评价内容

        //评价的星星

        holder.llStart.setStar(Float.parseFloat(info.ReviewMark));

        //用户头像
                with(ctx)
                .load(commentList.get(position).UserPhoto)
                .transform(new GlideCircleTransform(ctx))
                .placeholder(R.drawable.zhanwei3)
                .error(R.drawable.zhanwei3)
                .crossFade()
                .into(holder.ivUserPhoto);

        //评价图片
        if (commentList.get(position).Images == null || commentList.get(position).Images.size() <= 0) {
            holder.gv_product_comment.setVisibility(View.GONE);
        } else {
            holder.gv_product_comment.setVisibility(View.VISIBLE);
            holder.gv_product_comment.setAdapter(new MyAdapter(ctx, commentList.get(position).Images));
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView ivUserPhoto; //评论人图标
        private TextView tvUserName; //
        private TextView tvTime;
        private TextView tvComment;
        private com.BFMe.BFMBuyer.view.RatingBar llStart;//五颗星

        private GridView gv_product_comment;
    }


    private class MyAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<String> mImages = new ArrayList<>();

        public MyAdapter(Context context, ArrayList<String> images) {
            mContext = context;
            if (images != null && images.size() > 0) {
                mImages.clear();
                mImages.addAll(images);
            }
        }

        @Override
        public int getCount() {
            return mImages.size() > 3 ? 3 : mImages.size();
        }

        @Override
        public Object getItem(int position) {
            return mImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHodler viewHodler;
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
                viewHodler = new ViewHodler();

                viewHodler.imageView = (ImageView) convertView.findViewById(R.id.iv_item_comment);
                convertView.setTag(viewHodler);
            }else {
                viewHodler = (ViewHodler) convertView.getTag();

            }

            Glide
                    .with(ctx)
                    .load(mImages.get(position))
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.zhanwei1)
                    .error(R.drawable.zhanwei1)
                    .into(viewHodler.imageView);
            viewHodler.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent  intent =  new Intent (ctx,ImageLargeActivity.class);
                    intent.putExtra("position",position);
                    intent.putStringArrayListExtra("images",mImages);
                    ctx.startActivity(intent);
                }
            });

            return convertView;
        }

        private class ViewHodler{
            private ImageView imageView;
        }
    }


    /**
     * 添加数据
     *
     * @param datas
     * @param position
     */
    public void addData(int position, ArrayList<CommentBean.CommentData> datas) {
        if (datas != null && datas.size() > 0) {
            commentList.addAll(position, datas);
            notifyDataSetChanged();
        }
    }


    public void addData(ArrayList<CommentBean.CommentData> datas) {
        addData(0, datas);
    }

    /**
     * 清楚数据
     */
    public void cleanData() {
        commentList.clear();
        notifyDataSetChanged();
    }
}
