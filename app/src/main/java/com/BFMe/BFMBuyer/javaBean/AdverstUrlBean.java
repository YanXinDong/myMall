package com.BFMe.BFMBuyer.javaBean;

import java.util.ArrayList;

/**
 * Description: 广告轮播条
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/19 20:44
 */
public class AdverstUrlBean {
    //    {
    //        "Data": [
    //        {
    //            "ImageUrl": "http://192.168.1.149:8088/temp/b1b590142d5c824a.jpg",
    //                "Url": "http://"
    //        },
    //        {
    //            "ImageUrl": "http://img01.bfme.com/temp/201604121437072690370.jpg",
    //                "Url": "www.baidu.com"
    //        },
    //        {
    //            "ImageUrl": "http://192.168.1.149:8088/temp/5d1546eee541a764.jpg",
    //                "Url": "/m-weixin/topic/detail/85?isMobileHomeTopic=false"
    //        }
    //        ]

    public ArrayList<Datas> Data;

    public class Datas {
        public String ImageUrl;
        public String Url;
    }
}
