package com.BFMe.BFMBuyer.javaBean;

/**
 * Description：
 * Created by :奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2016/10/9 15:57
 */
public class ImInfoBean {

    /**
     * IMUserId : bfme_724112c2e7f24d1caef4df55b
     * IMPassWord : c16bec23b3524972
     * IMUserName : 哈哈
     * IMUserPic : http://img01.bfme.com//Storage/Member/14762/e31f932f1a28686.jpg
     */

    private IMInfoBean IMInfo;

    public IMInfoBean getIMInfo() {
        return IMInfo;
    }

    public void setIMInfo(IMInfoBean IMInfo) {
        this.IMInfo = IMInfo;
    }

    public static class IMInfoBean {
        private String IMUserId;
        private String IMPassWord;
        private String IMUserName;
        private String IMUserPic;

        public String getIMUserId() {
            return IMUserId;
        }

        public void setIMUserId(String IMUserId) {
            this.IMUserId = IMUserId;
        }

        public String getIMPassWord() {
            return IMPassWord;
        }

        public void setIMPassWord(String IMPassWord) {
            this.IMPassWord = IMPassWord;
        }

        public String getIMUserName() {
            return IMUserName;
        }

        public void setIMUserName(String IMUserName) {
            this.IMUserName = IMUserName;
        }

        public String getIMUserPic() {
            return IMUserPic;
        }

        public void setIMUserPic(String IMUserPic) {
            this.IMUserPic = IMUserPic;
        }
    }
}
