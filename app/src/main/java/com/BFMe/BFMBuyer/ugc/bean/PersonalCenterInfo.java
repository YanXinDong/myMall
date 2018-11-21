package com.BFMe.BFMBuyer.ugc.bean;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/16 17:22
 */
public class PersonalCenterInfo {


    /**
     * Fans : 1
     * FocusUserCount : 2
     * ImageUrl : http://img01.baifomi.com//Storage/Member/temp/8cd747f27486b783.jpeg
     * IntroduceMyself : 新个人简介
     * IsFocus : 0
     * IsOwn : 1
     * ParseCount : 0
     * TopicCount : 0
     * UserName : 新昵称
     * UserShareLink : http://aaa.bfme.com/1/h/eICbzLsB8u4iedWzT60GelcKseeQAYen0aNVQY06xKcKLlHHp8RrRaz45jRVZwQDZT0B7UkZbN5jkMYjiQZa1gSuX7dNNKVw7Cgwc9Ch2OiBq1ipwPnDhBOowKaVTbUdmM2+uWWXVInX64qkMeEU66fZ8hZ4PLrsAuLgokvE8=
     */

    private int Fans;
    private int FocusUserCount;
    private String ImageUrl;
    private String IntroduceMyself;
    private int IsFocus;
    private int IsOwn;
    private int ParseCount;
    private int TopicCount;
    private String UserName;
    private String UserShareLink;

    public int getFans() {
        return Fans;
    }

    public void setFans(int Fans) {
        this.Fans = Fans;
    }

    public int getFocusUserCount() {
        return FocusUserCount;
    }

    public void setFocusUserCount(int FocusUserCount) {
        this.FocusUserCount = FocusUserCount;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getIntroduceMyself() {
        return IntroduceMyself;
    }

    public void setIntroduceMyself(String IntroduceMyself) {
        this.IntroduceMyself = IntroduceMyself;
    }

    public int getIsFocus() {
        return IsFocus;
    }

    public void setIsFocus(int IsFocus) {
        this.IsFocus = IsFocus;
    }

    public int getIsOwn() {
        return IsOwn;
    }

    public void setIsOwn(int IsOwn) {
        this.IsOwn = IsOwn;
    }

    public int getParseCount() {
        return ParseCount;
    }

    public void setParseCount(int ParseCount) {
        this.ParseCount = ParseCount;
    }

    public int getTopicCount() {
        return TopicCount;
    }

    public void setTopicCount(int TopicCount) {
        this.TopicCount = TopicCount;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserShareLink() {
        return UserShareLink;
    }

    public void setUserShareLink(String UserShareLink) {
        this.UserShareLink = UserShareLink;
    }
}
