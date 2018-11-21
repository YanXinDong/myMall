package com.BFMe.BFMBuyer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.BFMe.BFMBuyer.widget.OnWheelChangedListener;
import com.BFMe.BFMBuyer.widget.WheelView;
import com.BFMe.BFMBuyer.widget.adapters.ArrayWheelAdapter;

/**
 * 地址选择页面
 */
public class SelectCitiesActivity extends BaseActivity implements View.OnClickListener, OnWheelChangedListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm;
    public static final int SELECT_CITY = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selectcity);
        //禁止窗口以外的点击事件
        setFinishOnTouchOutside(false);
        setUpViews();
        setUpListener();
        setUpData();
    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(SelectCitiesActivity.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentCityName+mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }

        ArrayWheelAdapter<String> stringArrayWheelAdapter = new ArrayWheelAdapter<>(this, areas);
        mViewDistrict.setViewAdapter(stringArrayWheelAdapter);
        mViewDistrict.setCurrentItem(0);

        int currentItem = mViewDistrict.getCurrentItem();
        if(currentItem < stringArrayWheelAdapter.getItemsCount() - 1) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[currentItem];
        }else {
            mCurrentDistrictName = "";
        }

    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                showSelectedResult();
                break;
            default:
                break;
        }
    }

    private void showSelectedResult() {
        Intent intent = new Intent();

        intent.putExtra("address", mCurrentProviceName + " " + mCurrentCityName + " "
                + mCurrentDistrictName);

        if(mZipcodeDatasMap.get(mCurrentCityName+mCurrentDistrictName) == null) {
            Logger.e(  "idTest"+mCityIdDatasMap.get(mCurrentCityName+mCurrentCityName));
            intent.putExtra("id", mCityIdDatasMap.get(mCurrentCityName));
        }else {
            Logger.e(  "idTest"+mZipcodeDatasMap.get(mCurrentCityName+mCurrentDistrictName));
            intent.putExtra("id",mZipcodeDatasMap.get(mCurrentCityName+mCurrentDistrictName));
        }
        setResult(SELECT_CITY, intent);
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("address", mCurrentProviceName + " " + mCurrentCityName + " "
                    + mCurrentDistrictName);

            if(mZipcodeDatasMap.get(mCurrentCityName+mCurrentDistrictName) == null) {
                intent.putExtra("id", mCityIdDatasMap.get(mCurrentCityName));
            }else {
                intent.putExtra("id", mZipcodeDatasMap.get(mCurrentCityName+mCurrentDistrictName));
            }
            setResult(SELECT_CITY, intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

