package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.LocalUtils;
import com.BFMe.BFMBuyer.utils.RegexUtils;
import com.BFMe.BFMBuyer.utils.StringUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * 身份信息填写修改页面
 */
public class EditIdentityActivity extends IBaseActivity implements View.OnClickListener {

    private EditText et_name, et_id_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.identity_authentication));
        et_name = (EditText) findViewById(R.id.et_name);
        et_id_number = (EditText) findViewById(R.id.et_id_number);

        findViewById(R.id.btn_savenick).setOnClickListener(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_edit_identity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_savenick:
                editIdentityInfo();
                break;
        }
    }

    private void editIdentityInfo() {
        boolean isSpace = verifyData();
        if (isSpace) {
            updateNameAndCardNo();
        }
    }

    /**
     * 修改身份信息
     */
    private void updateNameAndCardNo() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);
        params.put("RealName", et_name.getText().toString());
        params.put("CardNo", et_id_number.getText().toString());

        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_UPDATE_NAME_AND_CARD_NO)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("身份信息修改==" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            showToast(getString(R.string.identity_authentication_edit_succeed));
                            finish();
                            exitAnim();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private boolean verifyData() {
        String name = et_name.getText().toString();
        String idNumber = et_id_number.getText().toString();

        if (StringUtils.isSpace(name) || StringUtils.isSpace(idNumber)) {
            showToast(getString(R.string.input_info_is_empt_hint));
            return false;
        }
        if(!LocalUtils.isLocalMainland()){
            return true;
        }

        if (!RegexUtils.isIDCard18(idNumber)) {
            showToast(getString(R.string.id_number_format_error));
            return false;
        }

        return true;

    }
}
