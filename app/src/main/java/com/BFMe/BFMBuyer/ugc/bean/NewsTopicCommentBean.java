package com.BFMe.BFMBuyer.ugc.bean;

/**
 * Description：新的话题评论
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/21 18:13
 */
public class NewsTopicCommentBean {

    /**
     * CommentContent : 最后的最后
     * CreateDate : 2017-06-23
     * EncryptUserId : J1mZ+KoT2PydY0Fh+EocWCeezbEjXE4/B7u4g2pRnXY6WN3gUSBNyM8lHFjZjMLnq1qv2hClsqp/uEzLlU4GC7F6jhVLHvQ2azxYdSYQ7JD8DrwH1U6cjhTkNSrXfBSBZ4NnX78x39xaxI9YX/5kyz3vh5nEl8rqwXuYU0IEYJ+4yiM8bjqR8SIrL4pLUifIMymet7Ovxik6BJq1vBo5Bzdi0t9mrKCDRBPq8Y0MlGeXEH/fwLgrl/hzd8MT0OaH1R0a96PEhP2IDVzC0M8UH69wcNXCYm5NAaAQMZvFbstgGXoJmuR5Wx2fVg2DKM1a/ykINMOxE1uf7VxXydiqzg==
     * Id : 101
     * ImageUrl : http://192.168.1.50:8888//Storage/Plat/UGCTopicCate/105/4c93b890f8fdf3ea.jpg
     * Photo : http://192.168.1.50:8888//Storage/Member/1086/793f093b0290fb9e.jpg
     * TopicContent : asfsafasfd
     * UserId : 0
     * UserName : 锅包肉
     */

    private String CommentContent;
    private String CreateDate;
    private String EncryptUserId;
    private Long Id;
    private String ImageUrl;
    private String Photo;
    private String TopicContent;
    private int UserId;
    private String UserName;

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String CommentContent) {
        this.CommentContent = CommentContent;
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

    public void setEncryptUserId(String EncryptUserId) {
        this.EncryptUserId = EncryptUserId;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
    }

    public String getTopicContent() {
        return TopicContent;
    }

    public void setTopicContent(String TopicContent) {
        this.TopicContent = TopicContent;
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
}
