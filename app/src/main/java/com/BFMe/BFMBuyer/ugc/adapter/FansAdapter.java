package com.BFMe.BFMBuyer.ugc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.ugc.activity.UserInfoActivity;
import com.BFMe.BFMBuyer.ugc.bean.FansBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：我的粉丝
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/13 15:56
 */
public class FansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private List<FansBean.DataListBean> mDataList = new ArrayList<>();
    private boolean isFocus;

    public FansAdapter(Activity context, List<FansBean.DataListBean> dataList) {
        this.mContext = context;
        if (dataList != null && dataList.size() > 0) {
            mDataList.clear();
            mDataList.addAll(dataList);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_my_fans, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.setData(position);

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(int position, List<FansBean.DataListBean> dataList) {
        if (dataList != null && dataList.size() > 0) {
            mDataList.addAll(position, dataList);
            notifyDataSetChanged();
        }
    }

    public void cleanData() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public List<FansBean.DataListBean> getFanList() {
        return mDataList;
    }

    public interface OnFansClickListener {
        void onFans(int position, boolean isAttention);
    }

    private OnFansClickListener onFansClickListener;

    public void setOnFansClickListener(OnFansClickListener onFansClickListener) {
        this.onFansClickListener = onFansClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivHeadAttention;
        private TextView tvNameAttention;
        private TextView tvContentAttention;
        private CheckBox cbAttention;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivHeadAttention = (ImageView) itemView.findViewById(R.id.iv_head_fans);
            tvNameAttention = (TextView) itemView.findViewById(R.id.tv_name_fans);
            tvContentAttention = (TextView) itemView.findViewById(R.id.tv_content_fans);
            cbAttention = (CheckBox) itemView.findViewById(R.id.cb_fans);
        }

        public void setData(final int position) {
            if (mDataList.get(position).getIsFocus() == 1) {
                cbAttention.setChecked(true);
            } else {
                cbAttention.setChecked(false);
            }
            tvContentAttention.setText(mDataList.get(position).getTopicCount()
                    + mContext.getString(R.string.note_number) +
                    "," +
                    mDataList.get(position).getFansCount() +
                    mContext.getString(R.string.fans_number));
            tvNameAttention.setText(mDataList.get(position).getUserName());
            Glide
                    .with(mContext)
                    .load(mDataList.get(position).getPhoto())
                    .transform(new GlideCircleTransform(mContext))
                    .placeholder(R.drawable.zhanwei3)
                    .error(R.drawable.zhanwei3)
                    .into(ivHeadAttention);
            cbAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFocus) {
                        isFocus = false;
                        onFansClickListener.onFans(position, true);
                    } else {
                        isFocus = true;
                        onFansClickListener.onFans(position, false);
                    }
                }
            });
            ivHeadAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentUser = new Intent(mContext, UserInfoActivity.class);
                    intentUser.putExtra(GlobalContent.USER_INFO_ID, mDataList.get(position).getUserId());
                    mContext.startActivity(intentUser);
                    mContext.overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
            });
        }
    }
}
