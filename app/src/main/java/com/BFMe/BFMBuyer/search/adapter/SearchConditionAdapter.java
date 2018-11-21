package com.BFMe.BFMBuyer.search.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.SearchConditionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：搜索条件adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/27 16:21
 */
public class SearchConditionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SearchConditionBean> mData = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_condition, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<SearchConditionBean> data) {
        if (data != null && data.size() > 0) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_content;
        private TextView tv_content;
        private ImageView iv_select;

        private ViewHolder(View itemView) {
            super(itemView);
            rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            iv_select = (ImageView) itemView.findViewById(R.id.iv_select);
        }

        public void setData() {
            SearchConditionBean item = mData.get(getAdapterPosition());
            tv_content.setText(item.getTitle());
            if (item.isSelect()) {
                iv_select.setVisibility(View.VISIBLE);
            } else {
                iv_select.setVisibility(View.GONE);
            }
            rl_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mData.size(); i++) {
                        if (i == getAdapterPosition()) {
                            iv_select.setVisibility(View.VISIBLE);
                        } else {
                            iv_select.setVisibility(View.GONE);
                        }
                    }

                    onItemClickListener.itemClick(getAdapterPosition());
                }
            });
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void itemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
