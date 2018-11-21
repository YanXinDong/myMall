package com.BFMe.BFMBuyer.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.AttentionBean;
import com.BFMe.BFMBuyer.shop.activity.ShopHomeActivity;
import com.BFMe.BFMBuyer.view.GlideCircleTransform;
import com.BFMe.BFMBuyer.view.SlideLayout;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：关注的店铺adapter
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/10/25 17:38
 */
public class AttentionShopAdapter extends BaseAdapter {

    private List<AttentionBean.ShopsBean> mShopName = new ArrayList<>();
    private Context mActivity;
    public AttentionShopAdapter(List<AttentionBean.ShopsBean> shopName, Context context) {
        this.mActivity = context;
        if(shopName != null && shopName.size() > 0) {
            mShopName.clear();
            mShopName.addAll(shopName);
        }
    }

    @Override
    public int getCount() {
        return mShopName.size();
    }

    @Override
    public Object getItem(int position) {
        return mShopName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder myHolder = null;
        if (convertView == null) {
            myHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attention_delete,parent,false);
            myHolder.ll_layout = (RelativeLayout) convertView.findViewById(R.id.tv_item_content);
            myHolder.delete= (RelativeLayout) convertView.findViewById(R.id.tv_item_menu);
            myHolder.iv_temp_attention = (ImageView) convertView.findViewById(R.id.iv_temp_attention);
            myHolder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
            myHolder.tv_name_shop = (TextView) convertView.findViewById(R.id.tv_name_shop);

            convertView.setTag(myHolder);
        } else {
            myHolder = (ViewHolder) convertView.getTag();
        }

        final AttentionBean.ShopsBean item = (AttentionBean.ShopsBean) getItem(position);

        myHolder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //条目点击事件
                SlideLayout slideLayout = (SlideLayout) v.getParent();
                slideLayout.closeMenu();//关闭当前打开的item
                Intent intent = new Intent(mActivity, ShopHomeActivity.class);
                String shopId = item.getShopId() + "";
                intent.putExtra("shopId", shopId);
                mActivity.startActivity(intent);
            }
        });
        myHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerDelete.onDeleteClick(item.getShopId()+"");
                SlideLayout slideLayout = (SlideLayout) v.getParent();
                slideLayout.closeMenu();//关闭当前打开的item
            }
        });

        //给所有的item设置监听
        SlideLayout slideLayout = (SlideLayout) convertView;
        Glide.with(parent.getContext())
                .load(item.getLogo())
                .transform(new GlideCircleTransform(parent.getContext()))
                .error(R.drawable.rabit)
                .into(myHolder.iv_temp_attention);

        myHolder.tv_name_shop.setText(item.getShopName());
        if(TextUtils.isEmpty(item.getDescription())) {
            myHolder.tv_description.setText(mActivity.getString(R.string.shop_description_empt_hint));
        }else {
            myHolder.tv_description.setText(item.getDescription());
        }
        //给所有的item设置监听
        slideLayout.setOnStateChangeListener(onStateChangeListener);

        return convertView;
    }

    /**
     * 清除数据
     */
    public void cleanData() {
        mShopName.clear();
        notifyDataSetChanged();
    }

    public void addData(List<AttentionBean.ShopsBean> shops) {
        addData(0,shops);
    }

    public void addData(int position,List<AttentionBean.ShopsBean> shops) {
        if (shops != null && shops.size() > 0) {
            mShopName.addAll(position, shops);
            notifyDataSetChanged();
        }
    }
    static class ViewHolder {
        RelativeLayout ll_layout;
        RelativeLayout delete;
        ImageView iv_temp_attention;
        TextView tv_name_shop;
        TextView tv_description;
    }
    /**
     * 定义接口实现删除
     */
    public interface OnClickListenerDelete {
        void onDeleteClick(String shopId);
    }

    private OnClickListenerDelete onClickListenerDelete;


    public void setOnClickListenerDelete(OnClickListenerDelete onClickListenerDelete) {
        this.onClickListenerDelete = onClickListenerDelete;
    }

    //滑动删除
    private SlideLayout openedLayout;
    private SlideLayout.OnStateChangeListener onStateChangeListener = new SlideLayout.OnStateChangeListener() {

        @Override
        public void onOpen(SlideLayout layout) {
            openedLayout = layout; //保存打开的item
        }

        @Override
        public void onClose(SlideLayout layout) {
            if (layout == openedLayout) {
                openedLayout = null; //将保存Item置空
            }
        }

        @Override
        public void onDown(SlideLayout layout) {
            if (openedLayout != null && layout != openedLayout) { //如果按下不是打开的item
                openedLayout.closeMenu(); //将其关闭
            }
        }

    };
}
