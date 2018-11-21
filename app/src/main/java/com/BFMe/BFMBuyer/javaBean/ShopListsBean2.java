package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：从洲际馆获取店铺列表
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/5/8 10:53
 */
public class ShopListsBean2 {

    /**
     * Total : 1
     * Shops : [{"Id":0,"ProductId":0,"SaleCounts":0,"ShopId":769,"ShopName":"陈浩南的小店","ImagePath":"http://192.168.1.159:8080/Storage/Shop/769/Products/1028","Country":"香港","ShopDesc":"山东龙口的就疯了快圣诞节疯了快圣诞节法律快睡觉的费两块大三23423双方各地方给对方个..."}]
     */

    private AreasBean areas;

    public AreasBean getAreas() {
        return areas;
    }

    public void setAreas(AreasBean areas) {
        this.areas = areas;
    }

    public static class AreasBean {
        private int Total;
        /**
         * Id : 0
         * ProductId : 0
         * SaleCounts : 0
         * ShopId : 769
         * ShopName : 陈浩南的小店
         * ImagePath : http://192.168.1.159:8080/Storage/Shop/769/Products/1028
         * Country : 香港
         * ShopDesc : 山东龙口的就疯了快圣诞节疯了快圣诞节法律快睡觉的费两块大三23423双方各地方给对方个...
         */

        private List<ShopsBean> Shops;

        public int getTotal() {
            return Total;
        }

        public void setTotal(int Total) {
            this.Total = Total;
        }

        public List<ShopsBean> getShops() {
            return Shops;
        }

        public void setShops(List<ShopsBean> Shops) {
            this.Shops = Shops;
        }

        public static class ShopsBean {
            private int Id;
            private int ProductId;
            private int SaleCounts;
            private int ShopId;
            private String ShopName;
            private String ImagePath;
            private String Country;
            private String ShopDesc;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
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

            public String getImagePath() {
                return ImagePath;
            }

            public void setImagePath(String ImagePath) {
                this.ImagePath = ImagePath;
            }

            public String getCountry() {
                return Country;
            }

            public void setCountry(String Country) {
                this.Country = Country;
            }

            public String getShopDesc() {
                return ShopDesc;
            }

            public void setShopDesc(String ShopDesc) {
                this.ShopDesc = ShopDesc;
            }
        }
    }
}
