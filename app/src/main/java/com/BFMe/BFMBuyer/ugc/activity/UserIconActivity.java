package com.BFMe.BFMBuyer.ugc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.BFMe.BFMBuyer.R;
import com.bumptech.glide.Glide;

/**
 * UGC用户主页头像放大页面
 */
public class UserIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_icon_activty);
        String imageUrl = getIntent().getStringExtra("imageUrl");
        ImageView iv_icon = (ImageView) findViewById(R.id.iv_icon);
        Glide.with(this).load(imageUrl).into(iv_icon);
        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public static void start(Context context, View view, String imageUrl) {
        Intent starter = new Intent(context, UserIconActivity.class);
        starter.putExtra("imageUrl",imageUrl);
        ActivityCompat.startActivity((Activity) context, starter, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, view, "translate_view").toBundle());
    }
}
