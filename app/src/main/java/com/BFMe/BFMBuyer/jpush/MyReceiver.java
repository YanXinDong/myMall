package com.BFMe.BFMBuyer.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.javaBean.JpushBean;
import com.BFMe.BFMBuyer.ugc.activity.LogisticsActivity;
import com.BFMe.BFMBuyer.ugc.activity.NewsCommentActivity;
import com.BFMe.BFMBuyer.ugc.activity.OfficialActivity;
import com.BFMe.BFMBuyer.ugc.activity.TopicSelectActivity;
import com.BFMe.BFMBuyer.utils.logger.Logger;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Logger.d( "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Logger.d( "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...
			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Logger.d( "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {//收到推送的通知
				Logger.d( "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Logger.d( "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {//用户点击打开了通知
				Logger.d( "[MyReceiver] 用户点击打开了通知");
				analysisBundle(context,bundle);
			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {//富文本推送
				Logger.d( "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {//链接状态的改变监听
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w( "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Logger.d( "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	/**
	 * 处理推送进来的参数并跳转到指定消息列表
	 * @param context
	 * @param bundle
     */
	private void analysisBundle(Context context, Bundle bundle) {
		String string = bundle.getString(JPushInterface.EXTRA_EXTRA);
		JpushBean jpushBean = new Gson().fromJson(string, JpushBean.class);
		Intent intent = new Intent();
		String title = "";
		switch (jpushBean.getMessageType()){//消息类型  1：官方推荐，2：关注精选，3：物流通知，4：新的评论
			case 1:
				intent.setClass(context, OfficialActivity.class);
				title = context.getString(R.string.official_recommend);
			    break;
			case 2:
				intent.setClass(context, TopicSelectActivity.class);
				title = context.getString(R.string.topic_choiceness);
				break;
			case 3:
				intent.setClass(context, LogisticsActivity.class);
				title = context.getString(R.string.logistics_inform);
				break;
			case 4:
				intent.setClass(context, NewsCommentActivity.class);
				break;

		}
		intent.putExtra("TITLE", title);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i( "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e( "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

}
