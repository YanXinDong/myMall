package com.BFMe.BFMBuyer.main.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/7 11:04
 */
public class ShopHomeBean {

    /**
     * ImagePath : http://img01.baifomi.com/lib/shopHome/201795161553.png
     * Value : 1
     */

    private List<ShopDataBean> ShopData;

    public List<ShopDataBean> getShopData() {
        return ShopData;
    }

    public void setShopData(List<ShopDataBean> ShopData) {
        this.ShopData = ShopData;
    }

    public static class ShopDataBean {
        private String ImagePath;
        private int Value;

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public int getValue() {
            return Value;
        }

        public void setValue(int Value) {
            this.Value = Value;
        }
    }
}
