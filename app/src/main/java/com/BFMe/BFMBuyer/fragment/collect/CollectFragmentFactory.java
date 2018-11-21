package com.BFMe.BFMBuyer.fragment.collect;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/12 15:55
 */
public class CollectFragmentFactory {

    public static HashMap<Integer, Fragment> hm = new HashMap<>();

    public static Fragment getFragment(int position) {

        Fragment fragment = hm.get(position);

        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new CollectCommodityFragment();
                    break;
                case 1:
                    fragment = new AttentionShopFragment();
                    break;
                default:
                    break;
            }
            hm.put(position,fragment);
        }
        return fragment;
    }

}
