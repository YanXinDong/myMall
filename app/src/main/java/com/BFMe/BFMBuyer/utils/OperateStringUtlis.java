package com.BFMe.BFMBuyer.utils;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/8/11 15:26
 */
public class OperateStringUtlis {
    /**
     * 用户名加密的id处理
     */
    public static String getUerId(String base64ID){
        String UserId = base64ID.replace("+","%2b");
        return  UserId;
    }

    /**
     *  用户名加密的id还原出来
     */
    public static String getUerIdBack(String base64ID){
        String UserId = base64ID.replace("%2b","+");
        return  UserId;
    }
}
