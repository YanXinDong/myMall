package com.BFMe.BFMBuyer.ugc.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Description：话题状态Fragment工厂
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/20 11:15
 */
public class TopicFragmentFactory {
    private static HashMap<Integer, BaseTopicFragment> hm = new HashMap<>();

    public static Fragment getFragment(int position) {

        BaseTopicFragment fragment = hm.get(position);

        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new AllTopicFragment();
                    break;
                case 1:
                    fragment = new PassTopicFragment();
                    break;
                case 2:
                    fragment = new AuditTopicFragment();
                    break;
                case 3:
                    fragment = new NoAuditTopicFragment();
                    break;
                default:
                    break;
            }
            hm.put(position,fragment);
        }
        return fragment;
    }

}
