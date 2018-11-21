package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 * 商品列表的bean类
 */
public class ShopListBean {

    @Override
    public String toString() {
        return "ShopListBean{" +
                "Total=" + Total +
                ", Products=" + Products +
                '}';
    }

    /**
     * Products : [{"ProductId":2989,"ShopName":"Trillium Beauty Club 美淘","ProductName":"Ladamer 多效BB霜","ImagePath":"http://img01.bfme.com/Storage/Shop/787/Products/2989","MarketPrice":220,"MinSalePrice":176,"MeasureUnit":"支","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":true,"IsFreeTax":false,"ShopId":787},{"ProductId":4607,"ShopName":"香港药妆","ProductName":"泰国Mistine眼线笔防水不晕染3d极细速干眼线液笔银管 正品","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/4607","MarketPrice":65,"MinSalePrice":45,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":4656,"ShopName":"香港药妆","ProductName":"韩国芭妮兰致柔卸妆膏100ml 眼唇可卸 温和深层清洁","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/4656","MarketPrice":100,"MinSalePrice":85,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":4660,"ShopName":"香港药妆","ProductName":"日本cosme大赏Unicharm尤妮佳1/2省水化妆棉双面补水洁面卸妆40枚","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/4660","MarketPrice":40,"MinSalePrice":20,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":4681,"ShopName":"香港药妆","ProductName":"日本原装kiss me泪眼超防水不晕染眼线笔 软头纤细眼线液笔","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/4681","MarketPrice":80,"MinSalePrice":56,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":4693,"ShopName":"香港药妆","ProductName":"日本DHC蝶翠诗橄榄卸妆油200ml 眼唇脸部深彻清洁黑头毛孔不糊眼","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/4693","MarketPrice":140,"MinSalePrice":119,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":4694,"ShopName":"香港药妆","ProductName":"澳洲Lucas Papaw番木瓜膏润唇膏 滋润万用膏25g 婴儿孕妇可用天然","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/4694","MarketPrice":50,"MinSalePrice":36,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":4701,"ShopName":"香港药妆","ProductName":"herbacin/贺本清小甘菊敏感修护润唇膏4.8g 天然植物呵护","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/4701","MarketPrice":30,"MinSalePrice":24,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":4781,"ShopName":"香港药妆","ProductName":"正品 韩国悦诗风吟薄荷散粉 控油散粉细致毛孔定妆粉蜜粉 5g","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/4781","MarketPrice":50,"MinSalePrice":39,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":4849,"ShopName":"千亿喜多多 ","ProductName":"现货日本资生堂睫毛滋养精华液浓密增长 睫毛膏打底膏","ImagePath":"http://img01.bfme.com/Storage/Shop/1026/Products/4849","MarketPrice":158,"MinSalePrice":148,"MeasureUnit":"支","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":true,"IsFreeTax":false,"ShopId":1026},{"ProductId":4935,"ShopName":"彩虹日货馆","ProductName":"日本代购 CPB 资生堂肌肤之钥 洁面膏125g 清爽型滋润型日本直邮 清爽型","ImagePath":"http://img01.bfme.com/Storage/Shop/1085/Products/4935","MarketPrice":550,"MinSalePrice":520,"MeasureUnit":"件","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":true,"IsFreeTax":false,"ShopId":1085},{"ProductId":4937,"ShopName":"彩虹日货馆","ProductName":"【日本原装直邮】资生堂CPB肌肤之钥 新精华焕发细胞活力 精华肌底液 40ml","ImagePath":"http://img01.bfme.com/Storage/Shop/1085/Products/4937","MarketPrice":2066,"MinSalePrice":2026,"MeasureUnit":"件","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":true,"IsFreeTax":false,"ShopId":1085},{"ProductId":4938,"ShopName":"彩虹日货馆","ProductName":"资生堂CPB高光粉修容粉肌肤之钥亮采柔肤粉提亮粉立体10g 一套11","ImagePath":"http://img01.bfme.com/Storage/Shop/1085/Products/4938","MarketPrice":685,"MinSalePrice":645,"MeasureUnit":"件","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":true,"IsFreeTax":false,"ShopId":1085},{"ProductId":5242,"ShopName":"香港药妆","ProductName":"日本ALOVIVI卸妆皇后洁肤液500ml清洁温和卸妆水保湿洁面舒缓","ImagePath":"http://img01.bfme.com/Storage/Shop/1014/Products/5242","MarketPrice":90,"MinSalePrice":55,"MeasureUnit":"件","Country":"香港","CountryLogo":"http://img01.bfme.com/lib/countries/logo/3.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":1014},{"ProductId":421,"ShopName":"小鹤日本代购","ProductName":"【Shu Uemura 】植村秀 小灯泡微光粉饼（饼芯+饼盒）","ImagePath":"http://img01.bfme.com/Storage/Shop/409/Products/421","MarketPrice":480,"MinSalePrice":430,"MeasureUnit":"个","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":409},{"ProductId":422,"ShopName":"小鹤日本代购","ProductName":"【Shu Uemura 】植村秀 琥珀臻萃洁颜油 450ML","ImagePath":"http://img01.bfme.com/Storage/Shop/409/Products/422","MarketPrice":870,"MinSalePrice":785,"MeasureUnit":"瓶","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":409},{"ProductId":423,"ShopName":"小鹤日本代购","ProductName":"【Shu Uemura 】植村秀 如胶似漆眼线笔","ImagePath":"http://img01.bfme.com/Storage/Shop/409/Products/423","MarketPrice":235,"MinSalePrice":185,"MeasureUnit":"支","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":409},{"ProductId":425,"ShopName":"小鹤日本代购","ProductName":"【Shu Uemura 】植村秀 无色限柔雾唇膏","ImagePath":"http://img01.bfme.com/Storage/Shop/409/Products/425","MarketPrice":215,"MinSalePrice":165,"MeasureUnit":"只","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":409},{"ProductId":427,"ShopName":"小鹤日本代购","ProductName":"【Shu Uemura 】植村秀 无色限腮红","ImagePath":"http://img01.bfme.com/Storage/Shop/409/Products/427","MarketPrice":165,"MinSalePrice":115,"MeasureUnit":"个","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":409},{"ProductId":428,"ShopName":"小鹤日本代购","ProductName":"【Shu Uemura 】植村秀 专业睫毛夹","ImagePath":"http://img01.bfme.com/Storage/Shop/409/Products/428","MarketPrice":180,"MinSalePrice":130,"MeasureUnit":"个","Country":"日本","CountryLogo":"http://img01.bfme.com/lib/countries/logo/1.jpg","Freight":"","Tax":"","IsFreeFreight":false,"IsFreeTax":false,"ShopId":409}]
     * Total : 472
     */

