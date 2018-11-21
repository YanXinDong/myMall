package com.BFMe.BFMBuyer.utils;

import android.content.Context;
import android.content.res.Resources;

import com.BFMe.BFMBuyer.R;

/**
 * Created by BrainWang on 05/01/2016.
 * 拦截图文专题页面不需要的div
 */
public class ADFilterTool {
    public static String getClearAdDivJs(Context context){
        String js = "javascript:function remove(){";
        Resources res = context.getResources();
        String[] adDivs = res.getStringArray(R.array.adBlockDiv);
        for(int i=0;i<adDivs.length;i++){
            js += "var adDiv"+i+"= document.getElementsByClassName('"+adDivs[i]+"')[0];if(adDiv"+i+" != null)adDiv"+i+".parentNode.removeChild(adDiv"+i+");";
        }
        js += "}remove();";
        return js;
    }
}