package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：从国家馆获取店铺列表
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/8 10:53
 */
public class ShopListsBean1 {

    /**
     * Address : 日本
     * Description : ##########################33
     * Id : 0
     * ImagePath :
     * ProductId : 0
     * SaleCounts : 0
     * ShopId : 804
     * ShopName : TTTTTTTTTTT
     */

    private List<AreasBean> areas;

    public List<AreasBean> getAreas() {
        return areas;
    }

    public void setAreas(List<AreasBean> areas) {
        this.areas = areas;
    }

    public static class AreasBean {
        private String Address;
        private String Description;
        private int Id;
        private String ImagePath;
        private int ProductId;
        private int SaleCounts;
        private int ShopId;
        private String ShopName;

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public int getSaleCounts() {
            return SaleCounts;
        }

        public void setSaleCounts(int SaleCounts) {
            this.SaleCounts = SaleCounts;
        }

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int ShopId) {
            this.ShopId = ShopId;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }
    }
}
