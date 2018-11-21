package com.BFMe.BFMBuyer.javaBean;

import java.util.ArrayList;

/**
 * Description: 获取订单地址
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/8/22 11:24
 */
public class AddressListBean {
    public ArrayList<addresses> Address;

    public class addresses {
        public String  Address;
        public String  CardNumber;
        public String  Id;
        public boolean IsDefault;
        public String  Phone;
        public String  RegionFullName;
        public String  RegionId;
        public String  RegionIdPath;
        public String  ShipTo;
        public String  UserId;
    }
}
