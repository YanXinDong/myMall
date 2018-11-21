package com.BFMe.BFMBuyer.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;

import java.util.ArrayList;

/**
 * Description：搜索中最近搜索
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/4/11 16:25
 */
public class RecentViewedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mRecentViewedList = new ArrayList<>();

    public RecentViewedAdapter(Context searchActivity, ArrayList<String> recentViewed) {
        this.mContext = searchActivity;
        if (recentViewed != null && recentViewed.size() > 0) {
            mRecentViewedList.addAll(recentViewed);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recent_viewed, null);
        return new RecentViewedHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RecentViewedHolder recentViewedHolder = (RecentViewedHolder) holder;
        recentViewedHolder.tvRecentlyViewed.setText(mRecentViewedList.get(position));
        recentViewedHolder.tvRecentlyViewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecentViewedClickListener.OnRecentViewed(mRecentViewedList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mRecentViewedList.size() <= 6) {
            return mRecentViewedList.size();
        } else {
            return 6;
        }

    }

    public interface OnRecentViewedClickListener {
        void OnRecentViewed(String content);
    }

    private OnRecentViewedClickListener onRecentViewedClickListener;

    public void setOnRecentViewedClickListener(OnRecentViewedClickListener onRecentViewedClickListener) {
        this.onRecentViewedClickListener = onRecentViewedClickListener;
    }

    class RecentViewedHolder extends RecyclerView.ViewHolder {
        private TextView tvRecentlyViewed;

        public RecentViewedHolder(View itemView) {
            super(itemView);
            tvRecentlyViewed = (TextView) itemView.findViewById(R.id.tv_recently_viewed);
        }
    }
}
