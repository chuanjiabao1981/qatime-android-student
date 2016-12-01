package cn.qatime.player.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

import com.netease.neliveplayer.NELivePlayer;
import com.netease.neliveplayer.NELivePlayer.OnBufferingUpdateListener;
import com.netease.neliveplayer.NELivePlayer.OnCompletionListener;
import com.netease.neliveplayer.NELivePlayer.OnErrorListener;
import com.netease.neliveplayer.NELivePlayer.OnInfoListener;
import com.netease.neliveplayer.NELivePlayer.OnPreparedListener;
import com.netease.neliveplayer.NELivePlayer.OnSeekCompleteListener;
import com.netease.neliveplayer.NELivePlayer.OnVideoSizeChangedListener;
import com.netease.neliveplayer.NEMediaPlayer;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.qatime.player.R;


public class NEVideoView extends SurfaceView {
    private static final String TAG = "NELivePlayerNEVideoView";

    private static final int IDLE = 0;
    private static final int INITIALIZED = 1;
    private static final int PREPARING = 2;
    private static final int PREPARED = 3;
    private static final int STARTED = 4;
    private static final int PAUSED = 5;
    private static final int STOPED = 6;
    private static final int PLAYBACKCOMPLETED = 7;
    private static final int END = 8;
    private static final int RESUME = 9;
    private static final int ERROR = -1;
    private static Context mContext = null;

    private int mCurrState = IDLE;
    private int mNextState = IDLE;

    //    public static final int NELP_LOG_UNKNOWN = 0; //!< log输出模式：输出详细
//    public static final int NELP_LOG_DEFAULT = 1; //!< log输出模式：输出详细
//    public static final int NELP_LOG_VERBOSE = 2; //!< log输出模式：输出详细
//    public static final int NELP_LOG_DEBUG   = 3; //!< log输出模式：输出调试信息
//    public static final int NELP_LOG_INFO    = 4; //!< log输出模式：输出标准信息
//    public static final int NELP_LOG_WARN    = 5; //!< log输出模式：输出警告
//    public static final int NELP_LOG_ERROR   = 6; //!< log输出模式：输出错误
//    public static final int NELP_LOG_FATAL   = 7; //!< log输出模式：一些错误信息，如头文件找不到，非法参数使用
//    public static final int NELP_LOG_SILENT  = 8; //!< log输出模式：不输出

    private Uri mUri;
    private long mDuration = 0;
    private SurfaceHolder mSurfaceHolder = null;
    private NELivePlayer mMediaPlayer = null;
    private boolean mIsPrepared; //播放器是否准备完成
    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private View mBuffer;
    private NELivePlayer.OnCompletionListener mOnCompletionListener;
    private NELivePlayer.OnPreparedListener mOnPreparedListener;
    private NELivePlayer.OnErrorListener mOnErrorListener;
    private NELivePlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private NELivePlayer.OnInfoListener mOnInfoListener;
    private NELivePlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangeListener;
    private int mCurrentBufferPercentage;
    private long mSeekWhenPrepared;
    private boolean mHardwareDecoder = false;
    private boolean mPauseInBackground = false;
    private String mMediaType = "livestream";//直播livestream  点播videoondemand


    private boolean isBackground;
    private boolean manualPause = false;
    private NEVideoViewReceiver mReceiver;

    public NEVideoView(Context context) {
        super(context);
        NEVideoView.mContext = context;
        initVideoView();
    }

