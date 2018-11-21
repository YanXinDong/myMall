package com.BFMe.BFMBuyer.javaBean;

import com.BFMe.BFMBuyer.share.ShareType;

/**
 * Description：分享页面展示页面bean
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/28 14:33
 */
public class ShareBean {
    private int icon;
    private String name;
    private ShareType type;

    public ShareBean(int url, String name, ShareType type) {
        this.icon = url;
        this.name = name;
        this.type = type;
    }

    public ShareType getType() {
        return type;
    }

    public void setType(ShareType type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
