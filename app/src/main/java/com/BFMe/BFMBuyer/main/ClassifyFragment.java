package com.BFMe.BFMBuyer.main;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.activity.SubCategoryActivity;
import com.BFMe.BFMBuyer.base.BaseFragment;
import com.BFMe.BFMBuyer.base.recycle.CommonAdapter;
import com.BFMe.BFMBuyer.base.recycle.MultiItemTypeAdapter;
import com.BFMe.BFMBuyer.base.recycle.base.ViewHolder;
import com.BFMe.BFMBuyer.javaBean.RootBean;
import com.BFMe.BFMBuyer.main.bean.ClassifyBean;
import com.BFMe.BFMBuyer.utils.GlobalContent;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.view.DividerGridItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Request;

/**
 * Description：分类Fragment
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/14 16:20
 */
public class ClassifyFragment extends BaseFragment {
    private RecyclerView classifyRV;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.activity_rv, null, false);
        classifyRV = (RecyclerView) view.findViewById(R.id.rv);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) classifyRV.getLayoutParams();
        layoutParams.setMargins(UiUtils.dip2px(10), 0, 0, 0);
        classifyRV.setPadding(0, UiUtils.dip2px(10), 0, 0);
        classifyRV.setClipToPadding(false);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        getNetData();
    }

    private void getNetData() {
        showProgress();
        OkHttpUtils
                .get()
                .url(GlobalContent.GET_HOME_CATEGORIES)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dismissProgress();
                    }

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        Logger.e("一级分类=="+response);
                        RootBean rootBean = mGson.fromJson(response, RootBean.class);
                        if (rootBean.ErrCode.equals("0")) {
                            setAdapter(mGson.fromJson(rootBean.Data, ClassifyBean.class).getCategory());
                        } else {
                            showToast(rootBean.ResponseMsg);
                        }
                    }
                });
    }

    private void setAdapter(final List<ClassifyBean.CategoryBean> category) {
        classifyRV.setLayoutManager(new GridLayoutManager(mActivity, 2));
        classifyRV.addItemDecoration(new DividerGridItemDecoration(mActivity));

        CommonAdapter<ClassifyBean.CategoryBean> adapter = new CommonAdapter<ClassifyBean.CategoryBean>(mActivity, R.layout.item_image_165_100, category) {

            @Override
            protected void convert(ViewHolder holder, ClassifyBean.CategoryBean data, int position) {
                holder.setImageGlide(R.id.iv_icon, data.getIcon());
            }
        };
        classifyRV.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, SubCategoryActivity.class);
                intent.putExtra("Title",category.get(position).getName());
                intent.putExtra("CategoryId",category.get(position).getId());
                startActivity(intent);
                bottomStartAnim();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
}
