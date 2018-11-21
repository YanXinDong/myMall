package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/16 10:53
 */
public class TopicDetails {
    /**
     * CommentCount : 0
     * Content : 测试
     * CreateDate : 2017-06-16
     * EncryptId : FolidT0j7r9Jg20Xn3zD87j1euDm7iyIyxEiRQC4U9Eknf04g3TUNzxuX1N/BXbb4gAMxXAIMaye9aDrN1um6XBt6kD0F5iUe44XQ9LIeDWgTpvXgfVb+IGhPCocYH4rjcMjVYECAN/CyCYMooSFDkqUTtOorP6En72zvV1LZ0Jlcmx2ppshOWjXAkeZOaSFG3k9hdgn/0yfPAGXTs0nytN1hAEP+8lXX4MknyM3LMh5C0EMy5DtCOqPHPkSnEe4zcNLcMdcK+AS3/XlpTMP+XMVQEE/4pyQXh2r/8u9EPEOy0NHtG40OOgU6+T5cTsgSsC3Qdu7nxFRZZokhxvvOA==
     * Id : 0
     * IsFoucsUser : 0
     * IsParse : 0
     * ParseCount : 0
     * ShareLink : http://aaa.bfme.com/1/139
     * Title :
     * TopicPhotos : [{"ImageHeight":0,"ImageUrl":"http://192.168.1.50:8888//Storage/UGC/Member/1086/139/f5fec61724e4ae73.jpg","ImageWidth":0},{"ImageHeight":0,"ImageUrl":"http://192.168.1.50:8888//Storage/UGC/Member/1086/139/ac079dffe2b21ebe.jpg","ImageWidth":0},{"ImageHeight":0,"ImageUrl":"http://192.168.1.50:8888//Storage/UGC/Member/1086/139/4d7ff7db890c6e77.jpg","ImageWidth":0}]
     * UserName : 我是卖家第二季
     * UserPhoto :
     * topiccommentlist : []
     * topicparseuserlist : []
     * topicproductlist : [{"Id":371,"Photo":"http://img01.bfme.com/Storage/Shop/399/Products/371","Prise":73,"Title":" Shiseido资生堂六角眉笔"},{"Id":1136,"Photo":"http://192.168.1.149/Storage/Shop/520/Products/1136/1.png","Prise":200,"Title":"adsdsa"}]
     */

    private int CommentCount;
    private String Content;
    private String CreateDate;
    private String EncryptId;
    private String FailReason;
    private int Id;
    private int IsFoucsUser;
    private int IsParse;
    private int ParseCount;
    private String ShareLink;
    private int State;
    private String Title;
    private String UserName;
    private String UserPhoto;
    /**
     * ImageHeight : 0
     * ImageUrl : http://192.168.1.50:8888//Storage/UGC/Member/1086/139/f5fec61724e4ae73.jpg
     * ImageWidth : 0
     */

    private List<TopicPhotosBean> TopicPhotos;
    /**
     * Id : 371
     * Photo : http://img01.bfme.com/Storage/Shop/399/Products/371
     * Prise : 73.0
     * Title :  Shiseido资生堂六角眉笔
     */

