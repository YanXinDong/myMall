package com.BFMe.BFMBuyer.main.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/8/31 17:04
 */
public class HomeData {

    private HomeDataBean homeData;

    public HomeDataBean getHomeData() {
        return homeData;
    }

    public void setHomeData(HomeDataBean homeData) {
        this.homeData = homeData;
    }

    public static class HomeDataBean {
        /**
         * ImageUrl : http://192.168.1.149:8088//Storage/Plat/Weixin/SlidAd/6c3460e3371ac1ad.jpg
         * Type : 1
         * Url : http://m.baifomi.com/Page/110.html
         */

        private List<BottonBannerBean> bottonBanner;
        /**
         * ImageUrl : http://192.168.1.50:8888//Storage/Plat/UGCTopicCate/105/4c93b890f8fdf3ea.jpg
         * ParticipateCount : 6
         * Title : 晒晒你的购物车
         * TopicId : 101
         */

        private List<CategoryBean> category;
        /**
         * CountryAreaImgPath :
         * CountryAreaName :
         * MinSalePrice : 99.11
         * ProductId : 887
         * ProductImagePath : http://192.168.1.149:8088/Storage/Shop/515/Products/887
         * ProductName : IE8怎么样
         */

        private List<ProductBean> product;
        /**
         * ImageUrl : http://192.168.1.149:8088//Storage/Plat/Weixin/SlidAd/6c3460e3371ac1ad.jpg
         * Type : 1
         * Url : http://m.baifomi.com/Page/110.html
         */

        private List<TopBannerBean> topBanner;

        public List<BottonBannerBean> getBottonBanner() {
            return bottonBanner;
        }

        public void setBottonBanner(List<BottonBannerBean> bottonBanner) {
            this.bottonBanner = bottonBanner;
        }

        public List<CategoryBean> getCategory() {
            return category;
        }

        public void setCategory(List<CategoryBean> category) {
            this.category = category;
        }

        public List<ProductBean> getProduct() {
            return product;
        }

        public void setProduct(List<ProductBean> product) {
            this.product = product;
        }

        public List<TopBannerBean> getTopBanner() {
            return topBanner;
        }

        public void setTopBanner(List<TopBannerBean> topBanner) {
            this.topBanner = topBanner;
        }

        public static class BottonBannerBean {
            private String ImageUrl;
            private int Type;
            private String Url;

            public String getImageUrl() {
                return ImageUrl;
            }

            public void setImageUrl(String ImageUrl) {
                this.ImageUrl = ImageUrl;
            }

            public int getType() {
                return Type;
            }

            public void setType(int Type) {
                this.Type = Type;
            }

            public String getUrl() {
                return Url;
            }

            public void setUrl(String Url) {
                this.Url = Url;
            }
        }

        public static class CategoryBean {
            private String ImageUrl;
            private int ParticipateCount;
            private String Title;
            private int TopicId;

            public String getImageUrl() {
                return ImageUrl;
            }

            public void setImageUrl(String ImageUrl) {
                this.ImageUrl = ImageUrl;
            }

            public int getParticipateCount() {
                return ParticipateCount;
            }

            public void setParticipateCount(int ParticipateCount) {
                this.ParticipateCount = ParticipateCount;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public int getTopicId() {
                return TopicId;
            }

            public void setTopicId(int TopicId) {
                this.TopicId = TopicId;
            }
        }

        public static class ProductBean {
            private String CountryAreaImgPath;
            private String CountryAreaName;
            private double MinSalePrice;
            private int ProductId;
            private String ProductImagePath;
            private String ProductName;

            public String getCountryAreaImgPath() {
                return CountryAreaImgPath;
            }

            public void setCountryAreaImgPath(String CountryAreaImgPath) {
                this.CountryAreaImgPath = CountryAreaImgPath;
            }

            public String getCountryAreaName() {
                return CountryAreaName;
            }

            public void setCountryAreaName(String CountryAreaName) {
                this.CountryAreaName = CountryAreaName;
            }

            public double getMinSalePrice() {
                return MinSalePrice;
            }

            public void setMinSalePrice(double MinSalePrice) {
                this.MinSalePrice = MinSalePrice;
            }

            public int getProductId() {
                return ProductId;
            }

            public void setProductId(int ProductId) {
                this.ProductId = ProductId;
            }

            public String getProductImagePath() {
                return ProductImagePath;
            }

            public void setProductImagePath(String ProductImagePath) {
                this.ProductImagePath = ProductImagePath;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String ProductName) {
                this.ProductName = ProductName;
            }
        }

        public static class TopBannerBean {
            private String ImageUrl;
            private int Type;
            private String Url;

            public String getImageUrl() {
                return ImageUrl;
            }

            public void setImageUrl(String ImageUrl) {
                this.ImageUrl = ImageUrl;
            }

            public int getType() {
                return Type;
            }

            public void setType(int Type) {
                this.Type = Type;
            }

            public String getUrl() {
                return Url;
            }

            public void setUrl(String Url) {
                this.Url = Url;
            }
        }
    }
}