    private int Total;
    /**
     * ProductId : 2989
     * ShopName : Trillium Beauty Club 美淘
     * ProductName : Ladamer 多效BB霜
     * ImagePath : http://img01.bfme.com/Storage/Shop/787/Products/2989
     * MarketPrice : 220
     * MinSalePrice : 176
     * MeasureUnit : 支
     * Country : 香港
     * CountryLogo : http://img01.bfme.com/lib/countries/logo/3.jpg
     * Freight :
     * Tax :
     * IsFreeFreight : true
     * IsFreeTax : false
     * ShopId : 787
     */

    private List<ProductsBean> Products;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<ProductsBean> getProducts() {
        return Products;
    }

    public void setProducts(List<ProductsBean> Products) {
        this.Products = Products;
    }

    public static class ProductsBean {
        private int ProductId;
        private String ShopName;
        private String ProductName;
        private String ImagePath;
        private double MarketPrice;
        private double MinSalePrice;
        private String MeasureUnit;
        private String Country;
        private String CountryLogo;
        private String Freight;
        private String Tax;
        private boolean IsFreeFreight;
        private boolean IsFreeTax;
        private int ShopId;

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int productId) {
            ProductId = productId;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String imagePath) {
            ImagePath = imagePath;
        }

        public double getMarketPrice() {
            return MarketPrice;
        }

        public void setMarketPrice(double marketPrice) {
            MarketPrice = marketPrice;
        }

        public double getMinSalePrice() {
            return MinSalePrice;
        }

        public void setMinSalePrice(double minSalePrice) {
            MinSalePrice = minSalePrice;
        }

        public String getMeasureUnit() {
            return MeasureUnit;
        }

        public void setMeasureUnit(String measureUnit) {
            MeasureUnit = measureUnit;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }

        public String getCountryLogo() {
            return CountryLogo;
        }

        public void setCountryLogo(String countryLogo) {
            CountryLogo = countryLogo;
        }

        public String getFreight() {
            return Freight;
        }

        public void setFreight(String freight) {
            Freight = freight;
        }

        public String getTax() {
            return Tax;
        }

        public void setTax(String tax) {
            Tax = tax;
        }

        public boolean isFreeFreight() {
            return IsFreeFreight;
        }

        public void setFreeFreight(boolean freeFreight) {
            IsFreeFreight = freeFreight;
        }

        public boolean isFreeTax() {
            return IsFreeTax;
        }

        public void setFreeTax(boolean freeTax) {
            IsFreeTax = freeTax;
        }

        public int getShopId() {
            return ShopId;
        }

        public void setShopId(int shopId) {
            ShopId = shopId;
        }

        @Override
        public String toString() {
            return "ProductsBean{" +
                    "ProductId=" + ProductId +
                    ", ShopName='" + ShopName + '\'' +
                    ", ProductName='" + ProductName + '\'' +
                    ", ImagePath='" + ImagePath + '\'' +
                    ", MarketPrice='" + MarketPrice + '\'' +
                    ", MinSalePrice='" + MinSalePrice + '\'' +
                    ", MeasureUnit='" + MeasureUnit + '\'' +
                    ", Country='" + Country + '\'' +
                    ", CountryLogo='" + CountryLogo + '\'' +
                    ", Freight='" + Freight + '\'' +
                    ", Tax='" + Tax + '\'' +
                    ", IsFreeFreight=" + IsFreeFreight +
                    ", IsFreeTax=" + IsFreeTax +
                    ", ShopId=" + ShopId +
                    '}';
        }
    }
}
