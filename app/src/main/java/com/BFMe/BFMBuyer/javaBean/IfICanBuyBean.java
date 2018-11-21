package com.BFMe.BFMBuyer.javaBean;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/11/16 18:01
 */
public class IfICanBuyBean {

    /**
     * data : true
     * tips : "抱歉，该商品为新用户专享福利商品，看看其他商品吧！"
     */

    private boolean data;
    private String tips;

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
