package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 增加，编辑地址
 */
public class AddAddressActivity extends IBaseActivity implements View.OnClickListener {
    private static final int REQUEST = 10;
    public static final int SELECT_CITY = 1002;
    public static final int ADDRESS_EDIT = 30;
    public static final int ADDRESS_DELETE = 40;
    public static final int REQUEST_CODE_1 = 1;
    public static final int REQUEST_CODE_2 = 2;
    private TextView tvAddaddressArea;
    private EditText dtAddressName;
    private EditText tvAddressPhone;
    private EditText etAddressDetail;
    private int edit;
    private int position;
    private EditText etIdcard;
    private String addressId;
    private String id;
    private Button btnKeepAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    //初始化布局
    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        RelativeLayout rlArea = (RelativeLayout) findViewById(R.id.rl_area);

        btnKeepAddress = (Button) findViewById(R.id.btn_keep_address);
        //删除地址
        TextView tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        //收货人名字
        dtAddressName = (EditText) findViewById(R.id.dt_address_name);
        //手机号
        tvAddressPhone = (EditText) findViewById(R.id.tv_address_phone);
        //三级地址
        tvAddaddressArea = (TextView) findViewById(R.id.tv_addaddress_area);
        //详细地址
        etAddressDetail = (EditText) findViewById(R.id.et_address_detail);
        //身份证号
        etIdcard = (EditText) findViewById(R.id.et_idcard);

        Intent intent = getIntent();
        edit = intent.getIntExtra("edit", 2);
        addressId = intent.getStringExtra("addressId");
        id = intent.getStringExtra("RegionId");
        if (edit == REQUEST_CODE_1) {
            tv_title.setText(getResources().getString(R.string.edit_address));

            String addressInfo = intent.getStringExtra("addressInfo");
            AddressListBean.addresses addresses = new Gson().fromJson(addressInfo, AddressListBean.addresses.class);

            position = intent.getIntExtra("position", Integer.MAX_VALUE);

            dtAddressName.setText(addresses.ShipTo);
            tvAddressPhone.setText(addresses.Phone);
            tvAddaddressArea.setText(addresses.RegionFullName);
            etAddressDetail.setText(addresses.Address);
            etIdcard.setText(addresses.CardNumber);

            tv_title_right.setText(getResources().getString(R.string.delete));
        }
        if (edit == REQUEST_CODE_2) {
            tv_title.setText(getResources().getString(R.string.add_address));
        }

        rlArea.setOnClickListener(this);
        btnKeepAddress.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_area:
                Intent intent = new Intent(this, SelectCitiesActivity.class);
                startActivityForResult(intent, REQUEST);
                startAnim();
                break;
            case R.id.tv_title_right:
                deleteAddress();
                finish();
                exitAnim();
                break;
            case R.id.btn_keep_address:

                //获取地址信息
                String addressName = dtAddressName.getText().toString().trim();
                String addressPhone = tvAddressPhone.getText().toString().trim();
                String addaddressArea1 = tvAddaddressArea.getText().toString().trim();
                String addressDetail = etAddressDetail.getText().toString().trim();
                String idcard = etIdcard.getText().toString().trim();
                Intent intentSave = new Intent();
                intentSave.putExtra("addressName", addressName);
                intentSave.putExtra("addressPhone", addressPhone);
                intentSave.putExtra("address", addaddressArea1 + addressDetail);

                if (TextUtils.isEmpty(addressName) || TextUtils.isEmpty(addressPhone) ||
                        TextUtils.isEmpty(addaddressArea1) || TextUtils.isEmpty(addressDetail) || TextUtils.isEmpty(idcard)) {
                    showToast(getResources().getString(R.string.input_info_full_hint));
                    return;
                }

                if (edit == REQUEST_CODE_1) {
                    editAddress(addressName, addressPhone, addressDetail, idcard);
                    intentSave.putExtra("position", position);
                    setResult(ADDRESS_EDIT, intentSave);

                }
                if (edit == REQUEST_CODE_2) {
                    addAddress(addressName, addressPhone, addressDetail, idcard);
                    setResult(SELECT_CITY, intentSave);
                }

                btnKeepAddress.setEnabled(false);
                break;
            default:
                break;
        }
    }

    /**
     * 删除收货地址
     */
    private void deleteAddress() {
        Map<String, String> params = new HashMap<>();
        params.put("userId",mUserId);
        params.put("id",addressId);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_DELETE_ADDRESS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            showToast(getResources().getString(R.string.delete_address_succeed));
                        }else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    /**
     * 编辑收货地址
     *
     * @param addressName 地址名称
     * @param addressPhone 手机号
     * @param addressDetail 详情
     * @param idcard
     */
    private void editAddress(final String addressName, final String addressPhone, final String addressDetail, final String idcard) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("id", addressId);
        params.put("address",addressDetail);
        params.put("cardNumber", idcard);
        params.put("phone", addressPhone);
        params.put("regionId", id);
        params.put("shipTo", addressName);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAl_EDIT_ADDRESS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            showToast(getResources().getString(R.string.etid_address_succeed));
                            finish();
                        } else {
                            btnKeepAddress.setEnabled(true);
                        }
                    }
                });
    }

    /**
     * 添加收货地址
     *
     * @param addressName
     * @param addressPhone
     * @param addressDetail
     * @param idcard
     */
    private void addAddress(final String addressName, final String addressPhone, final String addressDetail, final String idcard) {

        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("address",  addressDetail);
        params.put("cardNumber", idcard);
        params.put("phone", addressPhone);
        params.put("regionId",id);
        params.put("shipTo", addressName);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAl_ADD_ADDRESS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        btnKeepAddress.setEnabled(true);
                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            showToast(getResources().getString(R.string.add_address_succeed));
                            finish();
                        } else {
                            btnKeepAddress.setEnabled(true);
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST || resultCode == SELECT_CITY) {
            tvAddaddressArea.setText(data.getStringExtra("address"));
            id = data.getStringExtra("id");
        }
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法1）
     * <p>在onTouch中处理，未获焦点则隐藏</p>
     * <p>参照以下注释代码</p>
     */
    @Override
    public boolean onTouchEvent (MotionEvent event){
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_address;
    }
}