    public NEVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        NEVideoView.mContext = context;
        initVideoView();
    }

    public NEVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        NEVideoView.mContext = context;
        initVideoView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * @param orientation true时表示横屏
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setVideoScalingMode(boolean orientation) {
        LayoutParams layPara = getLayoutParams();
        int winWidth = 0;
        int winHeight = 0;
        Rect rect = new Rect();
        this.getWindowVisibleDisplayFrame(rect);//获取状态栏高度
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay(); //获取屏幕分辨率
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { //new
            DisplayMetrics metrics = new DisplayMetrics();
            display.getRealMetrics(metrics);
            winWidth = metrics.widthPixels;
            winHeight = metrics.heightPixels - rect.top;
        } else { //old
            try {
                Method mRawWidth = Display.class.getMethod("getRawWidth");
                Method mRawHeight = Display.class.getMethod("getRawHeight");
                winWidth = (Integer) mRawWidth.invoke(display);
                winHeight = (Integer) mRawHeight.invoke(display) - rect.top;
            } catch (NoSuchMethodException e) {
                DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
                winWidth = dm.widthPixels;
                winHeight = dm.heightPixels;
                e.printStackTrace();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        float winRatio = (float) winWidth / winHeight;
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            float aspectRatio = (float) (mVideoWidth) / mVideoHeight;
            mSurfaceHeight = mVideoHeight;
            mSurfaceWidth = mVideoWidth;

//            if (VIDEO_SCALING_MODE_NONE == videoScalingMode && mSurfaceWidth < winWidth && mSurfaceHeight < winHeight) {
//                layPara.width = (int) (mSurfaceHeight * aspectRatio);
//                layPara.height = mSurfaceHeight;
//            } else if (VIDEO_SCALING_MODE_FIT == videoScalingMode) { //拉伸
            if (winRatio < aspectRatio) {
                layPara.width = winWidth;
                layPara.height = (int) (winWidth / aspectRatio);
            } else {
                layPara.width = (int) (aspectRatio * winHeight);
                layPara.height = winHeight;
            }
//            } else if (VIDEO_SCALING_MODE_FILL == videoScalingMode) { //满屏
//                layPara.width = winWidth;
//                layPara.height = winHeight;
//            } else if (VIDEO_SCALING_MODE_FULL == videoScalingMode) { //全屏
//                if (winRatio < aspectRatio) {
//                    layPara.width = (int) (winHeight * aspectRatio);
//                    layPara.height = winHeight;
//                } else {
//                    layPara.width = winWidth;
//                    layPara.height = (int) (winWidth / aspectRatio);
//                }
//            } else {
//                if (winRatio < aspectRatio) {
//                    layPara.width = (int) (aspectRatio * winHeight);
//                    layPara.height = winHeight;
//                } else {
//                    layPara.width = winWidth;
//                    layPara.height = (int) (winWidth / aspectRatio);
//                }
//            }
            if (orientation) {
                layPara.height = winHeight + rect.top;
                layPara.width = winWidth;
            }
            setLayoutParams(layPara);
            getHolder().setFixedSize(layPara.width, layPara.height);
//            Logger.e(TAG, "Video: width = " + mVideoWidth + ", height = " + mVideoHeight);
//            Logger.e(TAG, "Surface: width = " + mSurfaceWidth + ", height = " + mSurfaceHeight);
//            Logger.e(TAG, "Window:width = " + winWidth + ", height = " + winHeight);
//            Logger.e(TAG, "LayoutParams:width = " + layPara.width + ", height = " + layPara.height);
        }

//        mVideoScalingMode = videoScalingMode;
    }

    private void initVideoView() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        getHolder().addCallback(mSHCallback);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        registerBroadCast();
        mCurrState = IDLE;
        mNextState = IDLE;
    }

    public void setVideoPath(String path) { //设置视频文件路径
        isBackground = false; //指示是否在后台
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        mUri = uri;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrState = END;
            mNextState = END;
        }
    }

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }

        // Tell the music playback service to pause
        //  these constants need to be published somewhere in the framework
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);

        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        try {

            NEMediaPlayer neMediaPlayer = null;
            if (mUri != null) {
                neMediaPlayer = new NEMediaPlayer();
            }
            mMediaPlayer = neMediaPlayer;
//            getLogPath();
//            mMediaPlayer.setLogPath(mLogLevel, mLogPath);
            mMediaPlayer.setBufferStrategy(0);//设置缓冲策略，0为直播低延时，1为点播抗抖动
            mMediaPlayer.setHardwareDecoder(mHardwareDecoder);//设置是否开启硬件解码，0为软解，1为硬解
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mIsPrepared = false;
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
            if (mUri != null) {
                //设置播放地址，返回0正常，返回-1则说明地址非法，需要使用网易视频云官方生成的地址
                int ret = mMediaPlayer.setDataSource(mUri.toString());
                if (ret < 0) {
                    if (getWindowToken() != null && mMediaType.equals("livestream")) {
//                        new AlertDialog.Builder(mContext)
//                                .setTitle("error")
//                                .setMessage("地址非法，请输入网易视频云官方地址！")
//                                .setPositiveButton("OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                        /* If we get here, there is no onError listener, so
//                                         * at least inform them that the video is over.
//                                         */
                        if (mOnCompletionListener != null)
                            mOnCompletionListener.onCompletion(mMediaPlayer);
//                                            }
//                                        })
//                                .setCancelable(false)
//                                .show();
                    }
                    release_resource();
                    return;
                }
                mCurrState = INITIALIZED;
                mNextState = PREPARING;
            }
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync(mContext); //初始化视频文件
            mCurrState = PREPARING;
//            attachMediaController();
        } catch (IOException ex) {
            Logger.e(TAG, "Unable to open content: " + mUri, ex);
            mErrorListener.onError(mMediaPlayer, -1, 0);
            return;
        } catch (IllegalArgumentException ex) {
            Logger.e(TAG, "Unable to open content: " + mUri, ex);
            mErrorListener.onError(mMediaPlayer, -1, 0);
            return;
        }
    }


    public void setBufferPrompt(View buffer) {
        if (mBuffer != null) {
            mBuffer.setVisibility(View.GONE);
        }
        mBuffer = buffer;
    }


    OnVideoSizeChangedListener mSizeChangedListener = new OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(NELivePlayer mp, int width, int height, int sarNum, int sarDen) {
            Logger.e(TAG, "onVideoSizeChanged: " + width + "x" + height);
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            if (mOnVideoSizeChangeListener != null) {
                mOnVideoSizeChangeListener.onVideoSizeChanged(mp, width, height, sarNum, sarDen);
            }
//            mPixelSarNum = sarNum;
//            mPixelSarDen = sarDen;
//            if (mVideoWidth != 0 && mVideoHeight != 0)
//                setVideoScalingMode(false);
        }
    };

    OnPreparedListener mPreparedListener = new OnPreparedListener() {
        public void onPrepared(NELivePlayer mp) {
            Logger.e(TAG, "onPrepared");
            mCurrState = PREPARED;
            mNextState = STARTED;
            // briefly show the mediacontroller
            mIsPrepared = true;

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            if (mSeekWhenPrepared != 0)
                seekTo(mSeekWhenPrepared);
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                setVideoScalingMode(false);
                if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                    if (mNextState == STARTED) {
                        if (!isPaused()) {
                            start();
                        }
                    }
                }
            } else if (mNextState == STARTED) {
                if (!isPaused()) {
                    start();
                } else {
                    pause();
                }
            }
        }
    };

    private OnCompletionListener mCompletionListener = new OnCompletionListener() {
        public void onCompletion(NELivePlayer mp) {
            Logger.e(TAG, "onCompletion");
            mCurrState = PLAYBACKCOMPLETED;
            if (mOnCompletionListener != null)
                mOnCompletionListener.onCompletion(mMediaPlayer);

//            if (getWindowToken() != null && mMediaType.equals("livestream")) {
//                new AlertDialog.Builder(mContext)
//                        .setTitle("Completed!")
//                        .setMessage("播放结束！")
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        /* If we get here, there is no onError listener, so
//                                         * at least inform them that the video is over.
//                                         */
//                                        if (mOnCompletionListener != null)
//                                            mOnCompletionListener.onCompletion(mMediaPlayer);
//                                    }
//                                })
//                        .setCancelable(false)
//                        .show();
//            }
        }
    };

    private OnErrorListener mErrorListener = new OnErrorListener() {
        public boolean onError(NELivePlayer mp, int a, int b) {
            Logger.e(TAG, "Error: " + a + "," + b);
            mCurrState = ERROR;

            /* If an error handler has been supplied, use it and finish. */
            if (mOnErrorListener != null) {
                if (mOnErrorListener.onError(mMediaPlayer, a, b)) {
                    return true;
                }
            }

//            if (getWindowToken() != null) {
//                new AlertDialog.Builder(mContext)
//                        .setTitle("Error")
//                        .setMessage("播放拉流失败")
//                        .setPositiveButton("Ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        /* If we get here, there is no onError listener, so
//                                         * at least inform them that the video is over.
//                                         */
//                                        if (mOnCompletionListener != null)
//                                            mOnCompletionListener.onCompletion(mMediaPlayer);
//                                    }
//                                })
//                        .setCancelable(false)
//                        .show();
//            }
            return true;
        }
    };

    private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {
        public void onBufferingUpdate(NELivePlayer mp, int percent) {
            mCurrentBufferPercentage = percent;
            if (mOnBufferingUpdateListener != null)
                mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
        }
    };

    private OnInfoListener mInfoListener = new OnInfoListener() {
        @Override
        public boolean onInfo(NELivePlayer mp, int what, int extra) {
            Logger.e(TAG, "onInfo: " + what + ", " + extra);
            if (mOnInfoListener != null) {
                mOnInfoListener.onInfo(mp, what, extra);
            }

            if (mMediaPlayer != null) {
                ImageView image1 = (ImageView) mBuffer.findViewById(R.id.buffer_image1);

                if (what == NELivePlayer.NELP_BUFFERING_START) {
                    Logger.e(TAG, "onInfo: NELP_BUFFERING_START");
                    if (mBuffer != null) {
                        mBuffer.setVisibility(View.VISIBLE);
                        if (image1 != null) {
                            ((AnimationDrawable) image1.getBackground()).start();
                        } else {
                            ((AnimationDrawable) (mBuffer.findViewById(R.id.buffer_image2)).getBackground()).start();
                        }
                    }
                } else if (what == NELivePlayer.NELP_BUFFERING_END) {
                    Logger.e(TAG, "onInfo: NELP_BUFFERING_END");
                    if (mBuffer != null) {
                        mBuffer.setVisibility(View.GONE);
                        if (image1 != null) {
                            ((AnimationDrawable) image1.getBackground()).stop();
                        } else {
                            ((AnimationDrawable) (mBuffer.findViewById(R.id.buffer_image2)).getBackground()).stop();
                        }
                    }
                } else if (what == NELivePlayer.NELP_FIRST_VIDEO_RENDERED) {
                    Logger.e(TAG, "onInfo: NELP_FIRST_VIDEO_RENDERED");
                    if (mBuffer != null) {
                        mBuffer.setVisibility(View.GONE);
                        if (image1 != null) {
                            ((AnimationDrawable) image1.getBackground()).stop();
                        } else {
                            ((AnimationDrawable) (mBuffer.findViewById(R.id.buffer_image2)).getBackground()).stop();
                        }
                    }
                } else if (what == NELivePlayer.NELP_FIRST_AUDIO_RENDERED) {
                    if (mBuffer != null) {
                        mBuffer.setVisibility(View.GONE);
                        if (image1 != null) {
                            ((AnimationDrawable) image1.getBackground()).stop();
                        } else {
                            ((AnimationDrawable) (mBuffer.findViewById(R.id.buffer_image2)).getBackground()).stop();
                        }
                    }
                    Logger.e(TAG, "onInfo: NELP_FIRST_AUDIO_RENDERED");
                }
            }

            return true;
        }
    };

    private OnSeekCompleteListener mSeekCompleteListener = new OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(NELivePlayer mp) {
            Logger.e(TAG, "onSeekComplete");
            if (mOnSeekCompleteListener != null)
                mOnSeekCompleteListener.onSeekComplete(mp);
        }
    };

    public void setOnVideoSizeChangeListener(OnVideoSizeChangedListener l) {
        this.mOnVideoSizeChangeListener = l;
    }

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(OnErrorListener l) {
        mOnErrorListener = l;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
        mOnBufferingUpdateListener = l;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener l) {
        mOnSeekCompleteListener = l;
    }

    public void setOnInfoListener(OnInfoListener l) {
        mOnInfoListener = l;
    }

    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            mSurfaceHolder = holder;
            if (mMediaPlayer != null) {
                mMediaPlayer.setDisplay(mSurfaceHolder);
            }

            mSurfaceWidth = w;
            mSurfaceHeight = h;
            if (mMediaPlayer != null && mIsPrepared && mVideoWidth == w && mVideoHeight == h) {
                if (mSeekWhenPrepared != 0)
                    seekTo(mSeekWhenPrepared);
                if (!isPaused()) {
                    start();
                }
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceHolder = holder;

            if (mNextState != RESUME && !isBackground) {
                openVideo();
            } else {
                if (mHardwareDecoder) {
                    openVideo();
                    isBackground = false; //不在后台
                } else if (mPauseInBackground) {
                    //mMediaPlayer.setDisplay(mSurfaceHolder);
                    if (!isPaused())
                        start();
                    isBackground = false; //不在后台
                }
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            mSurfaceHolder = null;
            if (mMediaPlayer != null) {
                if (mHardwareDecoder) {
                    mSeekWhenPrepared = mMediaPlayer.getCurrentPosition();
                    if (mMediaPlayer != null) {
                        mMediaPlayer.reset();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                        mCurrState = IDLE;
                    }
                    isBackground = true;
                } else if (!mPauseInBackground) {
                    mMediaPlayer.setDisplay(null);
                    isBackground = true;
                } else {
                    pause();
                    isBackground = true;
                }

                mNextState = RESUME;
            }
        }
    };


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mIsPrepared &&
                keyCode != KeyEvent.KEYCODE_BACK
                && keyCode != KeyEvent.KEYCODE_VOLUME_UP
                && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
                && keyCode != KeyEvent.KEYCODE_MENU
                && keyCode != KeyEvent.KEYCODE_CALL
                && keyCode != KeyEvent.KEYCODE_ENDCALL
                && mMediaPlayer != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                    || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                    || keyCode == KeyEvent.KEYCODE_SPACE) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                } else {
                    if (!isPaused()) {
                        start();
                    }
                }
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void start() {
        if (mMediaPlayer != null && mIsPrepared) {
            mMediaPlayer.start();
            mCurrState = STARTED;
        }
        mNextState = STARTED;
    }

    public void pause() {
        if (mMediaPlayer != null && mIsPrepared) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrState = PAUSED;
            }
        }
        mNextState = PAUSED;
    }

    public int getDuration() {
        if (mMediaPlayer != null && mIsPrepared) {
            if (mDuration > 0)
                return (int) mDuration;
            mDuration = mMediaPlayer.getDuration();
            return (int) mDuration;
        }

        return -1;
    }


    public int getCurrentPosition() {
        if (mMediaPlayer != null && mIsPrepared) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(long msec) {
        if (mMediaPlayer != null && mIsPrepared) {
            mMediaPlayer.seekTo(msec);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = msec;
        }
    }

    public void seekAndChangeUrl(long msec, String path) {
        mUri = Uri.parse(path);
        //mMediaPlayer.stop();
        stopPlayback();
        mSeekWhenPrepared = msec;
        openVideo();
        requestLayout();
        invalidate();
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null && mIsPrepared) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void manualPause(boolean paused) {
        manualPause = paused;
    }

    public boolean isPaused() {
        //return (mCurrentState == PLAY_STATE_PAUSED) ? true : false;
        return manualPause;
    }

    public int getBufferPercentage() {
        if (mMediaPlayer != null)
            return mCurrentBufferPercentage;
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public void setMediaType(String MediaType) {
        mMediaType = MediaType;
    }

    public String getMediaType() {
        return mMediaType;
    }

    public boolean isHardware() {
        return mHardwareDecoder;
    }

    public void setHardwareDecoder(boolean enabled) {
        mHardwareDecoder = enabled;
        if (mHardwareDecoder) {
            mPauseInBackground = true;
        }
    }

    public boolean isInBackground() {
        return isBackground;
    }

    public void setPauseInBackground(boolean enabled) {
        mPauseInBackground = enabled;

        if (mHardwareDecoder) {
            mPauseInBackground = true;
        }
    }

//    //获取日志文件路径
//    public void getLogPath() {
//        try {
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                mLogPath = Environment.getExternalStorageDirectory() + "/qatime/video/";
//            } else {
//                Logger.e("lognonono");
//            }
//        } catch (Exception e) {
//            Logger.e(TAG, "an error occured while writing file...", e);
//        }
//    }

    public void release_resource() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrState = IDLE;
        }
    }

    // 以下用于接收资源释放成功的消息

    /**
     * 注册监听器
     */
    private void registerBroadCast() {
        unRegisterBroadCast();
        mReceiver = new NEVideoViewReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NEMediaPlayer.NELP_RELEASE_SUCCESS);
        mContext.registerReceiver(mReceiver, filter);
    }

    /**
     * 反注册监听器
     */
    private void unRegisterBroadCast() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    /**
     * 资源释放成功通知的消息接收器类
     */
    private class NEVideoViewReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NEMediaPlayer.NELP_RELEASE_SUCCESS)) {
                Log.i(TAG, NEMediaPlayer.NELP_RELEASE_SUCCESS);
                unRegisterBroadCast(); // 接收到消息后反注册监听器
            }
        }
    }

}

