package com.BFMe.BFMBuyer.search.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.SearchShopBean;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 * 搜索界面的adapter
 */
public class SearchShopAdapter extends BaseAdapter {

    private Context mContext;
    private List<SearchShopBean.ProductsBean> mProductsBeanList = new ArrayList<>();

    public SearchShopAdapter(Context context, List<SearchShopBean.ProductsBean> productsBeanList) {

        mContext = context;
        if (productsBeanList != null && productsBeanList.size() > 0) {
            mProductsBeanList.clear();
            mProductsBeanList.addAll(productsBeanList);
        }
    }

    @Override
    public int getCount() {
        return mProductsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHodler viewHodler = null;
        if (convertView == null) {
            viewHodler = new ViewHodler();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_subject_product, parent, false);

            viewHodler.iv_product_icon = (ImageView) convertView.findViewById(R.id.iv_product_icon);
            viewHodler.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            viewHodler.tv_price_left = (TextView) convertView.findViewById(R.id.tv_price_left);
            viewHodler.tv_price_right = (TextView) convertView.findViewById(R.id.tv_price_right);

            viewHodler.iv_country_inco = (ImageView) convertView.findViewById(R.id.iv_country_inco);
            viewHodler.tv_country_text = (TextView) convertView.findViewById(R.id.tv_country_text);

            viewHodler.tv_fax = (TextView) convertView.findViewById(R.id.tv_fax);
            viewHodler.tv_free_right = (ImageView) convertView.findViewById(R.id.iv_free_right);

            convertView.setTag(viewHodler);

        } else {

            viewHodler = (ViewHodler) convertView.getTag();

        }


        String productName = mProductsBeanList.get(position).getProductName();
        int tempStart = productName.indexOf("<em>");
        int tempEnd = productName.indexOf("</em>");

        productName = productName.replace("<em>", "");
        productName = productName.replace("</em>","");
        SpannableStringBuilder builder = getSpannableStringBuilder(productName, tempStart, tempEnd);

        //显示带html标签的文本
        viewHodler.tv_product_name.setText(builder);

        //市场价
        viewHodler.tv_price_right.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(mProductsBeanList.get(position).getMarketPrice()));
        //设置删除线
        viewHodler.tv_price_right.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        //现价
        viewHodler.tv_price_left.setText(parent.getContext().getResources().getString(R.string.money_type) + Utils.doubleSave2(mProductsBeanList.get(position).getMinSalePrice()));

        String countryName = mProductsBeanList.get(position).getCountry();
        int tempStart1 = countryName.indexOf("<em>");
        int tempEnd1 = countryName.indexOf("</em>");

        countryName = countryName.replace("<em>", "");
        countryName = countryName.replace("</em>","");

        SpannableStringBuilder countryBuilder = getSpannableStringBuilder(countryName, tempStart1, tempEnd1);
        //国家
        viewHodler.tv_country_text.setText(countryBuilder);
        Glide//国家图标
                .with(mContext)
                .load(mProductsBeanList.get(position).getCountryLogo())
                .centerCrop()
                .placeholder(R.drawable.location_icon)
                .error(R.drawable.location_icon)
                .crossFade()
                .into(viewHodler.iv_country_inco);
        Glide//缩略图
                .with(mContext)
                .load(mProductsBeanList.get(position).getImagePath())
                .centerCrop()
                .placeholder(R.drawable.zhanwei1)
                .crossFade()
                .into(viewHodler.iv_product_icon);
        return convertView;
    }

    @NonNull
    private SpannableStringBuilder getSpannableStringBuilder(String productName, int tempStart, int tempEnd) {
        //利用SpannableStringBuilder分批断设置字体颜色
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(productName);
        ForegroundColorSpan colorSpan =  new ForegroundColorSpan(Color.parseColor("#ff151515"));
        AbsoluteSizeSpan typefaceSpan = new AbsoluteSizeSpan(UiUtils.sp2px(mContext,13));
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);

        if(tempStart != -1 && tempEnd != -1) {
            builder.setSpan(colorSpan, tempStart, tempEnd - 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.setSpan(typefaceSpan, tempStart, tempEnd - 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.setSpan(styleSpan, tempStart, tempEnd - 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return builder;
    }


    class ViewHodler {

        ImageView iv_product_icon;
        TextView tv_product_name;
        TextView tv_price_left;
        TextView tv_price_right;
        ImageView iv_country_inco;
        TextView tv_country_text;

        TextView tv_fax;
        ImageView tv_free_right;
    }

    public List<SearchShopBean.ProductsBean> getDatas() {
        return mProductsBeanList;
    }

    /**
     * 清楚数据
     */
    public void cleanData() {
        mProductsBeanList.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param position
     * @param datas
     */
    public void addData(int position, List<SearchShopBean.ProductsBean> datas) {
        if (datas != null && datas.size() > 0) {
            mProductsBeanList.addAll(position, datas);
            notifyDataSetChanged();
        }
    }


    public void addData(List<SearchShopBean.ProductsBean> datas) {
        addData(0, datas);
    }

}
