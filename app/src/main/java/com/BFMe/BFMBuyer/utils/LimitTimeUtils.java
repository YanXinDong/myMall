package com.BFMe.BFMBuyer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:   获取限时购剩余的时间
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/22 15:21
 */
public class LimitTimeUtils {


    private static String time;

    //根据开始时间，结束时间判断剩余时间内
    public static String getTime(String start,String end) {

        try {
            //获取当前的时间
            long currentTime = new Date().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = sdf.parse(start).getTime();
            long endTime = sdf.parse(end).getTime();//结束时间
            long romoteTome = endTime - currentTime;//剩余时间

            if (romoteTome < 0) {
                return "0"+0; //  0：已结束
            } else {
                if(startTime>currentTime){
                    return "1"+romoteTome;//未开抢
                }else{

                   return "2"+romoteTome;//已开抢
               }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
