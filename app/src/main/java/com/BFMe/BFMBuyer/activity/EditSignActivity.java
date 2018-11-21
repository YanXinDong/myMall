package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * 编辑个性签名
 */
public class EditSignActivity extends IBaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvCommit;
    private EditText etContent;
    private TextView tvNumber;
    private TextView tvNumber2;
    private boolean is_from_sign;
    private int maxNum;
    private String url;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sign);
        is_from_sign = getIntent().getBooleanExtra("IS_FROM_SIGN", false);
        content = getIntent().getStringExtra("Content");
        initView();
        initData();

    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCommit = (TextView) findViewById(R.id.tv_commit);
        etContent = (EditText) findViewById(R.id.et_content);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        tvNumber2 = (TextView) findViewById(R.id.tv_number2);
        if (is_from_sign) {
            if (TextUtils.isEmpty(content)) {
                etContent.setHint(getString(R.string.sign_hint));
            } else {
                etContent.setText(content);
            }
            tvTitle.setText(getString(R.string.personalized_sing));
            tvNumber2.setText("/80");
            maxNum = 80;
        } else {
            etContent.setHint(getString(R.string.complaint_hint));
            tvTitle.setText(getString(R.string.complaint));
            tvNumber2.setText("/1000");
            maxNum = 1000;
        }
        tvCommit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void initData() {
        setEditTextListener();
    }

    private void postData() {
        final String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast(getString(R.string.input_info_is_empt_hint));
            return;
        }
        Map<String, String> map = new HashMap<>();
        if (is_from_sign) {
            map.put("UserId", mUserId);
            map.put("IntroduceMyself", content);
            url = GlobalContent.POST_USER_BUYER_INFO;//个性签名
        } else {
            map.put("userId", mUserId);
            map.put("content", content);
            url = GlobalContent.POST_USER_FEED_BACK;//投诉反馈
        }
        Logger.e(content + "  " + mUserId);
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("提交投诉或更改签名异常" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e(response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if ("0".equals(rootBean.ErrCode)) {
                            showToast(getString(R.string.submit_succeed));
                            Intent intent = getIntent().putExtra("SIGN", content);
                            setResult(1, intent);
                            finish();
                            exitAnim();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }

                    }
                });
    }


    private void setEditTextListener() {
        etContent.addTextChangedListener(new TextWatcher() {
            CharSequence temp;
            int num = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num + s.length();
                tvNumber.setText(number + "");
                if (temp.length() > maxNum) {
                    showToast(getString(R.string.number_max));
                    int selectionStart = etContent.getSelectionStart();
                    int selectionEnd = etContent.getSelectionEnd();
                    s.delete(selectionStart - 1, selectionEnd);
                }
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_edit_sign;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                exitAnim();
                break;
            case R.id.tv_commit:
                postData();
                break;
        }
    }
}
