package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：大家都在搜
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/4/17 15:49
 */
public class AllSearchBean {

    /**
     * Name : 洗面奶
     */

    private List<KeyWordsBean> KeyWords;

    public List<KeyWordsBean> getKeyWords() {
        return KeyWords;
    }

    public void setKeyWords(List<KeyWordsBean> KeyWords) {
        this.KeyWords = KeyWords;
    }

    public static class KeyWordsBean {
        private String Name;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
