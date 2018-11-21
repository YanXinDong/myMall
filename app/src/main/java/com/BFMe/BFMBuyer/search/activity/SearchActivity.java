package com.BFMe.BFMBuyer.search.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.TagAdapter;
import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.javaBean.AllSearchBean;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.javaBean.SearchKeyWord;
import com.BFMe.BFMBuyer.model.Model;
import com.BFMe.BFMBuyer.search.adapter.RecentViewedAdapter;
import com.BFMe.BFMBuyer.search.adapter.SearchThinkAdapter;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.flowlayout.FlowTagLayout;
import com.BFMe.BFMBuyer.view.flowlayout.OnTagSelectListener;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 搜索界面
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_search_circle;
    private RadioGroup rg_sort;
    private RecyclerView rv_think;
    private TextView tv_all_search;
    private TextView tv_recently_viewed;
    private FlowTagLayout ftl_search;
    private RecyclerView rvRecentlyViewed;
    private Button btnClearRecently;
    public static SearchActivity instance = null;
    private String mType = "3";//查询类型 1:话题,2:店铺,3:商品


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translucentStatusBar();
        setContentView(R.layout.activity_search);
        instance = this;
        initView();
        setListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setAllSearch();
        setRecentViewed();
    }

    private void setAllSearch() {
        OkHttpUtils.get()
                .url(GlobalContent.GET_ALL_SEARCH_KEY_WORDS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("获取大家都在搜标签" + response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {

                            AllSearchBean allSearchBean = new Gson().fromJson(rootBean.Data, AllSearchBean.class);

                            TagAdapter<AllSearchBean.KeyWordsBean> adapter = new TagAdapter<>(SearchActivity.this);
                            ftl_search.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
                            ftl_search.setAdapter(adapter);
                            ftl_search.setOnTagSelectListener(new OnTagSelectListener() {
                                @Override
                                public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                                    if (selectedList != null && selectedList.size() > 0) {

                                        for (int i : selectedList) {
                                            AllSearchBean.KeyWordsBean item = (AllSearchBean.KeyWordsBean) parent.getAdapter().getItem(i);
                                            et_search_circle.setText(item.getName());
                                            jumpSearchResult(item.getName());
                                        }
                                    }
                                }
                            });
                            adapter.onlyAddAll(allSearchBean.getKeyWords());
                        }
                    }
                });


    }


    /**
     * 设置最近浏览的数据
     */
    private void setRecentViewed() {
        ArrayList<String> recentViewid = Model.getInstance(this)
                .getRecentViewedDAO()
                .seleteRecentViewed();
        ArrayList<String> viewedList = new ArrayList<>();
        for (int i = recentViewid.size() - 1; i >= 0; i--) {
            viewedList.add(recentViewid.get(i));
        }
        RecentViewedAdapter recentViewidAdapter = new RecentViewedAdapter(this, viewedList);
        rvRecentlyViewed.setAdapter(recentViewidAdapter);
        rvRecentlyViewed.setLayoutManager(new LinearLayoutManager(this));
        recentViewidAdapter.setOnRecentViewedClickListener(new RecentViewedAdapter.OnRecentViewedClickListener() {
            @Override
            public void OnRecentViewed(String content) {
                et_search_circle.setText(content);
                jumpSearchResult(content.trim());
            }
        });
    }


    /**
     * 监听
     */
    private void setListener() {
        //编辑框
        et_search_circle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }
                    String mContent = et_search_circle.getText().toString();
                    jumpSearchResult(mContent.trim());
                }
                return false;
            }
        });
        et_search_circle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    hideThinkList();
                } else {
                    getNetThink(s.toString());
                    showThinkList();
                }

            }
        });

        rg_sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        mType = "3";
                        break;
                    case R.id.rb2:
                        mType = "1";
                        break;
                    case R.id.rb3:
                        mType = "2";
                        break;

                }

                String search = et_search_circle.getText().toString();
                if (search.isEmpty()) {
                    hideThinkList();
                } else {
                    getNetThink(search);
                    showThinkList();
                }
            }
        });
    }

    private void getNetThink(String str) {
        Map<String, String> params = new HashMap<>();
        params.put("keyWords", str);
        params.put("type", mType);
        Logger.e("搜索联想=="+params.toString());
        OkHttpUtils
                .post()
                .params(params)
                .url(GlobalContent.POST_SEARCH_TIPS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.e("搜索联想=="+response);
                        RootBean rootBean = new Gson().fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            SearchKeyWord KeyWords = new Gson().fromJson(rootBean.Data, SearchKeyWord.class);
                            setAdapter(KeyWords);
                        } else {
                            Toast.makeText(SearchActivity.this, rootBean.ResponseMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setAdapter(SearchKeyWord keyWords) {
        final List<String> strings = keyWords.getKeyWords();
        rv_think.setLayoutManager(new LinearLayoutManager(this));
        SearchThinkAdapter adapter = new SearchThinkAdapter(this,R.layout.item_text,strings);
        rv_think.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                jumpSearchResult(strings.get(position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void jumpSearchResult(String content) {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(SearchActivity.this, getString(R.string.search_empt_hint), Toast.LENGTH_SHORT).show();
        } else {
            //如果已经存在 则不再添加至数据库
            ArrayList<String> recentViewid = Model.getInstance(this)
                    .getRecentViewedDAO()
                    .seleteRecentViewed();
            for (int i = 0; i < recentViewid.size(); i++) {
                if (content.equals(recentViewid.get(i))) {
                    Model.getInstance(this).getRecentViewedDAO().deleteExistData(content);
                }
            }
            Model.getInstance(this).getRecentViewedDAO().addRecentViewed(content);

            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("Content", content);
            intent.putExtra("searchSort", mType);
            startActivity(intent);
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);

        }
    }

    private void hideThinkList() {
        rv_think.setVisibility(View.GONE);
        tv_all_search.setVisibility(View.VISIBLE);
        tv_recently_viewed.setVisibility(View.VISIBLE);
        ftl_search.setVisibility(View.VISIBLE);
        rvRecentlyViewed.setVisibility(View.VISIBLE);
        btnClearRecently.setVisibility(View.VISIBLE);
    }

    private void showThinkList() {
        rv_think.setVisibility(View.VISIBLE);
        tv_all_search.setVisibility(View.GONE);
        tv_recently_viewed.setVisibility(View.GONE);
        ftl_search.setVisibility(View.GONE);
        rvRecentlyViewed.setVisibility(View.GONE);
        btnClearRecently.setVisibility(View.GONE);
    }

    /**
     * 初始化布局
     */
    private void initView() {

        et_search_circle = (EditText) findViewById(R.id.et_search_circle);
        TextView tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);

        rg_sort = (RadioGroup) findViewById(R.id.rg_sort);
        rv_think = (RecyclerView) findViewById(R.id.rv_think);

        tv_all_search = (TextView) findViewById(R.id.tv_all_search);
        tv_recently_viewed = (TextView) findViewById(R.id.tv_recently_viewed);
        ftl_search = (FlowTagLayout) findViewById(R.id.ftl_search);
        rvRecentlyViewed = (RecyclerView) findViewById(R.id.rv_recently_viewed);
        btnClearRecently = (Button) findViewById(R.id.btn_clear_recently);

        et_search_circle.setOnClickListener(this);
        tv_search_cancel.setOnClickListener(this);
        btnClearRecently.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_cancel:
                finish();
                overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
                break;
            case R.id.btn_clear_recently:
                Model.getInstance(this).getRecentViewedDAO().deleteRecentViewed();
                setRecentViewed();
                break;
        }
    }

    /**
     * 点击屏幕空白区域隐藏软键盘（方法2）
     * <p>根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p >
     * <p>需重写dispatchTouchEvent</p >
     * <p>参照以下注释代码</p >
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    // 获取InputMethodManager，隐藏软键盘
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
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
