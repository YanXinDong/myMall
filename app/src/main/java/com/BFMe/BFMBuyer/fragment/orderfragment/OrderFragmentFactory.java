package com.BFMe.BFMBuyer.fragment.orderfragment;

import java.util.HashMap;

/**
 * Description:Fragment工厂
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 闫信董
 * Date       : 2016/7/4 18:36
 */
public class OrderFragmentFactory {
    public static HashMap<Integer, BaseOrderFragment> hm = new HashMap<>();

    public static BaseOrderFragment getFragment(int position) {

        BaseOrderFragment fragment = hm.get(position);

        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new AllOrderFragment();
                    break;
                case 1:
                    fragment = new WaitPayFragment();
                    break;
                case 2:
                    fragment = new WaitSendFragment();
                    break;
                case 3:
                    fragment = new WaitReceiveFragment();
                    break;
                case 4:
                    fragment = new WaitCommentFragment();
                    break;
                default:
                    break;
            }
            hm.put(position,fragment);
        }
         return fragment;
    }
}
