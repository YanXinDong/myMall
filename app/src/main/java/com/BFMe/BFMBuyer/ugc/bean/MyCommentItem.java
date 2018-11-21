package com.BFMe.BFMBuyer.ugc.bean;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/19 16:54
 */
public class MyCommentItem {

    /**
     * CommentContent : 话题很棒！！！
     * CreateDate : 2017-06-16 16:05:22
     * Id : 106
     * ImageUrl : http://192.168.1.50:8888//Storage/Plat/UGCTopicCate/105/4c93b890f8fdf3ea_100.jpg
     * TopicContent : asfsafasfd
     */

    private String CommentContent;
    private String CreateDate;
    private long Id;
    private String ImageUrl;
    private String TopicContent;

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

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getTopicContent() {
        return TopicContent;
    }

    public void setTopicContent(String TopicContent) {
        this.TopicContent = TopicContent;
    }
}
