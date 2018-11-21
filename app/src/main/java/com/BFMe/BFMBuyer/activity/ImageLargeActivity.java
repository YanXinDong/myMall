package com.BFMe.BFMBuyer.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.BFMe.BFMBuyer.R;
import com.BFMe.BFMBuyer.utils.UiUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class ImageLargeActivity extends AppCompatActivity {
    private ImageView largeImage;
    private LinearLayout llCircle;
    private ViewPager viewpager;
    private ArrayList<String> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_large);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        llCircle = (LinearLayout) findViewById(R.id.ll_cricle);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        imagesList = intent.getStringArrayListExtra("images");
        setCircle(position);
        viewpager.setAdapter(new MyImageAdapter());
        viewpager.setCurrentItem(position);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < llCircle.getChildCount(); i++) {
                    ImageView childAt = (ImageView) llCircle.getChildAt(i);
                    if (imagesList.size() > 0) {
                        if (i == position % imagesList.size()) {
                            childAt.setImageResource(R.drawable.active);
                        } else {
                            childAt.setImageResource(R.drawable.inactive);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setCircle(int position) {
        llCircle.removeAllViews();
        for (int i = 0; i < imagesList.size(); i++) {
            ImageView circle = new ImageView(ImageLargeActivity.this);
            if (i == position) {
                circle.setImageResource(R.drawable.active);
            } else {
                circle.setImageResource(R.drawable.inactive);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = UiUtils.px2dp(30);
            }
            circle.setLayoutParams(params);
            llCircle.addView(circle);
        }
    }

    private class MyImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imagesList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View inflate = View.inflate(ImageLargeActivity.this, R.layout.dialog_photo_entry, null);
            largeImage = (ImageView) inflate.findViewById(R.id.large_image);

            final ObjectAnimator anim = ObjectAnimator.ofInt(largeImage, "ImageLevel", 0, 10000);
            anim.setDuration(2000);
            anim.setRepeatCount(ObjectAnimator.INFINITE);
            anim.start();
            Glide
                    .with(ImageLargeActivity.this)
                    .load(imagesList.get(position))
                    .placeholder(R.drawable.rotate_pro)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            anim.cancel();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            anim.cancel();
                            return false;
                        }
                    })
                    .error(R.drawable.rotate_pro)
                    .into(largeImage);
            container.addView(inflate);
            return inflate;
        }
    }

    /**
     * 点击屏幕某个控件外finish页面
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = viewpager;
            if (!isRangeOfView(view, ev)) {
                click();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isRangeOfView(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (event.getX() < x || event.getX() > (x + view.getWidth()) || event.getY() < y
                || event.getY() > (y + view.getHeight())) {
            return false;
        }

        return true;
    }

    //关闭页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void click() {
        finish();
        overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
    }

}
