package com.BFMe.BFMBuyer.main.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/11/1 14:07
 */
public class HotProductsBean {

    /**
     * CountryIcon : http://img01.baifomi.com/lib/countries/logo/4.jpg
     * Id : 1334
     * ImagePath : http://img01.baifomi.com//Storage/Shop/597/temp/c14753cf67e8b1a9_350.jpg
     * MarketPrice : 999.0
     * Name : 香奈儿5
     * Price : 99.0
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String CountryIcon;
        private String Id;
        private String ImagePath;
        private double MarketPrice;
        private String Name;
        private double Price;

        public String getCountryIcon() {
            return CountryIcon;
        }

        public void setCountryIcon(String CountryIcon) {
            this.CountryIcon = CountryIcon;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public double getMarketPrice() {
            return MarketPrice;
        }

        public void setMarketPrice(double MarketPrice) {
            this.MarketPrice = MarketPrice;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }
    }
}
