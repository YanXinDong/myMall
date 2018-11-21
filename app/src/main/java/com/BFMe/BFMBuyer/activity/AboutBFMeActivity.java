package com.BFMe.BFMBuyer.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.adapter.MapInfoAdapter;
import com.BFMe.BFMBuyer.base.IBaseActivity;
import com.BFMe.BFMBuyer.javaBean.MapAdInfoBean;
import com.BFMe.BFMBuyer.utils.PackageUtils;
import com.BFMe.BFMBuyer.utils.Utils;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;

/**
 * Description：关于BFMe
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/20 14:25
 */
public class AboutBFMeActivity extends IBaseActivity implements PoiSearch.OnPoiSearchListener {
    private ListView listview;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;// Poi查询条件类
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    private double Longitude = 0;//经度
    private double Latitude = 0;//纬度
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    Longitude = amapLocation.getLongitude();
                    Latitude = amapLocation.getLatitude();
                    Logger.e("经度=" + amapLocation.getLongitude() + "纬度=" + amapLocation.getLatitude() + "");

                    Logger.e("省=" + amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict()
                            + amapLocation.getStreet() + amapLocation.getStreetNum());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    private TextView tv_version_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();


    }

    private void initData() {
        getCurrentPosition();
        String channelName = Utils.getChannelName(this);
        String versionName = PackageUtils.getVersionName(this);

        switch (channelName) {
            case "BFMe":
                channelName = "BFMe";
                break;
            case "yingyongbao":
                channelName = "应用宝";
                break;
            case "GooglePlay":
                channelName = "GooglePlay";
                break;
            case "xiaomi":
                channelName = "小米";
                break;
            case "huawei":
                channelName = "华为";
                break;
            case "_360":
                channelName = "360";
                break;
            case "baidu":
                channelName = "百度";
                break;
            case "vivo":
                channelName = "vivo";
                break;
            case "oppo":
                channelName = "OPPO";
                break;
            default:
                channelName = "";
                break;

        }

        tv_version_number.setText(versionName + "_" + channelName);

    }

    private void getCurrentPosition() {
    /*    //初始化定位
   mLocationClient = new AMapLocationClient(getApplicationContext());
   //设置定位回调监听
   mLocationClient.setLocationListener(mLocationListener);

   //初始化AMapLocationClientOption对象
   mLocationOption = new AMapLocationClientOption();
  *//* //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
   mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);*//*
   //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
   mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
   //获取一次定位结果：
   mLocationOption.setOnceLocation(true);
   //设置是否强制刷新WIFI，默认为true，强制刷新。
   mLocationOption.setWifiActiveScan(false);
   //设置是否返回地址信息（默认返回地址信息）
   mLocationOption.setNeedAddress(true);
   //关闭缓存机制
   mLocationOption.setLocationCacheEnable(false);
   //给定位客户端对象设置定位参数
   mLocationClient.setLocationOption(mLocationOption);
   //启动定位
   mLocationClient.startLocation();*/
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        tv_version_number = (TextView) findViewById(R.id.tv);

        setBackButtonVisibility(View.VISIBLE);
        tv_title.setText("Buy For Me");

    }

    @Override
    public int initLayout() {
        return R.layout.activity_aboutbfme;
    }


    public void btn(View v) {
        LatLonPoint lp = new LatLonPoint(Latitude, Longitude);

        query = new PoiSearch.Query("", "汽车服务|汽车销售|汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页 设置页数可根据listAddress集合的长度来设置  暂时先放这
        // 另外当查询结果为空时 暂未做处理

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
        // 设置搜索区域为以lp点为圆心，其周围5000米范围
        poiSearch.searchPOIAsyn();// 异步搜索
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {

        ArrayList<MapAdInfoBean> listAddress = new ArrayList();
        for (int i = 0; i < poiResult.getPois().size(); i++) {
            String address = poiResult.getPois().get(i).getCityName() + poiResult.getPois().get(i).getAdName()
                    + poiResult.getPois().get(i).getSnippet();
            MapAdInfoBean mapAdInfoBean = new MapAdInfoBean(address, poiResult.getPois().get(i).getTitle());
            listAddress.add(mapAdInfoBean);
        }
        listview.setAdapter(new MapInfoAdapter(listAddress, AboutBFMeActivity.this));
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}


