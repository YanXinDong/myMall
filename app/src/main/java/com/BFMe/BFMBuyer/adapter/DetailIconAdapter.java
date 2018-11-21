package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.ProductDetailBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/9.
 * 商品详情页的icon适配器
 */
public class DetailIconAdapter extends PagerAdapter {

    private Context mContext;
    private ProductDetailBean.ProductBean mProducts;
    private ArrayList<ImageView> mImageViews = new ArrayList<>();
    public DetailIconAdapter(Context context, ProductDetailBean.ProductBean products, ArrayList<ImageView> imageViews) {
        mContext = context;
        if(products != null) {
            mProducts = products;
        }
        if(imageViews != null && imageViews.size() > 0) {
            mImageViews.clear();
            mImageViews.addAll(imageViews);
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Glide//商品缩略图
                .with(mContext)
                .load(mProducts.getImagePath().get(position))
                .placeholder(R.drawable.zhanwei1)
                .crossFade()
                .fitCenter()
                .into(mImageViews.get(position));
        container.addView(mImageViews.get(position));
        return mImageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return mProducts.getImagePath().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
