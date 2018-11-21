package com.BFMe.BFMBuyer.javaBean;

/**
 * Created by Administrator on 2016/8/31.
 */
public class HelpArticleBean {

    /**
     * Id : 40
     * CategoryId : 11
     * Title : 签收时间
     * Content : <p style="margin-top:0;margin-right:0;margin-bottom:0;margin-left:0;text-indent:0;padding:0 0 0 0 ;line-height:22px;background:rgb(255,255,255)"><span style="font-family: 宋体;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">白富美海淘</span><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">强烈建议您：</span></p><p style="margin-top:0;margin-right:0;margin-bottom:0;margin-left:0;text-indent:0;padding:0 0 0 0 ;line-height:22px;background:rgb(255,255,255)"><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">快递员送货上门时，请您务必当面对照发货单核对商品，主要检查如下内容：</span></p><p style="margin-top:0;margin-right:0;margin-bottom:0;margin-left:0;text-indent:0;padding:0 0 0 0 ;line-height:22px;background:rgb(255,255,255)"><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">&nbsp;&nbsp;&nbsp;&nbsp;1<span style="font-family:宋体">、封箱胶带是否为</span></span><span style="font-family: 宋体;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">白富美海淘</span><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">专用胶带，胶带是否有被拆开重新粘贴过的痕迹；</span></p><p style="margin-top:0;margin-right:0;margin-bottom:0;margin-left:0;text-indent:0;padding:0 0 0 0 ;line-height:22px;background:rgb(255,255,255)"><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">&nbsp;&nbsp;&nbsp;&nbsp;2<span style="font-family:宋体">、外包装是否破损；</span></span></p><p style="margin-top:0;margin-right:0;margin-bottom:0;margin-left:0;text-indent:0;padding:0 0 0 0 ;line-height:22px;background:rgb(255,255,255)"><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">&nbsp;&nbsp;&nbsp;&nbsp;3<span style="font-family:宋体">、商品是否缺少、配送错误。</span></span></p><p style="margin-top:0;margin-right:0;margin-bottom:0;margin-left:0;text-indent:0;padding:0 0 0 0 ;line-height:22px;background:rgb(255,255,255)"><span style="font-family: Verdana;color: rgb(255, 0, 0);letter-spacing: 0;font-size: 12px">如在检查的过程中出现问题，请您不要签收商品，并及时电话联系</span><span style="font-family: 宋体;color: rgb(255, 0, 0);letter-spacing: 0;font-size: 12px">白富美海淘</span><span style="font-family: Verdana;color: rgb(255, 0, 0);letter-spacing: 0;font-size: 12px">客服中心（</span><span style="font-family: 宋体;color: rgb(255, 0, 0);letter-spacing: 0;font-size: 12px">010-57729541</span><span style="font-family: Verdana;color: rgb(255, 0, 0);letter-spacing: 0;font-size: 12px">），我们会第一时间处理您的问题。</span></p><p style="margin-top:0;margin-right:0;margin-bottom:0;margin-left:0;text-indent:0;padding:0 0 0 0 ;line-height:22px;background:rgb(255,255,255)"><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">签收后</span><span style="font-family: 宋体;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">白富美海淘</span><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">视为您已经认可并接受本公司商品，出现上述问题将不再进行处理。非订货人本人签收的产品，</span><span style="font-family: 宋体;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">白富美海淘</span><span style="font-family: Verdana;color: rgb(102, 102, 102);letter-spacing: 0;font-size: 12px">视代收人等同于订购人进行验货。</span></p><p><br/></p>
     * AddDate : /Date(1449654651000-0000)/
     * DisplaySequence : 1
     * IsRelease : true
     */

    private int Id;
    private int CategoryId;
    private String Title;
    private String Content;
    private String AddDate;
    private int DisplaySequence;
    private boolean IsRelease;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getAddDate() {
        return AddDate;
    }

    public void setAddDate(String AddDate) {
        this.AddDate = AddDate;
    }

    public int getDisplaySequence() {
        return DisplaySequence;
    }

    public void setDisplaySequence(int DisplaySequence) {
        this.DisplaySequence = DisplaySequence;
    }

    public boolean isIsRelease() {
        return IsRelease;
    }

    public void setIsRelease(boolean IsRelease) {
        this.IsRelease = IsRelease;
    }

    @Override
    public String toString() {
        return "HelpArticleBean{" +
                "Id=" + Id +
                ", CategoryId=" + CategoryId +
                ", Title='" + Title + '\'' +
                ", Content='" + Content + '\'' +
                ", AddDate='" + AddDate + '\'' +
                ", DisplaySequence=" + DisplaySequence +
                ", IsRelease=" + IsRelease +
                '}';
    }
}
