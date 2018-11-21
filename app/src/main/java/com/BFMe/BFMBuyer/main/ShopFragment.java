package com.BFMe.BFMBuyer.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.BaseFragment;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.main.bean.ShopHomeBean;
import com.BFMe.BFMBuyer.shop.activity.BrandFiltrateActivity;
import com.BFMe.BFMBuyer.shop.activity.RegionFiltrateActivity;
import com.BFMe.BFMBuyer.shop.activity.ShopListActivity;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Request;

/**
 * Description：店铺Fragment
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/14 16:20
 */
public class ShopFragment extends BaseFragment{
    private RecyclerView shopRV;
    private int[] mTitle = {R.string.guarantee_the_supply_of_goods,
            R.string.guarantee_import,
            R.string.safeguard_seller,
            R.string.region,
            R.string.brand};

    private Context mContext;
    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.activity_rv,null,false);
        shopRV = (RecyclerView) view.findViewById(R.id.rv);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) shopRV.getLayoutParams();
        layoutParams.setMargins(UiUtils.dip2px(10),0,UiUtils.dip2px(10),0);
        shopRV.setPadding(0, 0, 0,UiUtils.dip2px(10));
        shopRV.setClipToPadding(false);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = mActivity;
        getNetData();
    }

    private void getNetData() {
        showProgress();
        OkHttpUtils
                .get()
                .url(GlobalContent.GET_SHOP_HOME_PAGE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            Logger.e("店铺首页=="+rootBean.Data);
                            showData(mGson.fromJson(rootBean.Data, ShopHomeBean.class));
                        }else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void showData(ShopHomeBean shopHomeBean) {
        shopRV.setLayoutManager(new LinearLayoutManager(mActivity));
        CommonAdapter<ShopHomeBean.ShopDataBean> commonAdapter
                = new CommonAdapter<ShopHomeBean.ShopDataBean>(mActivity, R.layout.item_image_340_110, shopHomeBean.getShopData()) {

            @Override
            protected void convert(ViewHolder holder, ShopHomeBean.ShopDataBean shopDataBean, int position) {
                holder.setImageGlide(R.id.iv_icon,shopDataBean.getImagePath());
            }
        };
        shopRV.setAdapter(commonAdapter);
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent;
                switch (position){
                    case 0:
                    case 1:
                    case 2:
                        intent = new Intent(mContext, ShopListActivity.class);
                        intent.putExtra("TitleName",getString(mTitle[position]));
                        intent.putExtra("ShopType",position+1);
                        startActivity(intent);
                        startAnim();
                        break;
                    case 3:
                        intent = new Intent(mContext, RegionFiltrateActivity.class);
                        intent.putExtra("TitleName",getString(mTitle[position]));
                        intent.putExtra("ShopType",position+1);
                        startActivity(intent);
                        bottomStartAnim();
                        break;
                    case 4:
                        intent = new Intent(mContext, BrandFiltrateActivity.class);
                        intent.putExtra("TitleName",getString(mTitle[position]));
                        intent.putExtra("ShopType",position+1);
                        startActivity(intent);
                        bottomStartAnim();
                        break;
                    default:
                        intent = new Intent(mContext, ShopListActivity.class);
                        intent.putExtra("TitleName",getString(mTitle[position]));
                        intent.putExtra("ShopType",position+1);
                        startActivity(intent);
                        startAnim();
                        break;
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

}
