package com.BFMe.BFMBuyer.adapter.hotshop;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.view.CircularImageView;
import com.BFMe.BFMBuyer.view.MyRecyclerView;

/**
 * Description：热门店铺adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/3/10 11:16
 */
public class PopularShopsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    public PopularShopsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_rv_hot_shops, parent, false);

        return new HotShopViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HotShopViewHolder hotShopViewHolder = (HotShopViewHolder) holder;

        final MyRecyclerView rv_hot_shop_commodity = hotShopViewHolder.rv_hot_shop_commodity;
        rv_hot_shop_commodity.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        HotShopCommodityAdapter hotShopCommodityAdapter = new HotShopCommodityAdapter(mContext);
        rv_hot_shop_commodity.setAdapter(hotShopCommodityAdapter);

        //商品横向列表的item点击事件监听
        hotShopCommodityAdapter.setOnItemClickListener(new HotShopCommodityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    if(isDefault(rv_hot_shop_commodity)) {
                        rv_hot_shop_commodity.smoothScrollBy(UiUtils.dip2px(300), UiUtils.dip2px(0));
                    }else {
                        scrollToPosition(rv_hot_shop_commodity);
                    }

                } else if (position == 6) {
                    Toast.makeText(mContext, "全部商品", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "商品详情", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //滑动改变透明度
        rv_hot_shop_commodity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mDistanceX;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceX += dx;
                //获取背景控件的宽度
                int bgWidth = hotShopViewHolder.view_bg.getWidth();
                //当滑动的距离 <= view_bg宽度的时候，改变view_bg背景色的透明度，达到渐变的效果
                if (mDistanceX <= bgWidth - 200) {
                    float scale = (float) mDistanceX / bgWidth;
                    float alpha = scale * 255;
                    hotShopViewHolder.view_bg.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
                } else {
                    //上述虽然判断了滑动距离与view_bg宽度相等的情况，但是实际测试时发现，view_bg的背景色
                    hotShopViewHolder.view_bg.setBackgroundResource(R.color.translucence_b);
                }

                boolean aDefault = isDefault(rv_hot_shop_commodity);//是否处于默认状态
                if(aDefault) {
                    //banner背景色设置为全透明
                    hotShopViewHolder.view_bg.setBackgroundResource(R.color.transparent);
                }
            }
        });

        //设置背景banner的 点击事件监听
        rv_hot_shop_commodity.setOnBannerClickListener(new MyRecyclerView.OnBannerClickListener() {
            @Override
            public void onBannerClick() {
                boolean aDefault = isDefault(rv_hot_shop_commodity);
                if(aDefault) {
                    Toast.makeText(mContext, "Banner", Toast.LENGTH_SHORT).show();
                }else {
                    scrollToPosition(rv_hot_shop_commodity);
                }
            }
        });
    }

    /**
     * 是否是默认状态
     * @param rv_hot_shop_commodity
     */
    private boolean isDefault(MyRecyclerView rv_hot_shop_commodity) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv_hot_shop_commodity.getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        //获取与该view的顶部的偏移量用于判断是否是初始位置
        return topView.getLeft() >= UiUtils.dip2px(250);
    }

    /**
     * 让RecyclerView滚动到指定位置
     * @param recyclerView
     */
    private void scrollToPosition(MyRecyclerView recyclerView) {
        if(recyclerView.getLayoutManager() != null) {
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,null,0);
        }
    }
    @Override
    public int getItemCount() {
        return 5;
    }

    private class HotShopViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView civ_head;
        private TextView tv_user_name;
        private TextView tv_user_;
        private ImageView iv_shop_banner;
        private MyRecyclerView rv_hot_shop_commodity;
        private TextView tv_hot_shop_intro;
        private View view_bg;

        private HotShopViewHolder(View itemView) {
            super(itemView);
            civ_head = (CircularImageView) itemView.findViewById(R.id.civ_head);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_user_ = (TextView) itemView.findViewById(R.id.tv_user_);
            iv_shop_banner = (ImageView) itemView.findViewById(R.id.iv_shop_banner);
            rv_hot_shop_commodity = (MyRecyclerView) itemView.findViewById(R.id.rv_hot_shop_commodity);
            tv_hot_shop_intro = (TextView) itemView.findViewById(R.id.tv_hot_shop_intro);
            view_bg = itemView.findViewById(R.id.view_bg);
        }
    }
}
