package com.BFMe.BFMBuyer.javaBean;

import java.util.List;
import java.util.Map;

/**
 * Description：品牌列表bean
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/9/7 17:38
 */
public class BrandFiltrateBean {


    /**
     * data : {"A":[{"BrandChar":"A","BrandId":12428,"BrandName":"allwellness"},{"BrandChar":"A","BrandId":12632,"BrandName":"Ash"},{"BrandChar":"A","BrandId":12283,"BrandName":"AAPE"},{"BrandChar":"A","BrandId":12318,"BrandName":"ADAM"}],"B":[{"BrandChar":"B","BrandId":19563,"BrandName":"Bfme"},{"BrandChar":"B","BrandId":13113,"BrandName":"BMW"}],"C":[{"BrandChar":"C","BrandId":13730,"BrandName":"CR9"}],"D":[{"BrandChar":"D","BrandId":13944,"BrandName":"DG"},{"BrandChar":"D","BrandId":14067,"BrandName":"DQ"},{"BrandChar":"D","BrandId":13873,"BrandName":"DC"}],"E":[{"BrandChar":"E","BrandId":14355,"BrandName":"eS"},{"BrandChar":"E","BrandId":14336,"BrandName":"EOS"}],"F":[],"G":[],"H":[{"BrandChar":"H","BrandId":64,"BrandName":"好奇"},{"BrandChar":"H","BrandId":23,"BrandName":"HKC"}],"I":[],"J":[],"K":[],"L":[{"BrandChar":"L","BrandId":15766,"BrandName":"L & C"}],"M":[],"N":[],"O":[{"BrandChar":"O","BrandId":16972,"BrandName":"Oi Oi"},{"BrandChar":"O","BrandId":16956,"BrandName":"oe"}],"P":[],"Q":[],"R":[{"BrandChar":"R","BrandId":17622,"BrandName":"REFA"}],"S":[{"BrandChar":"S","BrandId":18113,"BrandName":"SK"}],"T":[],"U":[],"V":[{"BrandChar":"V","BrandId":18942,"BrandName":"VA"},{"BrandChar":"V","BrandId":19566,"BrandName":"vdfdfd"}],"W":[],"X":[],"Y":[],"Z":[]}
     * hotData : [{"BrandChar":"B","BrandId":19563,"BrandName":"Bfme"},{"BrandChar":"E","BrandId":14336,"BrandName":"EOS"},{"BrandId":0},{"BrandChar":"E","BrandId":14355,"BrandName":"eS"},{"BrandChar":"S","BrandId":18113,"BrandName":"SK"}]
     */

    private BrandBean Brand;

    public BrandBean getBrand() {
        return Brand;
    }

    public void setBrand(BrandBean Brand) {
        this.Brand = Brand;
    }

    public static class BrandBean {
        private Map<String,List<HotDataBean>> data;
        /**
         * BrandChar : B
         * BrandId : 19563
         * BrandName : Bfme
         */

        private List<HotDataBean> hotData;

        public Map<String, List<HotDataBean>> getData() {
            return data;
        }

        public void setData(Map<String, List<HotDataBean>> data) {
            this.data = data;
        }

        public List<HotDataBean> getHotData() {
            return hotData;
        }

        public void setHotData(List<HotDataBean> hotData) {
            this.hotData = hotData;
        }

        public static class HotDataBean {

            public HotDataBean() {
            }

            public HotDataBean(String brandChar) {
                BrandChar = brandChar;
            }

            private String BrandChar;
            private int BrandId;
            private String BrandName;

            public String getBrandChar() {
                return BrandChar;
            }

            public void setBrandChar(String BrandChar) {
                this.BrandChar = BrandChar;
            }

            public int getBrandId() {
                return BrandId;
            }

            public void setBrandId(int BrandId) {
                this.BrandId = BrandId;
            }

            public String getBrandName() {
                return BrandName;
            }

            public void setBrandName(String BrandName) {
                this.BrandName = BrandName;
            }
        }
    }
}
