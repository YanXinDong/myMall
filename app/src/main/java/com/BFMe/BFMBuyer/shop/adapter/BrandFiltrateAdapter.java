package com.BFMe.BFMBuyer.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.BrandFiltrateBean;
import com.BFMe.BFMBuyer.shop.activity.ShopListActivity;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/7 20:13
 */
public class BrandFiltrateAdapter extends CommonAdapter<BrandFiltrateBean.BrandBean.HotDataBean> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private List<BrandFiltrateBean.BrandBean.HotDataBean> mDatas = new ArrayList<>();
    private Context context;
    public BrandFiltrateAdapter(Context context, int layoutId, List<BrandFiltrateBean.BrandBean.HotDataBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
        if(datas != null && datas.size() > 0) {
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }

    @Override
    protected void convert(ViewHolder holder, final BrandFiltrateBean.BrandBean.HotDataBean hotDataBean, int position) {
        holder.setText(R.id.tv_content,hotDataBean.getBrandName());
        holder.setOnClickListener(R.id.ll_item_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopListActivity.class);
                intent.putExtra("TitleName",hotDataBean.getBrandName());
                intent.putExtra("BrandId",hotDataBean.getBrandId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public long getHeaderId(int position) {
        return getSortType(position);
    }

    private long getSortType(int position) {

        String brandChar = mDatas.get(position).getBrandChar();
        if(brandChar.equals(context.getString(R.string.hot_brand))) {
            return 1;
        }else {
            if(brandChar.length() == 1) {
                char[] c = brandChar.toCharArray();
                return (int)c[0];
            }
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_360_35_bg_f0f0f0, parent, false);
        return new BrandHeadViewHolder(inflate);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        BrandHeadViewHolder brandHeadViewHolder = (BrandHeadViewHolder) holder;
        brandHeadViewHolder.tv_title.setText(mDatas.get(position).getBrandChar());
    }

    /**
     * 粘性头部的viewHolder
     */
    private class BrandHeadViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        private BrandHeadViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
