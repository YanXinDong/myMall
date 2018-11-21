package com.BFMe.BFMBuyer.javaBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Description:热门店铺
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/22 20:30
 */
public class HotBean {
    public String              Total;
    public ArrayList<HotDatas> Shops;

    public static class HotDatas implements Parcelable {
        public String Country;
        public String Id;
        public String ImagePath;
        public String ProductId;
        public String SaleCounts;
        public String ShopDesc;
        public String ShopId;
        public String ShopName;

        protected HotDatas(Parcel in) {
            Country = in.readString();
            Id = in.readString();
            ImagePath = in.readString();
            ProductId = in.readString();
            SaleCounts = in.readString();
            ShopDesc = in.readString();
            ShopId = in.readString();
            ShopName = in.readString();
        }

        public static final Creator<HotDatas> CREATOR = new Creator<HotDatas>() {
            @Override
            public HotDatas createFromParcel(Parcel in) {
                return new HotDatas(in);
            }

            @Override
            public HotDatas[] newArray(int size) {
                return new HotDatas[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest,int flags) {
            dest.writeString(Country);
            dest.writeString(Id);
            dest.writeString(ImagePath);
            dest.writeString(ProductId);
            dest.writeString(SaleCounts);
            dest.writeString(ShopDesc);
            dest.writeString(ShopId);
            dest.writeString(ShopName);
        }
    }
}
