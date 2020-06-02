package com.floatview;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.feiyu.uvideoplayer.UniversalMediaController;
import com.feiyu.uvideoplayer.UniversalVideoView;

import java.util.ArrayList;

public class EggActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback {

    public static final String VIDEO_URL_02 = "http://jiajunhui.cn/video/kongchengji.mp4";
    public static final String VIDEO_URL_03 = "http://jiajunhui.cn/video/allsharestar.mp4";
    public static final String VIDEO_URL_04 = "http://jiajunhui.cn/video/edwin_rolling_in_the_deep.flv";
    public static final String VIDEO_URL_05 = "http://jiajunhui.cn/video/crystalliz.flv";
    public static final String VIDEO_URL_06 = "http://jiajunhui.cn/video/big_buck_bunny.mp4";
    public static final String VIDEO_URL_07 = "http://jiajunhui.cn/video/trailer.mp4";

    private ArrayList<String> urls = new ArrayList<>();
    private static final String SEEK_POSITION_KEY = "0x001";
    private View mVideoLayout;
    private UniversalVideoView mVideoView;
    private UniversalMediaController mMediaController;
    private int cachedHeight;
    private int mSeekPosition;
    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egg);

        urls.add(VIDEO_URL_02);
        urls.add(VIDEO_URL_03);
        urls.add(VIDEO_URL_04);
        urls.add(VIDEO_URL_05);
        urls.add(VIDEO_URL_06);

        mVideoLayout = findViewById(R.id.video_layout);
        View txt_next = findViewById(R.id.txt_next);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);

        //进度回调
        mMediaController.setProgressChangedCallback(new UniversalMediaController.ValidateSeekBarCallBack() {

            @Override
            public void onProgressChangedCallBack(SeekBar seekBar, int progress, boolean dragging) {

                mSeekPosition = progress;

            }
        });

        //下一个视频
        txt_next.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View v) {
                mVideoView.setVideoPath(urls.get((++i % urls.size())));
                mVideoView.requestFocus();
            }
        });
    }

    private void setVideoAreaSize() {

        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                //   cachedHeight = (int) (width * 405f / 720f);
                //  cachedHeight = (int) (width * 3f / 4f);
                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(VIDEO_URL_02);
               // mVideoView.setVideoResId(R.raw.video456165154ninlop);
                mVideoView.requestFocus();
            }
        });
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
        }

        switchActionBar(!isFullscreen);
    }

    private void switchActionBar(boolean show) {

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        if (null != mMediaController) {
            mMediaController.removeTimer();
        }
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        if (null != mMediaController) {
            mMediaController.startTimer();
        }
    }


    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }

    @Override
    protected void onStop() {
        mVideoView.pausePlayer();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != mVideoView) {
            mSeekPosition = mVideoView.getCurrentPosition();
        }

        if (isFinishing()) {

            if (null != mMediaController) {
                mMediaController.removeTimer();
            }

            if (null != mVideoView) {
                mVideoView.closePlayer();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mVideoView) {
            mSeekPosition = mVideoView.getCurrentPosition();
        }
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        mVideoView.seekTo(mSeekPosition);
    }

}