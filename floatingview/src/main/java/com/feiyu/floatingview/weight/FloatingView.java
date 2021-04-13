package com.feiyu.floatingview.weight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.feiyu.floatingview.R;
import com.feiyu.floatingview.utils.ScreenUtils;

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
    private int mScreenHeight;
    private int mScreenWidth;
    private boolean mIsShow;
    private ImageView mSdv_cover;
    private GifView mGif_float;
    private int mDp167;
    private int mDp48;
    private boolean mLoading;
    private ValueAnimator mValueAnimator;
    private boolean moveVertical;
    private static int mFloatBallParamsX;
    private static int mFloatBallParamsY;

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
        mGif_float.setGifResource(R.mipmap.mailgif);
        mGif_float.play();

        initFloatBallParams(mContext);

        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);
        mDp167 = (int) ScreenUtils.dp2px(mContext, 167);
        mDp48 = (int) ScreenUtils.dp2px(mContext, 48);
//        slop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        slop = 3;
    }

    /**
     * 获取悬浮球的布局参数
     */
    private void initFloatBallParams(Context context) {

        mFloatBallParams = new WindowManager.LayoutParams();
        mFloatBallParams.flags = mFloatBallParams.flags
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            //避免悬浮球被通知栏部分遮挡
                |WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
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

    private int slop;
    private boolean isDrag;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (null != mValueAnimator && mValueAnimator.isRunning()) {
                    mValueAnimator.cancel();
                }

                setPressed(true);
                isDrag = false;
                inputStartX = (int) event.getRawX();
                inputStartY = (int) event.getRawY();
                viewStartX = mFloatBallParams.x;
                viewStartY = mFloatBallParams.y;

                break;
            case MotionEvent.ACTION_MOVE:

                inMovingX = (int) event.getRawX();
                inMovingY = (int) event.getRawY();
                int MoveX = viewStartX + inMovingX - inputStartX;
                int MoveY = viewStartY + inMovingY - inputStartY;

                if (mScreenHeight <= 0 || mScreenWidth <= 0) {
                    isDrag = false;
                    break;
                }

                if (Math.abs(inMovingX - inputStartX) > slop
                        && Math.abs(inMovingY - inputStartY) > slop) {
                    isDrag = true;

                    mFloatBallParams.x = MoveX;
                    mFloatBallParams.y = MoveY;
                    updateWindowManager();
                } else {
                    isDrag = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                }
                //吸附贴边计算和动画
                welt();
                break;
            default:
                break;
        }
        return isDrag || super.onTouchEvent(event);
    }

    private boolean isLeftSide() {
        return getX() == 0;
    }

    private boolean isRightSide() {
        return getX() == mScreenWidth - getWidth();
    }

    //吸附贴边计算和动画
    private void welt() {

        int movedX = mFloatBallParams.x;
        int movedY = mFloatBallParams.y;

        moveVertical = false;
        if (mFloatBallParams.y < getHeight() && mFloatBallParams.x >= slop && mFloatBallParams.x <= mScreenWidth - getWidth() - slop) {
            movedY = 0;
        } else if (mFloatBallParams.y > mScreenHeight - getHeight() * 2 && mFloatBallParams.x >= slop && mFloatBallParams.x <= mScreenWidth - getWidth() - slop) {
            movedY = mScreenHeight - getHeight();
        } else {
            moveVertical = true;
            if (mFloatBallParams.x < mScreenWidth / 2 - getWidth() / 2) {
                movedX = 0;
            } else {
                movedX = mScreenWidth - getWidth();
            }
        }

        int duration;
        if (moveVertical) {
            mValueAnimator = ValueAnimator.ofInt(mFloatBallParams.x, movedX);
            duration = movedX - mFloatBallParams.x;
        } else {
            mValueAnimator = ValueAnimator.ofInt(mFloatBallParams.y, movedY);
            duration = movedY - mFloatBallParams.y;
        }
        mValueAnimator.setDuration(Math.abs(duration));
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer level = (Integer) animation.getAnimatedValue();
                if (moveVertical) {
                    mFloatBallParams.x = level;
                } else {
                    mFloatBallParams.y = level;
                }
                updateWindowManager();
            }
        });
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (null != mValueAnimator && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }//进入下个页面的时候贴边动画暂停，下个页面attached时候会继续动画， 你手速快的话还能在中途接住球继续拖动
        super.onDetachedFromWindow();
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
    public ImageView CircleImageView() {
        return mSdv_cover;
    }

    /**
     * 显示悬浮
     */
    public void showFloat() {
        mIsShow = true;
        if (mFloatBallParamsX == -1 || mFloatBallParamsY == -1) {
            //首次打开时，初始化的位置
            mFloatBallParams.x = mScreenWidth - mDp48;
            mFloatBallParams.y = mScreenHeight - mDp167 - mDp48;

            mFloatBallParamsX = mFloatBallParams.x;
            mFloatBallParamsY = mFloatBallParams.y;
        } else {
            mFloatBallParams.x = mFloatBallParamsX;
            mFloatBallParams.y = mFloatBallParamsY;
        }
        mWindowManager.addView(this, mFloatBallParams);
        //吸附贴边计算和动画
        welt();
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
        mFloatBallParamsX = mFloatBallParams.x;
        mFloatBallParamsY = mFloatBallParams.y;
    }


    private void loadData() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        // TODO网络请求
    }

}
