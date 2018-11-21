package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.application.MyApplication;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.EncryptCode;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Description：修改密码
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/18 14:58
 */
public class ChangePasswordActivity extends IBaseActivity {
    private EditText etOldpwd;
    private EditText etNewpwd1;
    private EditText etNewpwd2;
    private Button btnSavenick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.change_pwd));

        etOldpwd = (EditText) findViewById(R.id.et_oldpwd);
        etNewpwd1 = (EditText) findViewById(R.id.et_newpwd1);
        etNewpwd2 = (EditText) findViewById(R.id.et_newpwd2);
        btnSavenick = (Button) findViewById(R.id.btn_savenick);
        initData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_password_change;
    }

    private void initData() {

        btnSavenick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldpwd = etOldpwd.getText().toString().trim();
                final String newpwd1 = etNewpwd1.getText().toString().trim();
                String newpwd2 = etNewpwd2.getText().toString().trim();
                if ("".equals(oldpwd) || "".equals(newpwd1) || "".equals(newpwd2)) {
                    showToast(getResources().getString(R.string.info_fill_out_error));
                    return;
                }

                if (newpwd1.length() > 20 || newpwd1.length() < 6) {
                    showToast(getResources().getString(R.string.pwd_illegal));
                    return;
                }
                if (!newpwd1.equals(newpwd2)) {
                    showToast(getResources().getString(R.string.pwd_inconformity));
                    return;
                }

                changePassword(oldpwd, newpwd1);
            }
        });
    }

    /**
     * 修改密码
     * @param oldpwd 旧密码
     * @param newpwd1 新密码
     */
    private void changePassword(final String oldpwd, final String newpwd1) {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);

        try {
            params.put("OldPassword", EncryptCode.encrypt(oldpwd));
            params.put("NewPassword", EncryptCode.encrypt(newpwd1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .post()
                .url(GlobalContent.GLABAL_CHNAGE_PWD)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if ("0".equals(rootBean.ErrCode)) {
                            //修改是否登陆的boolear值
                            showToast(getResources().getString(R.string.pwd_change_succeed));
                            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                            CacheUtils.putBoolean(ChangePasswordActivity.this, GlobalContent.ISLOGIN, false);
                            MyApplication.getInstance().exit();
                            finish();
                            startAnim();
                        }
                    }
                });
    }
}
