package com.BFMe.BFMBuyer.shop.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.ShopListBean;
import com.BFMe.BFMBuyer.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Description：店铺主页商品列表adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2018/2/7 14:04
 */
public class ShopHomeListAdapter extends CommonAdapter<ShopListBean.ProductsBean> {

    public ShopHomeListAdapter(Context context, int layoutId, List<ShopListBean.ProductsBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final ShopListBean.ProductsBean data, int position) {
        holder.setText(R.id.tv_product_name,data.getProductName());
        holder.setText(R.id.tv_price_right, MyApplication.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(data.getMarketPrice()));//市场价
        //设置删除线
        TextView view = holder.getView(R.id.tv_price_right);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.setText(R.id.tv_price_left,MyApplication.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(data.getMinSalePrice()));//商城价
        holder.setImageGlide(R.id.iv_product_icon,data.getImagePath(),1);

        ImageView imageView =  holder.getView(R.id.iv_country_inco);
        Glide//国家图标
                .with(mContext)
                .load(data.getCountryLogo())
                .placeholder(R.drawable.location_icon)
                .error(R.drawable.location_icon)
                .crossFade()
                .into(imageView);

        //国家名称
        holder.setText(R.id.tv_country_text,data.getCountry());

        holder.setOnClickListener(R.id.rl_subjectproduct, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(String.valueOf(data.getProductId()) ,String.valueOf(data.getShopId()));
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(String productId ,String shopId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
