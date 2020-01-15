package com.floatview.weight;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.floatview.R;
import com.floatview.utils.CacheUtil;
import com.floatview.utils.ScreenUtils;


/**
 * @author weijian.huang 2019/12/16
 * @Package com.transfar.peaceinn.ui.module.ximalaya
 * @Title: XimalayaFloatView
 * @Description: (用一句话描述该文件做什么)
 * Copyright (c) 传化公路港物流有限公司版权所有
 * Create DateTime: 2019/12/16
 */
public class FloatingView extends RelativeLayout {
    private static final String TAG = "FloatingView";
    private int inputStartX = 0;
    private int inputStartY = 0;
    private int viewStartX = 0;
    private int viewStartY = 0;
    private int inMovingX = 0;
    private int inMovingY = 0;

    private WindowManager.LayoutParams mFloatBallParams;
    private WindowManager mWindowManager;
    private Context mContext;
    private final int mScreenHeight;
    private final int mScreenWidth;
    private boolean mIsShow;
    private ImageView mSdv_cover;
    private GifView mGif_float;
    private int mDp94;
    private int mDp48;
    private boolean mLoading;

    public boolean isShow() {
        return mIsShow;
    }

    public FloatingView(Context context) {
        this(context, null);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        inflate(context, R.layout.floating_view, this);

        mSdv_cover = findViewById(R.id.sdv_cover);
        mGif_float = findViewById(R.id.gif_float);
        mGif_float.setOnShot(false);
        mGif_float.setGifResource(R.mipmap.float_gif);
        mGif_float.play();

        initFloatBallParams(mContext);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);
        mDp94 = (int) ScreenUtils.dp2px(mContext, 167);
        mDp48 = (int) ScreenUtils.dp2px(mContext, 48);
        CacheUtil.open(mContext);
    }

    /**
     * 获取悬浮球的布局参数
     */
    private void initFloatBallParams(Context context) {
        mFloatBallParams = new WindowManager.LayoutParams();
        mFloatBallParams.flags = mFloatBallParams.flags
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mFloatBallParams.dimAmount = 0.2f;

//      mFloatBallParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        mFloatBallParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mFloatBallParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

        mFloatBallParams.gravity = Gravity.LEFT | Gravity.TOP;
        mFloatBallParams.format = PixelFormat.RGBA_8888;
        // 设置整个窗口的透明度
        mFloatBallParams.alpha = 1.0f;
        // 显示悬浮球在屏幕左上角
        mFloatBallParams.x = 0;
        mFloatBallParams.y = 0;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                inputStartX = (int) event.getRawX();
                inputStartY = (int) event.getRawY();
                viewStartX = mFloatBallParams.x;
                viewStartY = mFloatBallParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                inMovingX = (int) event.getRawX();
                inMovingY = (int) event.getRawY();

                mFloatBallParams.x = viewStartX + inMovingX - inputStartX;
                mFloatBallParams.y = viewStartY + inMovingY - inputStartY;
                updateWindowManager();
                break;
            case MotionEvent.ACTION_UP:

                if (mFloatBallParams.y < 200) {
                    mFloatBallParams.y = 0;
                } else if (mFloatBallParams.y > mScreenHeight - 200) {
                    mFloatBallParams.y = mScreenHeight - getHeight();
                } else {
                    if (mFloatBallParams.x < mScreenWidth / 2) {
                        mFloatBallParams.x = 0;
                    } else if (mFloatBallParams.x > mScreenWidth / 2) {
                        mFloatBallParams.x = mScreenWidth - getWidth();
                    }
                }
                updateWindowManager();

                break;
            default:
                break;
        }

        Log.v("X-Y",
                "mScreenHeight: " + mScreenHeight + "\n"
                        + "mScreenHeight - getHeight(): " + (mScreenHeight - getHeight()) + "\n"
        );
//        Log.v("X-Y",
//                "inputStartX: " + inputStartX + "\n"
//                        + "inputStartY: " + inputStartY + "\n"
//                        + "viewStartX: " + viewStartX + "\n"
//                        + "viewStartY: " + viewStartY + "\n"
//                        + "inMovingX: " + inMovingX + "\n"
//                        + "inMovingY: " + inMovingY + "\n");
        return super.onTouchEvent(event);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 贴图片
     */
    public void loadImageView() {
        if (null != mSdv_cover) {
            Glide.with(mContext.getApplicationContext()).load("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3874919870,2456972669&fm=26&gp=0.jpg")
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(mSdv_cover);
        }
    }

    /**
     * 显示悬浮
     */
    public void showFloat() {
        mIsShow = true;
        loadImageView();
        int floatBallParamsX = CacheUtil.getInt("floatBallParamsX", -1);
        int floatBallParamsY = CacheUtil.getInt("floatBallParamsY", -1);
        if (floatBallParamsX == -1 || floatBallParamsY == -1) {
            mFloatBallParams.x = mScreenWidth - mDp48;
            mFloatBallParams.y = mScreenHeight - mDp94 - mDp48;
            CacheUtil.putInt("floatBallParamsX", mFloatBallParams.x);
            CacheUtil.putInt("floatBallParamsY", mFloatBallParams.y);
        } else {
            mFloatBallParams.x = floatBallParamsX;
            mFloatBallParams.y = floatBallParamsY;
        }
        mWindowManager.addView(this, mFloatBallParams);
    }

    /**
     * 移除该view
     */
    public void dismissFloatView() {
        mIsShow = false;
        mWindowManager.removeViewImmediate(this);
    }

    //更新位置，并保存到手机内存
    public void updateWindowManager() {
        mWindowManager.updateViewLayout(this, mFloatBallParams);
        CacheUtil.putInt("floatBallParamsX", mFloatBallParams.x);
        CacheUtil.putInt("floatBallParamsY", mFloatBallParams.y);
    }


    private void loadData() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        //网络请求
    }

}
