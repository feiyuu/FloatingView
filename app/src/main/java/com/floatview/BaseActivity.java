package com.floatview;

import androidx.appcompat.app.AppCompatActivity;

import com.feiyu.floatingview.weight.FloatingView;

public class BaseActivity extends AppCompatActivity {

    private FloatingView floatingView;

    @Override
    protected void onResume() {
        super.onResume();
        if (null == floatingView) {
            floatingView = new FloatingView(this);
            floatingView.showFloat();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (floatingView!=null) {
            floatingView.dismissFloatView();
            floatingView=null;
        }
    }
}
