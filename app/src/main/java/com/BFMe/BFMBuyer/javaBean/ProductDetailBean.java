package com.BFMe.BFMBuyer.javaBean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:商品详情
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/26 15:25
 */
public class ProductDetailBean implements Serializable{


    /**
     * Color : [{"HasStock":true,"Id":1,"Name":"选择颜色","Value":"红色"},{"HasStock":true,"Id":2,"Name":"选择颜色","Value":"紫色"}]
     * FreightStr : 运费 ¥30
     * IsExpiredShop : false
     * IsLimitBuy : false
     * IsMyself : false
     * IsSellerAdminProdcut : false
     * LimitId : 0
     * Price : 12.00-16.00
     * Product : {"ClickGoodCount":0,"Id":1211,"ImageCount":0,"ImagePath":["http://img.bfme.com//Storage/Shop/520/Products/1211/749b55a50188f50a.jpg"],"IsFavorite":false,"MarketPrice":0,"MeasureUnit":"个","ProductName":"郝艳同测试SKU双的","SaleCounts":0,"SellerBearTax":false,"VistiCounts":0}
     * SendMethod : {"BuyerDes":" 该配送为香港官方保证进口配送模式，商品全部从海外配送到BFM官方香港仓库再由官方配送到用户手中。","Id":3,"SKUId":"1211_1_1_0","SendName":"香港配送"}
     * ShopId : 520
     * Size : [{"HasStock":true,"Id":1,"Name":"选择尺码","Value":"XL"},{"HasStock":true,"Id":2,"Name":"选择尺码","Value":"L"}]
     * Tax : 0
     * Version : []
     */

    private int IsOff;
    private String FreightStr;
    private boolean IsExpiredShop;
    private boolean IsLimitBuy;
    private boolean IsMyself;
    private boolean IsSellerAdminProdcut;
    private int LimitId;
    private String Price;
    /**
     * ClickGoodCount : 0
     * Id : 1211
     * ImageCount : 0
     * ImagePath : ["http://img.bfme.com//Storage/Shop/520/Products/1211/749b55a50188f50a.jpg"]
     * IsFavorite : false
     * MarketPrice : 0.0
     * MeasureUnit : 个
     * ProductName : 郝艳同测试SKU双的
     * SaleCounts : 0
     * SellerBearTax : false
     * VistiCounts : 0
     */

    private ProductBean Product;
    /**
     * BuyerDes :  该配送为香港官方保证进口配送模式，商品全部从海外配送到BFM官方香港仓库再由官方配送到用户手中。
     * Id : 3
     * SKUId : 1211_1_1_0
     * SendName : 香港配送
     */

    private SendMethodBean SendMethod;
    private String ShopId;
    private double Tax;
    /**
     * HasStock : true
     * Id : 1
     * Name : 选择颜色
     * Value : 红色
     */

    private List<ColorBean> Color;
    /**
     * HasStock : true
     * Id : 1
     * Name : 选择尺码
     * Value : XL
     */

    private List<SizeBean> Size;
    private List<?> Version;
    private int StockCount;

    public int getIsOff() {
        return IsOff;
    }

    public void setIsOff(int isOff) {
        this.IsOff = isOff;
    }

    public int getStockCount() {
        return StockCount;
    }

    public void setStockCount(int stockCount) {
        StockCount = stockCount;
    }

    public String getFreightStr() {
        return FreightStr;
    }

    public void setFreightStr(String FreightStr) {
        this.FreightStr = FreightStr;
    }

    public boolean isIsExpiredShop() {
        return IsExpiredShop;
    }

    public void setIsExpiredShop(boolean IsExpiredShop) {
        this.IsExpiredShop = IsExpiredShop;
    }

    public boolean isLimitBuy() {
        return IsLimitBuy;
    }

    public void setIsLimitBuy(boolean IsLimitBuy) {
        this.IsLimitBuy = IsLimitBuy;
    }

    public boolean isIsMyself() {
        return IsMyself;
    }

    public void setIsMyself(boolean IsMyself) {
        this.IsMyself = IsMyself;
    }

    public boolean isIsSellerAdminProdcut() {
        return IsSellerAdminProdcut;
    }

    public void setIsSellerAdminProdcut(boolean IsSellerAdminProdcut) {
        this.IsSellerAdminProdcut = IsSellerAdminProdcut;
    }

    public int getLimitId() {
        return LimitId;
    }

