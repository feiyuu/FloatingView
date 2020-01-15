package com.floatview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Main3Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ImageView img = findViewById(R.id.img);
        Glide.with(getApplicationContext()).load("https://goss.veer.com/creative/vcg/veer/800water/veer-309677502.jpg")
                .into(img);
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main3Activity.this,MainActivity.class));
            }
        });
    }
}
