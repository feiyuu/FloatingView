package com.floatview;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.feiyu.floatingview.weight.FloatingView;

public class BaseActivity extends AppCompatActivity {

    private FloatingView floatingView;
    private int numberMask;

    @Override
    protected void onResume() {
        super.onResume();
        if (null == floatingView) {
            floatingView = new FloatingView(this);
            floatingView.showFloat();
            floatingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numberMask++;
                    if (numberMask == 3) {
                        Toast.makeText(BaseActivity.this, "恭喜你发现了隐藏页面！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BaseActivity.this, EggActivity.class));
                    } else {
                        Toast.makeText(BaseActivity.this, "你点击了", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            Glide.with(getApplicationContext()).load("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3874919870,2456972669&fm=26&gp=0.jpg")
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(floatingView.CircleImageView());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (floatingView != null) {
            floatingView.dismissFloatView();
            floatingView = null;
        }
    }
}
