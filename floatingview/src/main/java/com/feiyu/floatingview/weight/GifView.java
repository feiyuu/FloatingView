package com.feiyu.floatingview.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * @author feiyu
 * @Description: (播放gif工具类)
 * 支持SDK_INT >=16
 */
public class GifView extends View {

    private static final int DEFAULT_MOVIE_VIEW_DURATION = 1000;

    private int mMovieResourceId;
    private Movie movie;

    private long mMovieStart;
    private int mCurrentAnimationTime;


    /**
     * Position for drawing animation frames in the center of the view.
     */
    private float mLeft;
    private float mTop;

    /**
     * Scaling factor to fit the animation within view bounds.
     */
    private float mScale;

    /**
     * Scaled movie frames width and height.
     */
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;

    private volatile boolean mPaused;
    private boolean mVisible = true;
    private int screenHeight;
    private int screenWidth;
    private float scaleH = 1f;
    private float scaleW = 1f;
    private int scaleType = 0;
    public static int FIT_CENTER = 0x001;
    private boolean mOneShot = true;

    public GifView(Context context) {
        this(context, null);
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        screenHeight = getScreenHeight(getContext());
        screenWidth = getScreenWidth(getContext());
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setViewAttributes(context, attrs, defStyle);
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }

    @SuppressLint("NewApi")
    private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {

        /**
         * Starting from HONEYCOMB(Api Level:11) have to turn off HW acceleration to draw
         * Movie on Canvas.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public void setGifResource(int movieResourceId) {
        this.mMovieResourceId = movieResourceId;
        movie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        requestLayout();
    }

    public int getGifResource() {

        return this.mMovieResourceId;
    }


    public void play() {
        if (this.mPaused) {
            this.mPaused = false;

            /**
             * Calculate new movie start time, so that it resumes from the same
             * frame.
             */
            mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;

            invalidate();
        }
    }

    public void pause() {
        if (!this.mPaused) {
            this.mPaused = true;

            invalidate();
        }

    }


    public boolean isPaused() {
        return this.mPaused;
    }

    public boolean isPlaying() {
        return !this.mPaused;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (movie != null) {
            int movieWidth = movie.width();
            int movieHeight = movie.height();

            /*
             * Calculate horizontal scaling
             */

            int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);

            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
//                int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
//                if (movieWidth > maximumWidth) {
//                }
            }

            scaleW = (float) MeasureSpec.getSize(widthMeasureSpec) / (float) movieWidth;

            /*
             * calculate vertical scaling
             */

            int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);

            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
//                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
//                if (movieHeight > maximumHeight) {
//                }
            }

            scaleH = (float) MeasureSpec.getSize(heightMeasureSpec) / (float) movieHeight;

            /*
             * calculate overall scale
             */
            mScale = Math.min(scaleH, scaleW);


            if (FIT_CENTER == scaleType) {
                scaleH = scaleW = mScale;
            }

            mMeasuredMovieWidth = (int) (movieWidth * scaleW);
            mMeasuredMovieHeight = (int) (movieHeight * scaleH);

            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);

        } else {
            /*
             * No movie set, just set minimum available size.
             */
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /*
         * Calculate mLeft / mTop for drawing in center
         */
        mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
        mTop = (getHeight() - mMeasuredMovieHeight) / 2f;

        mVisible = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (movie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    /**
     * Invalidates view only if it is mVisible.
     * <br>
     * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
     */
    @SuppressLint("NewApi")
    private void invalidateView() {

        if (mVisible && !mPaused) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }

            play();

        } else {
            pause();
        }
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        int dur = movie.duration();

        if (dur == 0) {
            dur = DEFAULT_MOVIE_VIEW_DURATION;
        }

        if (now - mMovieStart >= dur && mOneShot) {
            this.mPaused = true;
            return;
        }
        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    /**
     * Draw current GIF frame
     */
    @SuppressLint("WrongConstant")
    private void drawMovieFrame(Canvas canvas) {
        movie.setTime(mCurrentAnimationTime);
        canvas.save();
        canvas.scale(scaleW, scaleH);
        movie.draw(canvas, mLeft / scaleW, mTop / scaleH);
        canvas.restore();
    }

//    @SuppressLint("NewApi")
//    @Override
//    public void onScreenStateChanged(int screenState) {
//        super.onScreenStateChanged(screenState);
//        mVisible = screenState == SCREEN_STATE_ON;
//        invalidateView();
//    }

//    @SuppressLint("NewApi")
//    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//        mVisible = visibility == View.VISIBLE;
//        invalidateView();
//    }
//
//    @Override
//    protected void onWindowVisibilityChanged(int visibility) {
//        super.onWindowVisibilityChanged(visibility);
//        mVisible = visibility == View.VISIBLE;
//        invalidateView();
//    }

    public int getScreenWidth(Context context) {
        @SuppressLint("WrongConstant") WindowManager wm = (WindowManager) context.getSystemService("window");
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public int getScreenHeight(Context context) {
        @SuppressLint("WrongConstant") WindowManager wm = (WindowManager) context.getSystemService("window");
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public void setOnShot(boolean oneShot) {
        mOneShot = oneShot;
    }
}