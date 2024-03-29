package cn.qatime.player.view;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.qatime.player.R;
import cn.qatime.player.activity.WatchPictureActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.ImageItem;
import libraryextra.utils.StringUtils;
import libraryextra.view.GridViewForScrollView;

public class ExpandView extends FrameLayout implements View.OnClickListener {

    private boolean mIsExpand;
    private TextView time;
    private TextView content;
    private String audioFileName;
    private Disposable d;
    private MediaPlayer mediaPlayer;
    private List<ImageItem> list;
    private int initHeight;
    private CommonAdapter<ImageItem> adapter;
    private View audioPlay;
    private ImageView audioAnim;

    public ExpandView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public ExpandView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public ExpandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public void expand() {
        if (!mIsExpand) {
            mIsExpand = true;
            doAnim(300, 0, initHeight);
        }
    }


    public void collapse() {
        if (mIsExpand) {
            mIsExpand = false;
            doAnim(300, initHeight, 0);
        }
    }


    private void doAnim(int duration, final int start, final int end) {
        ValueAnimator mAnimator = ValueAnimator.ofInt(0, 100);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                int animatorValue = (int) animation.getAnimatedValue();
                float fraction = animatorValue / 100f;
                IntEvaluator mEvaluator = new IntEvaluator();
                getLayoutParams().height = mEvaluator.evaluate(fraction, start, end);
                requestLayout();
            }
        });
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(duration);
        mAnimator.setTarget(this);
        mAnimator.start();
    }

    public boolean isExpand() {
        return mIsExpand;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.question_detail_expand, null);
        removeAllViews();
        addView(view);
        mIsExpand = true;
    }

    public void initExpandView(String content, String audioUrl, List<ImageItem> imageList, final boolean show) {
        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        initHeight = getHeight();
                        if (!show) {
                            doAnim(0, initHeight, 0);
                            mIsExpand = false;
                        }
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
        this.content = (TextView) findViewById(R.id.content);
        if (!StringUtils.isNullOrBlanK(content)) {
            this.content.setVisibility(VISIBLE);
            this.content.setText(content);
        } else {
            this.content.setVisibility(GONE);
        }
        this.audioFileName = audioUrl;
        this.list = imageList;

        if (this.list == null) {//不管有没有都要加载  否则复用会造成下边空白
            this.list = new ArrayList<>();
        }
        GridViewForScrollView grid = (GridViewForScrollView) findViewById(R.id.grid);
        if(list.size()>0){
            grid.setVisibility(VISIBLE);
        }else{
            grid.setVisibility(GONE);
        }
        adapter = new CommonAdapter<ImageItem>(getContext(), list, R.layout.item_question_image) {
            @Override
            public void convert(ViewHolder holder, ImageItem item, int position) {
                ImageView view = (ImageView) holder.getView(R.id.image);
                Glide.with(getContext()).load(item.imagePath).placeholder(R.mipmap.default_image).crossFade().centerCrop().into(view);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = view.getWidth();
                view.setLayoutParams(layoutParams);
            }
        };
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem item = adapter.getItem(position);
                Intent intent = new Intent(getContext(), WatchPictureActivity.class);
                intent.putExtra("imageItems", (Serializable) list);
                intent.putExtra("position", position);
                getContext().startActivity(intent);
            }
        });
        time = (TextView) findViewById(R.id.time);
        audioAnim = (ImageView) findViewById(R.id.audio_animation);
        audioPlay = findViewById(R.id.audio_layout);
        if (audioUrl != null) {
            audioPlay.setVisibility(VISIBLE);
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioFileName);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    time.setText((mediaPlayer.getDuration() / 1000) + "\"");
                }
            });
        } else {
            audioPlay.setVisibility(GONE);
        }
        audioPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio_layout:
                playOrPause();
                break;
        }
    }

    private void playOrPause() {
        if (!mediaPlayer.isPlaying()) {
            audioAnim.setBackgroundResource(R.drawable.audio_animation_list_left);
            if (audioAnim.getBackground() instanceof AnimationDrawable) {
                AnimationDrawable animation = (AnimationDrawable) audioAnim.getBackground();
                animation.start();
            }
            mediaPlayer.start();
            Observable.interval(1, TimeUnit.SECONDS)
                    .takeWhile(new Predicate<Long>() {
                        @Override
                        public boolean test(Long aLong) throws Exception {
                            return mediaPlayer.isPlaying();
                        }
                    })
                    .map(new Function<Long, Long>() {
                        @Override
                        public Long apply(Long aLong) throws Exception {
                            return (long) (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            ExpandView.this.d = d;
                        }

                        @Override
                        public void onNext(Long aLong) {
                            time.setText((int) (aLong / 1000) + "\"");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            resetMediaPlayer();
                        }
                    });
        } else {
            d.dispose();
            resetMediaPlayer();
        }
    }

    private void resetMediaPlayer() {
        if (audioAnim.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) audioAnim.getBackground();
            animation.stop();
            audioAnim.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
        }
        d = null;
        mediaPlayer.stop();
        mediaPlayer.prepareAsync();
    }

    @Override
    protected void onDetachedFromWindow() {//view销毁
        super.onDetachedFromWindow();
        if (d != null) {
            d.dispose();
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}