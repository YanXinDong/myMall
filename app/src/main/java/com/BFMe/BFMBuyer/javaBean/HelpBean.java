package com.BFMe.BFMBuyer.javaBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 * 帮助中心bean
 */
public class HelpBean implements Serializable{

    @Override
    public String toString() {
        return "HelpBean{" +
                "HelpCenterNav=" + HelpCenterNav +
                '}';
    }

    /**
     * Id : 36
     * CategoryId : 9
     * Title : 常见问题
     * Content : <p><span style=";font-family:宋体;font-size:14px">1<span style="font-family:宋体">、白富美海淘订单什么时候发货？大概多长时间可以收到货？</span></span></p><p><span style=";font-family:宋体;font-size:14px"><span style="font-family:宋体"> &nbsp;一般情况下，自您下单后，卖家会在</span><span style="font-family:Calibri">24</span><span style="font-family:宋体">小时之内发货（如遇重大活动，最长不超过</span><span style="font-family:Calibri">72</span><span style="font-family:宋体">小时）。发货之后，您可以在我的订单中查看发货状态。由于快递公司可能无法实时更新信息，因此您看到的发货情况可能有一定延迟。</span></span></p><p><span style=";font-family:宋体;font-size:14px">&nbsp;</span></p><p><span style=";font-family:宋体;font-size:14px">2<span style="font-family:宋体">、为什么后下的订单，先收到了，之前下的订单还没有收到？你们是按什么顺序发货的？</span></span></p><p><span style=";font-family:宋体;font-size:14px"><span style="font-family:宋体"> &nbsp;由于订单商品所在的国家不同，因此可能会出现订单发货和下单顺序不完全一致的情况。我们会尽一切努力为您尽快发货。</span></span></p><p><span style=";font-family:宋体;font-size:14px">&nbsp;</span></p><p><span style=";font-family:宋体;font-size:14px">3<span style="font-family:宋体">、订单显示发货了，但快递信息为什么还没有更新？</span></span></p><p><span style=";font-family:宋体;font-size:14px"><span style="font-family:宋体"> &nbsp;您在白富美海淘订单页面看到的物流查询信息由快递公司传递提供。由于快递公司信息更新会有一定延迟，请您多多谅解。</span></span></p><p><span style=";font-family:宋体;font-size:14px">&nbsp;</span></p><p><span style=";font-family:宋体;font-size:14px">4<span style="font-family:宋体">、产品收到后，发现漏发了或者错发，应该怎么办？</span></span></p><p><span style=";font-family:宋体;font-size:14px"><span style="font-family:宋体"> &nbsp;请您先准备好漏发，错发的相关图片（拍摄图片），再通过客服核实，待客服确认后会进行相关处理。</span></span></p><p><span style=";font-family:宋体;font-size:14px">&nbsp;</span></p><p><span style=";font-family:宋体;font-size:14px">5<span style="font-family:宋体">、为什么我已经收到产品，但是订单仍显示正在配送？</span></span></p><p><span style="font-family: 宋体; font-size: 14px;">&nbsp; 由于不同快递公司物流信息更新时间略有不同，当白富美海淘订单发货后，部分快递信息可能会延迟返回。</span></p><p><span style=";font-family:宋体;font-size:14px">&nbsp;</span></p><p><span style=";font-family:宋体;font-size:14px">6<span style="font-family:宋体">、白富美海淘使用什么快递公司为客户发货？如何收费？</span></span></p><p><span style="font-family: 宋体; font-size: 14px;">&nbsp; 白富美海淘与明邦转运合作配送商品，您在白富美海淘成功购买商品的每一个订单（限一个邮寄地址），系统都会默认生成一个包裹。</span></p><p><span style="font-family: 宋体; font-size: 14px;">&nbsp; 普通快递暂时不提供自选，我们会根据订单中的收货地址和商品种类选择最合适的物流公司为您进行配送。</span></p><p style="margin: 0px; text-indent: 0px; padding: 0px; background: rgb(255, 255, 255);"><br/></p>
     * AddDate : /Date(1449653769000-0000)/
     * DisplaySequence : 1
     * IsRelease : true
     */


    private List<HelpCenterNavData> HelpCenterNav;

    public List<HelpCenterNavData> getHelpCenterNav() {
        return HelpCenterNav;
    }

    public void setHelpCenterNav(List<HelpCenterNavData> HelpCenterNav) {
        this.HelpCenterNav = HelpCenterNav;
    }

    public static class HelpCenterNavData implements Serializable{
        private int Id;
        private int CategoryId;
        private String Title;
        private String Content;
        private String AddDate;
        private int DisplaySequence;
        private boolean IsRelease;

        @Override
        public String toString() {
            return "HelpCenterNavData{" +
                    "Id=" + Id +
                    ", CategoryId=" + CategoryId +
                    ", Title='" + Title + '\'' +
                    ", Content='" + Content + '\'' +
                    ", AddDate='" + AddDate + '\'' +
                    ", DisplaySequence=" + DisplaySequence +
                    ", IsRelease=" + IsRelease +
                    '}';
        }

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
    }
}
