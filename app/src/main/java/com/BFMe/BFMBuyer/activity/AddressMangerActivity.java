package com.BFMe.BFMBuyer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.AddressListBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 地址管理
 */
public class AddressMangerActivity extends IBaseActivity implements View.OnClickListener {
    private static final int REQUEST = 10;
    public static final int REQUEST_CODE_1 = 1;
    public static final int REQUEST_CODE_2 = 2;
    private LinearLayout llDetailAddress;
    private TextView tvSelectAddress;
    private ViewHolder holder;
    private HashMap<Integer, CheckBox> stateItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {

        setBackButtonVisibility(View.VISIBLE);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);

        tvSelectAddress = (TextView) findViewById(R.id.tv_select_address);
        Button btnAddAddress = (Button) findViewById(R.id.btn_add_address);
        llDetailAddress = (LinearLayout) findViewById(R.id.ll_detail_address);

        tv_title.setText(getResources().getString(R.string.address_manage));
        btnAddAddress.setOnClickListener(this);
        if (llDetailAddress.getChildAt(0) == null) {
            tvSelectAddress.setVisibility(View.INVISIBLE);
        } else {
            tvSelectAddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getNetAddress();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_address_manger;
    }

    /**
     * 联网获取收货地址
     */
    private void getNetAddress() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("userId",mUserId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_ADDRESS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String data = rootBean.Data;
                        if(Integer.parseInt(rootBean.ErrCode) == 0) {
                            //隐藏加载进度条
                            AddressListBean addressListBean = mGson.fromJson(data, AddressListBean.class);
                            ArrayList<AddressListBean.addresses> addressList = addressListBean.Address;
                            showData(addressList);
                        }
                    }
                });
    }

    private void showData(ArrayList<AddressListBean.addresses> addressList) {
        llDetailAddress.removeAllViews();
        if (addressList != null && addressList.size() > 0) {
            for (int i = 0; i < addressList.size(); i++) {
                showAddress(addressList.get(i).ShipTo
                        , addressList.get(i).Phone
                        , addressList.get(i).Address
                        , addressList.get(i).RegionFullName
                        , addressList.get(i).IsDefault);
            }

            editAddress(addressList);
        } else {
            tvSelectAddress.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 地址管理
     *
     * @param addressList
     */
    private void editAddress(final ArrayList<AddressListBean.addresses> addressList) {

        stateItem = new HashMap<>();
        for (int i = 0; i < llDetailAddress.getChildCount(); i++) {

            final LinearLayout childAt = (LinearLayout) llDetailAddress.getChildAt(i);
            final int finalI = i;
            RelativeLayout childAt0 = (RelativeLayout) childAt.getChildAt(0);
            CheckBox cbCheckShop = (CheckBox) childAt0.getChildAt(0);
            if(i==0){
                cbCheckShop.setChecked(true);
                settingAddress(addressList.get(0).Id);
            }
            else{
                cbCheckShop.setChecked(false);
            }
            childAt0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddressMangerActivity.this, AddAddressActivity.class);
                    intent.putExtra("edit", REQUEST_CODE_1);
                    intent.putExtra("position", finalI);
                    intent.putExtra("addressId", addressList.get(finalI).Id);
                    intent.putExtra("RegionId", addressList.get(finalI).RegionId);

                    intent.putExtra("addressInfo", new Gson().toJson(addressList.get(finalI)));
                    startActivityForResult(intent, REQUEST);
                    startAnim();
                }
            });
            stateItem.put(finalI, cbCheckShop);
            stateItem.get(finalI).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        showProgress();
                        for (int i = 0; i < stateItem.size(); i++) {
                            if (i != finalI) {
                                stateItem.get(i).setChecked(false);
                            }
                        }
                        settingAddress(addressList.get(finalI).Id);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_address:
                if (llDetailAddress.getChildCount() == 6) {
                    showToast(getResources().getString(R.string.add_address_hint3));
                    return;
                }
                Intent intent = new Intent(this, AddAddressActivity.class);
                intent.putExtra("edit", REQUEST_CODE_2);
                startActivityForResult(intent, REQUEST);
                startAnim();
                break;
            case R.id.iv_arrow:
                finish();
                exitAnim();
                break;
            default:
                break;
        }
    }

    /**
     * 显示地址数据
     *
     * @param addressName
     * @param addressPhone
     * @param address
     * @param cityAddress
     * @param isDefault
     */
    private void showAddress(String addressName, String addressPhone, String address, String cityAddress, boolean isDefault) {
        holder = new ViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.address_manger_item, null, false);

        holder.tvAddressName = (TextView) view.findViewById(R.id.tv_address_name);
        holder.tvAddressPhone = (TextView) view.findViewById(R.id.tv_address_phone);
        holder.tvAccountAddress = (TextView) view.findViewById(R.id.tv_account_address);
        holder.cb_check_shop = (CheckBox) view.findViewById(R.id.cb_check_shop);

        holder.tvAddressName.setText(addressName);
        holder.tvAddressPhone.setText(addressPhone);
        holder.tvAccountAddress.setText(cityAddress + " " + address);
        holder.cb_check_shop.setChecked(isDefault);

        llDetailAddress.addView(view);
        tvSelectAddress.setVisibility(View.VISIBLE);
    }

    static class ViewHolder {
        public TextView tvAddressPhone;
        public TextView tvAddressName;
        public TextView tvAccountAddress;
        private CheckBox cb_check_shop;
    }

    /**
     * 设置为默认地址
     *
     * @param id
     */
    private void settingAddress(final String id) {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("userId",mUserId);
        params.put("addressId", id);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_DEFAULT_ADDRESS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if(rootBean.ErrCode.equals("0")) {
                            showToast(getResources().getString(R.string.setting_default_address_succeed));
                        }else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }
}
