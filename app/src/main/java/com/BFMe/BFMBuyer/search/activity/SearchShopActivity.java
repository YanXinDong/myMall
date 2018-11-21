package com.BFMe.BFMBuyer.search.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.commodity.activity.ProducetDetailsActivity;
import com.BFMe.BFMBuyer.search.adapter.SearchShopAdapter;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.SearchShopBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshBase;
import com.netease.nim.uikit.common.ui.ptr.PullToRefreshGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 店铺内搜索
 */
public class SearchShopActivity extends AppCompatActivity {

    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 0;
    /**
     * 刷新状态
     */
    private static final int STATE_REFREH = 1;
    /**
     * 加载更多状态
     */
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;//默认为正常状态


    //加载的页数
    private int PAGENO = 1;
    //一页的数据
    private int PAGESIZE = 10;

    //联网地址
    String mInspirationFunUrl;

    //输入的内容
    String mContent;
    private EditText et_search_circle2;
    private TextView tv_search_cancel;
    private PullToRefreshGridView gv_search_shop;//带下拉刷新的gridView

    private FrameLayout pb_load;
    private TextView tv_load;
    private SearchShopAdapter mSearchShopAdapter;
    private SearchShopBean mSearchShopBean;
    private String mShopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translucentStatusBar();
        setContentView(R.layout.activity_search_shop);

        initView();
        initData();
        setListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mShopId = getIntent().getStringExtra(GlobalContent.SHOP_ID);
    }
    /**
     * 监听
     */
    private void setListener() {
        //编辑框
        et_search_circle2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(SearchShopActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    mContent = et_search_circle2.getText().toString();
                    if (TextUtils.isEmpty(mContent)) {
                        Toast.makeText(SearchShopActivity.this, getString(R.string.search_empt_hint), Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        pb_load.setVisibility(View.VISIBLE);
                        tv_load.setVisibility(View.VISIBLE);

                        PAGENO = 1;
                        state = STATE_NORMAL;
                        mInspirationFunUrl = GlobalContent.GLOBAL_SHOP_SORT;
                        getNetShopProduct();
                    }
                    return true;
                }
                return false;
            }
        });

        //取消按钮
        tv_search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
            }
        });

        gv_search_shop.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                PAGENO = 1;
                state = STATE_REFREH;
                mInspirationFunUrl = GlobalContent.GLOBAL_SHOP_SORT;
                getNetShopProduct();
            }

            //上拉加载更多
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

                PAGENO += 1;
                state = STATE_MORE;
                mInspirationFunUrl = GlobalContent.GLOBAL_SHOP_SORT;
                getNetShopProduct();
            }
        });

    }

    /**
     * 获取店铺内商品
     */
    private void getNetShopProduct() {
        OkHttpUtils
                .post()
                .url(mInspirationFunUrl)
                .addParams("ShopId", mShopId)
                .addParams("PageNo", PAGENO + "")
                .addParams("PageSize", PAGESIZE + "")
                .addParams("KeyWords", mContent+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        getNetShopProduct();
                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("店铺内搜索数据"+response);
                        parseJson(response);
                    }
                });
    }
    /**
     * 解析数据
     *
     * @param response
     */
    private void parseJson(String response) {
        //隐藏加载布局
        pb_load.setVisibility(View.GONE);
        tv_load.setVisibility(View.GONE);

        Gson gson = new Gson();

        RootBean rootBean = gson.fromJson(response, RootBean.class);
        String data = rootBean.Data;

        if (!TextUtils.isEmpty(data)) {
            mSearchShopBean = new Gson().fromJson(data, SearchShopBean.class);
            if (mSearchShopBean.getTotal() == 0) {
                tv_load.setVisibility(View.VISIBLE);
                tv_load.setText(getString(R.string.search_no_result));
            } else {
                tv_load.setVisibility(View.GONE);
            }
            showData();
        }
    }

    /**
     * 处理数据
     */
    private void showData() {

        switch (state) {
            //正常状态
            case STATE_NORMAL:

                mSearchShopAdapter = new SearchShopAdapter(SearchShopActivity.this, mSearchShopBean.getProducts());
                gv_search_shop.setAdapter(mSearchShopAdapter);

                break;

            //刷新状态
            case STATE_REFREH:
                if (mSearchShopAdapter != null) {
                    mSearchShopAdapter.cleanData();
                    mSearchShopAdapter.addData(mSearchShopBean.getProducts());
                } else {
                    mSearchShopAdapter = new SearchShopAdapter(SearchShopActivity.this, mSearchShopBean.getProducts());
                    gv_search_shop.setAdapter(mSearchShopAdapter);
                }
                gv_search_shop.onRefreshComplete();

                break;

            //加载更多状态
            case STATE_MORE:
                if (mSearchShopAdapter != null) {
                    if (mSearchShopAdapter.getCount() >= mSearchShopBean.getTotal()) {
                        Toast.makeText(SearchShopActivity.this, getString(R.string.no_more_data1), Toast.LENGTH_SHORT).show();
                        gv_search_shop.onRefreshComplete();
                        return;
                    }
                    //在原来的基础上添加数据
                    mSearchShopAdapter.addData(mSearchShopAdapter.getCount(), mSearchShopBean.getProducts());
                } else {
                    mSearchShopAdapter = new SearchShopAdapter(SearchShopActivity.this, mSearchShopBean.getProducts());
                    gv_search_shop.setAdapter(mSearchShopAdapter);
                }
                gv_search_shop.onRefreshComplete();//隐藏上拉加载
                break;
        }
        //item的点击事件监听
        gv_search_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SearchShopActivity.this, ProducetDetailsActivity.class);
                intent.putExtra("productId", mSearchShopAdapter.getDatas().get(position).getProductId());
                intent.putExtra("ShopId", mSearchShopAdapter.getDatas().get(position).getShopId());
                startActivity(intent);
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {

        et_search_circle2 = (EditText) findViewById(R.id.et_search_circle2);
        tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);
        gv_search_shop = (PullToRefreshGridView) findViewById(R.id.gv_search_shop);
        pb_load = (FrameLayout) findViewById(R.id.pb_load);
        tv_load = (TextView) findViewById(R.id.tv_load);
    }


    private void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
