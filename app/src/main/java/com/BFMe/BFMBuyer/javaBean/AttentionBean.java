package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/9/14 19:06
 */
public class AttentionBean {

    /**
     * ShopName : 美美福利社
     * Logo :
     * ConcernCount : 10
     * ConcernTime : 2016-09-14
     * ShopId : 425
     * Id : 345
     * SericeMark : 0
     * CurrentPage : 1
     * ItemsPerPage : 10
     * TotalItems : 4
     * Description :
     */

    private List<ShopsBean> Shops;

    public List<ShopsBean> getShops() {
        return Shops;
    }

    public void setShops(List<ShopsBean> Shops) {
        this.Shops = Shops;
    }

    public static class ShopsBean {
        private String ShopName;
        private String Logo;
        private int ConcernCount;
        private String ConcernTime;
        private int ShopId;
        private int Id;
        private int SericeMark;
        private int CurrentPage;
        private int ItemsPerPage;
        private int TotalItems;
        private String Description;

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String Logo) {
            this.Logo = Logo;
        }

        public int getConcernCount() {
            return ConcernCount;
        }

        public void setConcernCount(int ConcernCount) {
            this.ConcernCount = ConcernCount;
        }

        public String getConcernTime() {
            return ConcernTime;
        }

        public void setConcernTime(String ConcernTime) {
            this.ConcernTime = ConcernTime;
        }

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int ShopId) {
            this.ShopId = ShopId;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getSericeMark() {
            return SericeMark;
        }

        public void setSericeMark(int SericeMark) {
            this.SericeMark = SericeMark;
        }

        public int getCurrentPage() {
            return CurrentPage;
        }

        public void setCurrentPage(int CurrentPage) {
            this.CurrentPage = CurrentPage;
        }

        public int getItemsPerPage() {
            return ItemsPerPage;
        }

        public void setItemsPerPage(int ItemsPerPage) {
            this.ItemsPerPage = ItemsPerPage;
        }

        public int getTotalItems() {
            return TotalItems;
        }

        public void setTotalItems(int TotalItems) {
            this.TotalItems = TotalItems;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }
    }
}
