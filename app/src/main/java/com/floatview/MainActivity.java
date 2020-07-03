package com.floatview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = findViewById(R.id.img);
        Glide.with(getApplicationContext()).load("https://goss.veer.com/creative/vcg/veer/800water/veer-134671947.jpg")
                .into(img);

        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
        System.out.println();
        Log.i("aasd12aaa", "onCreate: "+isPowOfTwo(2784)+"=====================2784");
    }
    public static boolean isPowOfTwo(int n) {
        int temp = 0;
        for (int i = 1; ; i++) {
            temp = (int) Math.pow(2, i);
            if (temp >= n)
                break;
        }
        if (temp == n) return true;
        else return false;
    }
}
