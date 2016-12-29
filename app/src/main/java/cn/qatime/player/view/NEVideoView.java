package cn.qatime.player.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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

import cn.qatime.player.R;
import libraryextra.utils.ScreenUtils;


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
    private View mBuffer;
    private NELivePlayer.OnCompletionListener mOnCompletionListener;
    private NELivePlayer.OnPreparedListener mOnPreparedListener;
    private NELivePlayer.OnErrorListener mOnErrorListener;
    private NELivePlayer.OnSeekCompleteListener mOnSeekCompleteListener;
    private NELivePlayer.OnInfoListener mOnInfoListener;
    private NELivePlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangeListener;
    private long mSeekWhenPrepared;
    private boolean mHardwareDecoder = false;
    private boolean mPauseInBackground = false;
    private String mMediaType = "livestream";//直播livestream  点播videoondemand


    private boolean isBackground;
//    private NEVideoViewReceiver mReceiver;

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


    private void initVideoView() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        getHolder().addCallback(mSHCallback);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
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
//            Logger.e(TAG, "onVideoSizeChanged: " + width + "x" + height);
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            if (mOnVideoSizeChangeListener != null) {
                mOnVideoSizeChangeListener.onVideoSizeChanged(mp, width, height, sarNum, sarDen);
            }
//            mPixelSarNum = sarNum;
//            mPixelSarDen = sarDen;
            if (mVideoWidth != 0 && mVideoHeight != 0 && getId() == R.id.video2)
                setSelfSize(0, 0);
        }
    };

    OnPreparedListener mPreparedListener = new OnPreparedListener() {
        public void onPrepared(NELivePlayer mp) {
//            Logger.e(TAG, "onPrepared");
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
            if (mVideoWidth != 0 && mVideoHeight != 0 && getId() == R.id.video2) {
                setSelfSize(0, 0);
                if (mNextState == STARTED) {
                    start();
                }
            } else if (mNextState == STARTED) {
                start();
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
            if (mOnBufferingUpdateListener != null)
                mOnBufferingUpdateListener.onBufferingUpdate(mp, percent);
        }
    };

    private OnInfoListener mInfoListener = new OnInfoListener() {
        @Override
        public boolean onInfo(NELivePlayer mp, int what, int extra) {
//            Logger.e(TAG, "onInfo: " + what + ", " + extra);
            if (mOnInfoListener != null) {
                mOnInfoListener.onInfo(mp, what, extra);
            }

            if (mMediaPlayer != null) {
                ImageView image1 = (ImageView) mBuffer.findViewById(R.id.buffer_image1);

                if (what == NELivePlayer.NELP_BUFFERING_START) {
//                    Logger.e(TAG, "onInfo: NELP_BUFFERING_START");
                    if (mBuffer != null) {
                        mBuffer.setVisibility(View.VISIBLE);
                        if (image1 != null) {
                            ((AnimationDrawable) image1.getBackground()).start();
                        } else {
                            ((AnimationDrawable) (mBuffer.findViewById(R.id.buffer_image2)).getBackground()).start();
                        }
                    }
                } else if (what == NELivePlayer.NELP_BUFFERING_END) {
//                    Logger.e(TAG, "onInfo: NELP_BUFFERING_END");
                    if (mBuffer != null) {
                        if (image1 != null) {
                            ((AnimationDrawable) image1.getBackground()).stop();
                        } else {
                            ((AnimationDrawable) (mBuffer.findViewById(R.id.buffer_image2)).getBackground()).stop();
                        }
                        mBuffer.setVisibility(View.GONE);
                    }
                } else if (what == NELivePlayer.NELP_FIRST_VIDEO_RENDERED) {
//                    Logger.e(TAG, "onInfo: NELP_FIRST_VIDEO_RENDERED");
                    if (mBuffer != null) {
                        if (image1 != null) {
                            ((AnimationDrawable) image1.getBackground()).stop();
                        } else {
                            ((AnimationDrawable) (mBuffer.findViewById(R.id.buffer_image2)).getBackground()).stop();
                        }
                        mBuffer.setVisibility(View.GONE);
                    }
                } else if (what == NELivePlayer.NELP_FIRST_AUDIO_RENDERED) {
                    if (mBuffer != null) {
                        if (image1 != null) {
                            ((AnimationDrawable) image1.getBackground()).stop();
                        } else {
                            ((AnimationDrawable) (mBuffer.findViewById(R.id.buffer_image2)).getBackground()).stop();
                        }
                        mBuffer.setVisibility(View.GONE);
                    }
//                    Logger.e(TAG, "onInfo: NELP_FIRST_AUDIO_RENDERED");
                }
            }

            return true;
        }
    };

    private OnSeekCompleteListener mSeekCompleteListener = new OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(NELivePlayer mp) {
//            Logger.e(TAG, "onSeekComplete");
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

            if (mMediaPlayer != null && mIsPrepared && mVideoWidth == w && mVideoHeight == h) {
                if (mSeekWhenPrepared != 0)
                    seekTo(mSeekWhenPrepared);
                start();
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
                    start();
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

    public boolean isPlaying() {
        return mMediaPlayer != null && mIsPrepared && mMediaPlayer.isPlaying();
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

    /**
     * 根据父view大小,决定自身大小
     *
     * @param parentWidth
     * @param parentHeight
     */
    public void setSelfSize(int parentWidth, int parentHeight) {
        float winRatio = 0;
        if (parentWidth <= 0 || parentHeight <= 0) {
            parentWidth = ScreenUtils.getScreenWidth(getContext());
            parentHeight = parentWidth * 9 / 16;
            winRatio = 16.0f / 9;
        } else {
            winRatio = (float) parentWidth / parentHeight;
        }
        float videoRatio = (float) mVideoWidth / mVideoHeight;
        LayoutParams params = getLayoutParams();
        if (winRatio > videoRatio) {
            params.height = parentHeight;
            params.width = (int) (parentHeight * videoRatio);
        } else {
            params.width = parentWidth;
            params.height = (int) (parentWidth / videoRatio);
        }
        setLayoutParams(params);
        getHolder().setFixedSize(params.width, params.height);
    }

}

