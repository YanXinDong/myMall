package com.BFMe.BFMBuyer.activity;

import android.app.Activity;
import android.os.Bundle;

import com.BFMe.BFMBuyer.utils.LocalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class BaseActivity extends Activity {
	private JSONObject mJsonObj;//把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	private String mJsonData;//json格式的地址信息
	protected String id;
	protected String cityId;

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 市 values - id
	 */
	protected Map<String, String> mCityIdDatasMap = new HashMap<>();
	/**
	 * key - 区 values - id
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName ="";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initJsonData();
	}

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas()
	{
		try {
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
				String province = jsonP.getString("name");// 省名字
				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("city");
				} catch (Exception e1) {
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("name");// 市名字
					cityId = jsonCity.getString("id");
					mCitiesDatas[j] = city;
					mCityIdDatasMap.put(mCitiesDatas[j],cityId);
					JSONArray jsonAreas = null;
					try {
						jsonAreas = jsonCity.getJSONArray("county");
					} catch (Exception e) {
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
					for (int k = 0; k < jsonAreas.length(); k++) {
						String area = jsonAreas.getJSONObject(k).getString("name");// 区域的名称
						id = jsonAreas.getJSONObject(k).getString("id");
						mAreasDatas[k] = area;
						mZipcodeDatasMap.put(city+mAreasDatas[k],id);
					}
					mDistrictDatasMap.put(city,mAreasDatas);

				}

				mCitisDatasMap.put(province,mCitiesDatas);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mJsonObj = null;
	}
	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private void initJsonData() {
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = null;

			if(LocalUtils.isLocalMainland()){
				is = getAssets().open("address.json");
			}else {
				is = getAssets().open("address_hk.json");
			}

			int len = -1;
			byte[] buf = new byte[is.available()];
			while ((len = is.read(buf)) != -1) {
				sb.append(new String(buf,0,len,"utf-8"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
			mJsonData = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