    public void setLimitId(int LimitId) {
        this.LimitId = LimitId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public ProductBean getProduct() {
        return Product;
    }

    public void setProduct(ProductBean Product) {
        this.Product = Product;
    }

    public SendMethodBean getSendMethod() {
        return SendMethod;
    }

    public void setSendMethod(SendMethodBean SendMethod) {
        this.SendMethod = SendMethod;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String ShopId) {
        this.ShopId = ShopId;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double Tax) {
        this.Tax = Tax;
    }

    public List<ColorBean> getColor() {
        return Color;
    }

    public void setColor(List<ColorBean> Color) {
        this.Color = Color;
    }

    public List<SizeBean> getSize() {
        return Size;
    }

    public void setSize(List<SizeBean> Size) {
        this.Size = Size;
    }

    public List<?> getVersion() {
        return Version;
    }

    public void setVersion(List<?> Version) {
        this.Version = Version;
    }

    public static class ProductBean  implements Serializable {
        private int ClickGoodCount;
        private int Id;
        private int ImageCount;
        private boolean IsFavorite;
        private double MarketPrice;
        private String MeasureUnit;
        private String ProductName;
        private String ShortDescription;
        private int SaleCounts;
        private boolean SellerBearTax;
        private int VistiCounts;
        private List<String> ImagePath;
        private String ShareLink;

        public String getShareLink() {
            return ShareLink;
        }

        public void setShareLink(String shareLink) {
            ShareLink = shareLink;
        }

        public boolean isFavorite() {
            return IsFavorite;
        }

        public void setFavorite(boolean favorite) {
            IsFavorite = favorite;
        }

        public int getClickGoodCount() {
            return ClickGoodCount;
        }

        public void setClickGoodCount(int ClickGoodCount) {
            this.ClickGoodCount = ClickGoodCount;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getImageCount() {
            return ImageCount;
        }

        public void setImageCount(int ImageCount) {
            this.ImageCount = ImageCount;
        }

        public boolean isIsFavorite() {
            return IsFavorite;
        }

        public void setIsFavorite(boolean IsFavorite) {
            this.IsFavorite = IsFavorite;
        }

        public double getMarketPrice() {
            return MarketPrice;
        }

        public void setMarketPrice(double MarketPrice) {
            this.MarketPrice = MarketPrice;
        }

        public String getMeasureUnit() {
            return MeasureUnit;
        }

        public void setMeasureUnit(String MeasureUnit) {
            this.MeasureUnit = MeasureUnit;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getShortDescription() {
            return ShortDescription;
        }

        public void setShortDescription(String shortDescription) {
            ShortDescription = shortDescription;
        }

        public int getSaleCounts() {
            return SaleCounts;
        }

        public void setSaleCounts(int SaleCounts) {
            this.SaleCounts = SaleCounts;
        }

        public boolean isSellerBearTax() {
            return SellerBearTax;
        }

        public void setSellerBearTax(boolean SellerBearTax) {
            this.SellerBearTax = SellerBearTax;
        }

        public int getVistiCounts() {
            return VistiCounts;
        }

        public void setVistiCounts(int VistiCounts) {
            this.VistiCounts = VistiCounts;
        }

        public List<String> getImagePath() {
            return ImagePath;
        }

        public void setImagePath(List<String> ImagePath) {
            this.ImagePath = ImagePath;
        }
    }

    public static class SendMethodBean  implements Serializable{
        private String BuyerDes;
        private int Id;
        private String SKUId;
        private String SendName;

        public String getBuyerDes() {
            return BuyerDes;
        }

        public void setBuyerDes(String BuyerDes) {
            this.BuyerDes = BuyerDes;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getSKUId() {
            return SKUId;
        }

        public void setSKUId(String SKUId) {
            this.SKUId = SKUId;
        }

        public String getSendName() {
            return SendName;
        }

        public void setSendName(String SendName) {
            this.SendName = SendName;
        }
    }

    public static class ColorBean  implements Serializable{
        private boolean HasStock;
        private String Id;
        private String Name;
        private String Value;

        public boolean isHasStock() {
            return HasStock;
        }

        public void setHasStock(boolean HasStock) {
            this.HasStock = HasStock;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }
    }

    public static class SizeBean  implements Serializable{
        private boolean HasStock;
        private String Id;
        private String Name;
        private String Value;

        public boolean isHasStock() {
            return HasStock;
        }

        public void setHasStock(boolean HasStock) {
            this.HasStock = HasStock;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }
    }
}