    private List<TopicproductlistBean> topicproductlist;

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int CommentCount) {
        this.CommentCount = CommentCount;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public String getEncryptId() {
        return EncryptId;
    }

    public void setEncryptId(String EncryptId) {
        this.EncryptId = EncryptId;
    }

    public int getId() {
        return Id;
    }

    public String getFailReason() {
        return FailReason;
    }

    public void setFailReason(String failReason) {
        FailReason = failReason;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIsFoucsUser() {
        return IsFoucsUser;
    }

    public void setIsFoucsUser(int IsFoucsUser) {
        this.IsFoucsUser = IsFoucsUser;
    }

    public int getIsParse() {
        return IsParse;
    }

    public void setIsParse(int IsParse) {
        this.IsParse = IsParse;
    }

    public int getParseCount() {
        return ParseCount;
    }

    public void setParseCount(int ParseCount) {
        this.ParseCount = ParseCount;
    }

    public String getShareLink() {
        return ShareLink;
    }

    public void setShareLink(String ShareLink) {
        this.ShareLink = ShareLink;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String UserPhoto) {
        this.UserPhoto = UserPhoto;
    }

    public List<TopicPhotosBean> getTopicPhotos() {
        return TopicPhotos;
    }

    public void setTopicPhotos(List<TopicPhotosBean> TopicPhotos) {
        this.TopicPhotos = TopicPhotos;
    }

    public List<TopicproductlistBean> getTopicproductlist() {
        return topicproductlist;
    }

    public void setTopicproductlist(List<TopicproductlistBean> topicproductlist) {
        this.topicproductlist = topicproductlist;
    }

    public static class TopicPhotosBean {
        private int ImageHeight;
        private String ImageUrl;
        private int ImageWidth;

        public int getImageHeight() {
            return ImageHeight;
        }

        public void setImageHeight(int ImageHeight) {
            this.ImageHeight = ImageHeight;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public int getImageWidth() {
            return ImageWidth;
        }

        public void setImageWidth(int ImageWidth) {
            this.ImageWidth = ImageWidth;
        }
    }

    public static class TopicproductlistBean {
        private String Id;
        private String Photo;
        private double Prise;
        private String Title;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String Photo) {
            this.Photo = Photo;
        }

        public double getPrise() {
            return Prise;
        }

        public void setPrise(double Prise) {
            this.Prise = Prise;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }
    }

    /**
     * Content : 对的啊
     * CreateDate : 2017-06-15 15:52:53
     * IsParse : 0
     * ParseCount : 0
     * UserId : 1103
     * UserName : tiger28
     * UserPhoto : http://127.0.0.1:8080//Storage/Member/1103/b09d1c88e3d22b02.jpg
     */

    private List<TopiccommentlistBean> topiccommentlist;
    /**
     * UserId : 38424
     * UserPhoto :
     */

    private List<TopicparseuserlistBean> topicparseuserlist;

    public List<TopiccommentlistBean> getTopiccommentlist() {
        return topiccommentlist;
    }

    public void setTopiccommentlist(List<TopiccommentlistBean> topiccommentlist) {
        this.topiccommentlist = topiccommentlist;
    }

    public List<TopicparseuserlistBean> getTopicparseuserlist() {
        return topicparseuserlist;
    }

    public void setTopicparseuserlist(List<TopicparseuserlistBean> topicparseuserlist) {
        this.topicparseuserlist = topicparseuserlist;
    }

    public static class TopiccommentlistBean {
        private String Id;
        private String Content;
        private String CreateDate;
        private String EncryptUserId;
        private int IsParse;
        private int ParseCount;
        private int UserId;
        private String UserName;
        private String UserPhoto;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String CreateDate) {
            this.CreateDate = CreateDate;
        }

        public String getEncryptUserId() {
            return EncryptUserId;
        }

        public void setEncryptUserId(String encryptUserId) {
            EncryptUserId = encryptUserId;
        }

        public int getIsParse() {
            return IsParse;
        }

        public void setIsParse(int IsParse) {
            this.IsParse = IsParse;
        }

        public int getParseCount() {
            return ParseCount;
        }

        public void setParseCount(int ParseCount) {
            this.ParseCount = ParseCount;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getUserPhoto() {
            return UserPhoto;
        }

        public void setUserPhoto(String UserPhoto) {
            this.UserPhoto = UserPhoto;
        }
    }

    public static class TopicparseuserlistBean {
        private String EncryptUserId;
        private int UserId;
        private String UserPhoto;

        public String getEncryptUserId() {
            return EncryptUserId;
        }

        public void setEncryptUserId(String encryptUserId) {
            EncryptUserId = encryptUserId;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public String getUserPhoto() {
            return UserPhoto;
        }

        public void setUserPhoto(String UserPhoto) {
            this.UserPhoto = UserPhoto;
        }
    }

}
