package com.BFMe.BFMBuyer.javaBean;

import java.util.ArrayList;

/**
 * Description: 商品详情页的两个评价列表
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/8/3 18:59
 */
public class ProductCommentBean {
    public String                  Total;
    public ArrayList<CommentsData> Comments;

    public class CommentsData {

        public String ProductId;
        public String ReviewContent;
        public String ReviewDate;
        public String ReviewMark;
        public String UserName;
        public String UserPhoto;
    }
}
