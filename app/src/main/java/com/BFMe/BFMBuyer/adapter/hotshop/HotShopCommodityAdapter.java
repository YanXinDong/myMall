package com.BFMe.BFMBuyer.adapter.hotshop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.utils.UiUtils;

/**
 * Description：热门店铺商品adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/3/10 14:11
 */
public class HotShopCommodityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEAD = 1;
    private final int TYPE_CONTENT = 2;
    private final int TYPE_END = 3;

    private Context mContext;
    public HotShopCommodityAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return TYPE_HEAD;
        }else if(position == 6) {
            return TYPE_END;
        }else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate ;
        RelativeLayout.LayoutParams lp  = new RelativeLayout.LayoutParams(UiUtils.dip2px(100),UiUtils.dip2px(140));
        if(viewType == TYPE_HEAD) {
            inflate = LayoutInflater.from(mContext).inflate(R.layout.item_hot_shop_head, parent, false);
            lp = new RelativeLayout.LayoutParams(UiUtils.dip2px(50),UiUtils.dip2px(65));
            lp.setMargins(UiUtils.dip2px(250),UiUtils.dip2px(30),UiUtils.dip2px(15),UiUtils.dip2px(0));
            inflate.setLayoutParams(lp);

            return new HeadViewHolder(inflate);

        }else if(viewType == TYPE_END) {
            inflate = LayoutInflater.from(mContext).inflate(R.layout.item_hot_shop_end, parent, false);
            lp.setMargins(UiUtils.dip2px(15),UiUtils.dip2px(30),UiUtils.dip2px(30),UiUtils.dip2px(0));
            inflate.setLayoutParams(lp);
            return new EndViewHolder(inflate);

        }else if(viewType == TYPE_CONTENT) {
            inflate = LayoutInflater.from(mContext).inflate(R.layout.item_hot_shop_commodity, parent, false);
            lp.setMargins(UiUtils.dip2px(15),UiUtils.dip2px(30),UiUtils.dip2px(15),UiUtils.dip2px(0));
            inflate.setLayoutParams(lp);
            return new CommodityViewHolder(inflate);
        }else {
            inflate = LayoutInflater.from(mContext).inflate(R.layout.item_hot_shop_commodity, parent, false);
            lp.setMargins(UiUtils.dip2px(15),UiUtils.dip2px(30),UiUtils.dip2px(15),UiUtils.dip2px(0));
            inflate.setLayoutParams(lp);
            return new CommodityViewHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position == 0) {
            final HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            headViewHolder.ll_item_hot_shop_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(headViewHolder.getAdapterPosition());
                }
            });
        }else if(position == 6) {
            final EndViewHolder endViewHolder = (EndViewHolder) holder;
            endViewHolder.ll_item_hot_shop_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(endViewHolder.getAdapterPosition());
                }
            });
        }else{
            final CommodityViewHolder commodityViewHolder = (CommodityViewHolder) holder;
            commodityViewHolder.ll_item_hot_shop_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(commodityViewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout ll_item_hot_shop_head;

        private HeadViewHolder(View itemView) {
            super(itemView);
            ll_item_hot_shop_head = (LinearLayout) itemView.findViewById(R.id.ll_item_hot_shop_head);
        }
    }

    private class CommodityViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_commodity;
        private LinearLayout ll_item_hot_shop_content;

        private CommodityViewHolder(View itemView) {
            super(itemView);
            iv_commodity = (ImageView) itemView.findViewById(R.id.iv_commodity);
            ll_item_hot_shop_content = (LinearLayout) itemView.findViewById(R.id.ll_item_hot_shop_content);
        }
    }

    private class EndViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout ll_item_hot_shop_end;

        private EndViewHolder(View itemView) {
            super(itemView);
            ll_item_hot_shop_end = (LinearLayout) itemView.findViewById(R.id.ll_item_hot_shop_end);
        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
