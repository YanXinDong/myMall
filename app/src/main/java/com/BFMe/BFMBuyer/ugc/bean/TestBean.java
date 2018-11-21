package com.BFMe.BFMBuyer.ugc.bean;

/**
 * Description：瀑布流图片需要的bean类
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/9 17:35
 */
public class TestBean {
    private String url;
    private int width;
    private int height;

    public TestBean() {
    }

    public TestBean(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public TestBean(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
