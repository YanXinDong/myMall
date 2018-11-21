package com.BFMe.BFMBuyer.ugc.bean;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/17 11:38
 */
public class CommentItem {

    /**
     * Content : 对的啊
     * CreateDate : 2017-06-15 15:52:53
     * EncryptUserId : g5VH+gNdJKemlJgStY3KW0US8h/zDfDAJ4MfpzVCBbsIxgvI116uWV2Zxs9eeAuQcCQH1u2YYIdyqxt/h1UmH5kmWmwyylx5KfGKVP+nOU8x7cjQ/+BGWji9q8DSkVmVCxXBhHN93HQiWFv+vuL9sKzZSezDnOVLHgXUm6dbs/VWVfePWqVkScRbH6O1vtIeuCx6zbwgtcPRit3iZEKtEM4YQjM0IV9vxs+f/trbDaTfMG5jQ2IKotviN5TMRPdtFe+56D8T79li+nWf6F4jz8Bl3ZKm7yX1g6F5G6CHHwYXOhI7XQdM4X4oepbBPGf8W5XSsPHC3OD6PRMHUVtUGg==
     * Id : 103
     * IsParse : 0
     * ParseCount : 0
     * Photo : http://127.0.0.1:8080//Storage/Member/1103/b09d1c88e3d22b02.jpg
     * UserId : 0
     * UserName : tiger28
     */

    private String Content;
    private String CreateDate;
    private String EncryptUserId;
    private String Id;
    private int IsParse;
    private int ParseCount;
    private String Photo;
    private int UserId;
    private String UserName;

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

    public void setEncryptUserId(String EncryptUserId) {
        this.EncryptUserId = EncryptUserId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
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

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String Photo) {
        this.Photo = Photo;
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
