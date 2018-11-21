package com.BFMe.BFMBuyer.fragment.commentfragment;

import java.util.HashMap;

/**
 * Description:Fragment工厂
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 奚兆进
 */
public class CommentFragmentFactory {
    public static HashMap<Integer, BaseCommentFragment> hm = new HashMap<>();

    public static BaseCommentFragment getFragment(int position) {

        BaseCommentFragment fragment = hm.get(position);

        if (fragment == null) {
            switch (position) {
                case 0:
                   fragment = new AllCommentFragment();
                    break;
                case 1:
                    fragment = new GoodCommentFragment();
                    break;
                case 2:
                    fragment = new MiddleCommentFrament();
                    break;
                case 3:
                    fragment = new BadCommentFragment();
                    break;
                default:
                    break;
            }
            hm.put(position,fragment);
        }
         return fragment;
    }
}
