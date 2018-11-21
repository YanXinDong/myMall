package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：店铺列表bean
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/8 14:44
 */
public class ShopListData {

    /**
     * Area : 香港
     * Description :
     * Id : 1003
     * ImageUrl :
     * ShopName : lfx测试店铺2
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String Area;
        private String Description;
        private String Id;
        private String ImageUrl;
        private String ShopName;

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }
    }
}
