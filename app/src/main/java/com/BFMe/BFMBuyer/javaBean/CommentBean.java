package com.BFMe.BFMBuyer.javaBean;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : 白富美(北京)网络科技有限公司
 * Author     : 王  可
 * Date       : 2016/7/25 18:56
 */
public class CommentBean {
    public int Total;
    public ArrayList<CommentData> Comments;
    public class CommentData{
        public ArrayList<String> Images;
        public String UserPhoto;
        public String UserName;
        public String ProductId;
        public String ReviewContent;
        public String ReviewDate;
        public String ReviewMark;

    }
}
