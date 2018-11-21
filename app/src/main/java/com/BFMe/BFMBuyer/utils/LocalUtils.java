package com.BFMe.BFMBuyer.utils;

/**
 * Created by BFMe.miao on 2018/3/1.
 */

public class LocalUtils {
    /**
     * 是否是大陆地区
     * 首先判断用户是否是选择了，然后再次根据语言选择
     *
     * @return
     */
    public static boolean isLocalMainland() {
        return true;

//        boolean is_hongkong = CacheUtils.getBoolean(MyApplication.getContext(), GlobalContent.IS_HONGKONG);//false  是大陆
//
//        if(is_hongkong){
//            return false;
//        }else {
//            return true;
//        }
//        Locale locale = Locale.getDefault();
//        String language = locale.getLanguage();
//        String country = locale.getCountry();
//        if(language.equals("zh")){
//            if(!country.equals("CN")){
//                return false;
//            }
//        }
//        return true;
    }
}
