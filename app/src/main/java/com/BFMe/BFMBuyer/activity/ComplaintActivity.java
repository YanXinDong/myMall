package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.OperateStringUtlis;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 维权页面
 */
public class ComplaintActivity extends IBaseActivity {

    private EditText et_complaint;//文本框
    private String mUserId;
    private String mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        mUserId = CacheUtils.getString(this, GlobalContent.USER);
        mOrderId = getIntent().getStringExtra(GlobalContent.ORDER_ID);

    }


    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.complaint));
        et_complaint = (EditText) findViewById(R.id.et_complaint);
        Button btn_complaint_submit = (Button) findViewById(R.id.btn_complaint_submit);

        btn_complaint_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_complaint.getText().toString())) {
                    showToast(getString(R.string.input_complaint_content_hint));
                } else {
                    submitInfo();
                }

            }
        });
    }

    private void submitInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", OperateStringUtlis.getUerIdBack(mUserId));
        params.put("OrderId", mOrderId);
        params.put("ComplaintReason", et_complaint.getText().toString());
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_COMPLAINT)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (Integer.parseInt(rootBean.ErrCode) == 0) {
                            showToast(getString(R.string.submit_succeed));
                            finish();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_complaint;
    }
}
