package com.BFMe.BFMBuyer.javaBean;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/2/7 15:53
 */
public class MapAdInfoBean {
    private String address;
    private String title;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MapAdInfoBean(String address, String title) {
        this.address = address;
        this.title = title;
    }
}
