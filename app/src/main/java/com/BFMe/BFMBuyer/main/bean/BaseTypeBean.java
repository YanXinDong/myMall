package com.BFMe.BFMBuyer.main.bean;

/**
 * Description：通用分类型bean
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/15 11:17
 */
public class BaseTypeBean {
    private String type;
    private Object data;

    public BaseTypeBean() {
    }

    public BaseTypeBean(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
