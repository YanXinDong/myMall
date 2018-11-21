package com.BFMe.BFMBuyer.ugc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.base.IBaseActivity;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class PushSettingActivity extends IBaseActivity {

    @BindView(R.id.btn_switch)
    Switch btn_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.message_push_setting));

        if (JPushInterface.isPushStopped(this)) {
            btn_switch.setChecked(false);
        } else {
            btn_switch.setChecked(true);
        }

        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JPushInterface.resumePush(getApplicationContext());
                } else {
                    JPushInterface.stopPush(getApplicationContext());
                }
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_push_setting;
    }
}
