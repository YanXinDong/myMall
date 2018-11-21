package com.BFMe.BFMBuyer.javaBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Description:   SubjectDataBean
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/21 10:07
 */
public class SubjectDataBean {
    public String Total;
    public ArrayList<TopicsDtats>  Topics;
    public class TopicsDtats{
        public String ImageUrl;
        public String Tags;
        public String tretre;
        public String Id;
        public ArrayList<ProductsData> Products;
    }
    public static class ProductsData implements Parcelable {

        public String Name;
        public String Id;
        public String Image;
        public String Price;
        public String MarketPrice;
        public String ShopId;

        protected ProductsData(Parcel in) {
            Name = in.readString();
            Id = in.readString();
            Image = in.readString();
            Price = in.readString();
            MarketPrice = in.readString();
        }

        public static final Creator<ProductsData> CREATOR = new Creator<ProductsData>() {
            @Override
            public ProductsData createFromParcel(Parcel in) {
                return new ProductsData(in);
            }

            @Override
            public ProductsData[] newArray(int size) {
                return new ProductsData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest,int flags) {
            dest.writeString(Name);
            dest.writeString(Id);
            dest.writeString(Image);
            dest.writeString(Price);
            dest.writeString(MarketPrice);
        }
    }
}
