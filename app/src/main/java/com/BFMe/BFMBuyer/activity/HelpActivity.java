package com.BFMe.BFMBuyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.HelpAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.HelpBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

/**
 * 帮助中心
 */
public class HelpActivity extends IBaseActivity {

    private ListView lv_help_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化布局
        initView();
        //初始化数据
        initData();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_help;
    }

    /**
     * 获取数据
     */
    private void initData() {
        showProgress();
        OkHttpUtils
                .get()
                .url(GlobalContent.GLOBAL_HELP)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        //解析返回的数据
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String data = rootBean.Data;
                        if (rootBean.ErrCode.equals("0")) {
                            //解析Data
                            HelpBean helpBean = new Gson().fromJson(data, HelpBean.class);
                            showData(helpBean);
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }

                    }
                });
    }

    /**
     * 显示布局数据
     *
     * @param helpBean
     */
    private void showData(final HelpBean helpBean) {
        final ArrayList<HelpBean.HelpCenterNavData> list = new ArrayList<>();
        for (int i = 0; i < helpBean.getHelpCenterNav().size(); i++) {
            if (helpBean.getHelpCenterNav().get(i).isIsRelease()) {
                list.add(helpBean.getHelpCenterNav().get(i));
            }
        }
        //设置适配器
        HelpAdapter helpAdapter = new HelpAdapter(HelpActivity.this, list);
        lv_help_center.setAdapter(helpAdapter);

        //item点击监听
        lv_help_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(HelpActivity.this, HelpDetailsActivity.class);
                intent.putExtra("help_id", list.get(position).getId());
                startActivity(intent);
                startAnim();
            }
        });
    }

    private void initView() {

        //返回按钮
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.help_centre));

        lv_help_center = (ListView) findViewById(R.id.lv_help_center);
    }
}
