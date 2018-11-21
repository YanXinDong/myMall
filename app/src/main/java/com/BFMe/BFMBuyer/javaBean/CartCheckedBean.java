package com.BFMe.BFMBuyer.javaBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/10.
 */
public class CartCheckedBean {

    @Override
    public String toString() {
        return "CartCheckedBean{" +
                "CartItems=" + CartItems +
                '}';
    }

    private  ArrayList<CartCheckeditems> CartItems;

    public ArrayList<CartCheckeditems> getCartItems() {
        return CartItems;
    }

    public void setCartItems(ArrayList<CartCheckeditems> cartItems) {
        CartItems = cartItems;
    }

    public static class CartCheckeditems{
        @Override
        public String toString() {
            return "CartCheckeditems{" +
                    "isChecked=" + isChecked +
                    ", CartItemsItems=" + CartItemsItems +
                    '}';
        }

        private boolean                   isChecked;
        private  ArrayList<CartCheckeditemsitem> CartItemsItems;

        public ArrayList<CartCheckeditemsitem> getCartItemsItems() {
            return CartItemsItems;
        }

        public void setCartItemsItems(ArrayList<CartCheckeditemsitem> cartItemsItems) {
            CartItemsItems = cartItemsItems;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public static class CartCheckeditemsitem{
            @Override
            public String toString() {
                return "CartCheckeditemsitem{" +
                        "isChecked=" + isChecked +
                        '}';
            }

            private boolean isChecked;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

        }
    }
}
