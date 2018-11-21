package com.BFMe.BFMBuyer.javaBean;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/25 11:11
 */
public class HotSellProductsBean {
    public String Total;
    public ArrayList<HotSellProductsData>  Products;

    public class HotSellProductsData{
        public String Id;
        public String ProductName;
        public String ImagePath;
        public String MinSalePrice;
        public String NationalArea;
        public String Count;
        public String MarktPrice;
        public boolean SellerBearFreight;
        public String Freight;
        public boolean SellerBearTax;
        public String Tax;
        public String Country;
        public String CountryImg;
        public String ShopId;
    }
}
