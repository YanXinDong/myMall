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
import com.BFMe.BFMBuyer.ugc.bean.PushExpressNoticeBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：物流通知
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/13 17:43
 */
public class LogisticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PushExpressNoticeBean.DataBean> mPushExpressData = new ArrayList<>();

    public LogisticsAdapter(Context context, List<PushExpressNoticeBean.DataBean> pushExpressData) {
        this.mContext = context;
        if (pushExpressData != null && pushExpressData.size() != 0) {
            mPushExpressData.clear();
            this.mPushExpressData.addAll(pushExpressData);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_logistics_push, parent, false);
        return new LogisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LogisticsViewHolder logisticsHolder = (LogisticsViewHolder) holder;
        logisticsHolder.tvTime.setText(mPushExpressData.get(position).getPushDate());
        logisticsHolder.tvContentLogistics.setText(mPushExpressData.get(position).getContent());
        Glide
                .with(mContext)
                .load(mPushExpressData.get(position).getImagePath())
                .placeholder(R.drawable.zhanwei1)
                .error(R.drawable.zhanwei1)
                .into(logisticsHolder.ivPicLogistic);
        logisticsHolder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPushExpressClickListener.onPushExpress(mPushExpressData.get(position).getOrderId(),
                        mPushExpressData.get(position).getIsComment(),
                        mPushExpressData.get(position).getOrderStateDesc());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPushExpressData.size();
    }

    public void cleanData() {
        mPushExpressData.clear();
        notifyDataSetChanged();
    }

    public void addData(List<PushExpressNoticeBean.DataBean> pushExpressData) {
        addData(0, pushExpressData);
    }

    public void addData(int position, List<PushExpressNoticeBean.DataBean> pushExpressData) {
        if (pushExpressData != null && pushExpressData.size() != 0) {
            mPushExpressData.addAll(position, pushExpressData);
            notifyDataSetChanged();
        }
    }

    public interface OnPushExpressClickListener {
        void onPushExpress(String orderId, String isComment,String orderStateDesc);
    }

    private OnPushExpressClickListener onPushExpressClickListener;

    public void setOnPushExpressClickListener(OnPushExpressClickListener onPushExpressClickListener) {
        this.onPushExpressClickListener = onPushExpressClickListener;
    }

    class LogisticsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private ImageView ivPicLogistic;
        private TextView tvContentLogistics;
        private RelativeLayout rl_layout;

        public LogisticsViewHolder(View itemView) {
            super(itemView);

            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivPicLogistic = (ImageView) itemView.findViewById(R.id.iv_pic_logistic);
            tvContentLogistics = (TextView) itemView.findViewById(R.id.tv_content_logistics);
            rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);

        }
    }
}
