package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.GradeAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.GradeBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.utils.CacheUtils;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.DividerItemDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Description：积分明细
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/14 18:30
 */
public class GradeActivity extends IBaseActivity {

    private XRecyclerView listview;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFREH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态

    private int pageNo = 1;//记载的页数
    private GradeAdapter adapter;

    @BindView(R.id.tv_grade)
    TextView tvGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.grade_info));
        listview = (XRecyclerView) findViewById(R.id.listview_grade);
        listview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        listview.setFootViewText(getString(R.string.load), getString(R.string.no_more_data));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String myGrade = CacheUtils.getString(this, "myGrade");
        tvGrade.setText(myGrade);
        setData();
        setListener();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_grade;
    }

    private void setData() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", mUserId);
        params.put("pageSize", 20 + "");
        params.put("pageNo", pageNo + "");
        OkHttpUtils
                .post()
                .url(GlobalContent.GLOBAL_GET_INTEGRALDETAIL)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("GradeActivity" + response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        String data = rootBean.Data;
                        if (rootBean.ErrCode.equals("0")) {
                            GradeBean gradeBean = mGson.fromJson(data, GradeBean.class);
                            showShopCommodity(gradeBean);
                        }
                    }
                });
    }

    /**
     * 监听
     */
    private void setListener() {
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                state = STATE_REFREH;
                setData();
            }

            @Override
            public void onLoadMore() {
                pageNo += 1;
                state = STATE_MORE;
                setData();
            }
        });

    }

    private void showShopCommodity(GradeBean gradeBean) {
        List<GradeBean.MemberIntegralRecordBean> memberIntegralRecord = gradeBean.getMemberIntegralRecord();

        switch (state) {
            case STATE_NORMAL:
                listview.setLayoutManager(new LinearLayoutManager(this));
                adapter = new GradeAdapter(GradeActivity.this, memberIntegralRecord);
                listview.setAdapter(adapter);
                break;

            case STATE_REFREH:
                if (adapter != null) {
                    adapter.cleanData();
                    adapter.addData(memberIntegralRecord);
                } else {
                    listview.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new GradeAdapter(GradeActivity.this, memberIntegralRecord);
                    listview.setAdapter(adapter);
                }
                listview.refreshComplete();
                break;

            case STATE_MORE:
                if (adapter != null) {
                    if (adapter.getItemCount() >= gradeBean.getPageInfo().getTotalItems()) {
                        listview.setNoMore(true);
                    } else {
                        adapter.addData(adapter.getItemCount(), memberIntegralRecord);
                        listview.loadMoreComplete();
                    }

                } else {
                    listview.setLayoutManager(new LinearLayoutManager(this));
                    adapter = new GradeAdapter(GradeActivity.this, memberIntegralRecord);
                    listview.setAdapter(adapter);
                }
                break;
        }
    }
}
