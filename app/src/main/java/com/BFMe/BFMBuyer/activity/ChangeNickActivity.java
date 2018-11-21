package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Description：修改昵称
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/18 09:34
 */
public class ChangeNickActivity extends IBaseActivity {
    private EditText etChangenick;
    private Button btnSavenick;
    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.change_nick));
        etChangenick = (EditText) findViewById(R.id.et_changenick);
        btnSavenick = (Button) findViewById(R.id.btn_savenick);

        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_changnick;
    }


    private void initData() {

        btnSavenick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = etChangenick.getText().toString().trim();
                if (!TextUtils.isEmpty(nick)) {
                    changeNick();
                } else {
                    showToast(getResources().getString(R.string.nick_no_null));
                }
            }
        });
    }

    private void changeNick() {
        showProgress();
        Map<String, String> params = new HashMap<>();
        params.put("userId", mUserId);
        params.put("Nick", nick);
        OkHttpUtils
                .post()
                .url(GlobalContent.GLABAL_CHNAGE_NICK)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String errCode = rootBean.ErrCode;
                        if ("0".equals(errCode)) {
                            showToast(getResources().getString(R.string.nick_modification_succeed));
                            Intent intent = getIntent().putExtra("NICK", nick);
                            setResult(0, intent);
                            finish();
                            exitAnim();
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }
}
