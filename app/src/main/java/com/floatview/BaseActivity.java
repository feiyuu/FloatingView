package com.floatview;

import androidx.appcompat.app.AppCompatActivity;

import com.feiyu.floatingview.weight.FloatingView;


/**
 * @author weijian.huang 2020/1/15
 * @Package com.floatview
 * @Title: BaseActivity
 * @Description: (用一句话描述该文件做什么)
 * Copyright (c) 传化公路港物流有限公司版权所有
 * Create DateTime: 2020/1/15
 */
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
