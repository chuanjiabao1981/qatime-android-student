package cn.qatime.player.activity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.qatime.player.R;
import cn.qatime.player.adapter.QuestionEditAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import libraryextra.bean.ImageItem;
import libraryextra.utils.StringUtils;
import libraryextra.view.GridViewForScrollView;

/**
 * @author luntify
 * @date 2017/8/15 15:37
 * @Description: 提问
 */

public class QuestionEditActivity extends BaseActivity implements View.OnClickListener {
    private ImageView control;
    private TextView time;
    private ProgressBar progress;
    private ImageView play;
    private EditText content;
    private EditText head;
    private String audioFileName;
    private MediaRecorder mRecorder;
    private boolean isRecording = false;
    private Disposable d;
    private MediaPlayer mediaPlayer;
    private List<ImageItem> list = new ArrayList<>();
    private QuestionEditAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_edit);
        setTitles("提问");
        initView();
    }

    private void initView() {

        control = (ImageView) findViewById(R.id.control);
        time = (TextView) findViewById(R.id.time);
        time.setText("60\"");
        progress = (ProgressBar) findViewById(R.id.progress);
        play = (ImageView) findViewById(R.id.play);
        GridViewForScrollView grid = (GridViewForScrollView) findViewById(R.id.grid);
        adapter = new QuestionEditAdapter(this, list);
        grid.setAdapter(adapter);
        content = (EditText) findViewById(R.id.content);
        head = (EditText) findViewById(R.id.head);
        play.setOnClickListener(this);
        control.setOnClickListener(this);
        adapter.setOnEventListener(new QuestionEditAdapter.OnEventListener() {
            @Override
            public void onDelete(int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == list.size()) {
                    Intent intent = new Intent(QuestionEditActivity.this, PictureSelectActivity.class);
//                intent.putExtra("gonecamera", true);
                    startActivityForResult(intent, Constant.REQUEST);
                } else {
                    ImageItem item = adapter.getItem(position);
                    showImage(item.imagePath);
                    Toast.makeText(QuestionEditActivity.this, "看大图", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showImage(String tag) {
        Intent intent = new Intent(this, WatchPictureActivity.class);
        intent.putExtra("src", tag);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
            if (data != null) {
                ImageItem image = (ImageItem) data.getSerializableExtra("data");
                list.add(image);
                adapter.notifyDataSetChanged();
            }
        } else if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
            if (data != null) {
                String url = data.getStringExtra("url");
                if (!StringUtils.isNullOrBlanK(url)) {
//                    Uri uri = null;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        uri = FileProvider.getUriForFile(this, "com.qatime.player.fileprovider", new File(url));
////                            Bitmap bitmap = BitmapFactory.decodeFile(url);
////                            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
////                            bitmap.recycle();
//                    } else {
//                        uri = Uri.fromFile(new File(url));
//                    }
                    ImageItem imageItem = new ImageItem();
                    imageItem.imagePath = url;
                    imageItem.thumbnailPath = url;
                    imageItem.imageId="";
                    list.add(imageItem);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                playOrPause();
                break;
            case R.id.control:
                if (StringUtils.isNullOrBlanK(audioFileName)) {
                    RxPermissions rxPermissions = new RxPermissions(this);
                    rxPermissions.request(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (aBoolean) {
                                        recordAudio();
                                    }
                                }
                            });
                } else {
                    if (mRecorder != null && isRecording) {//录音中    停止录音
                        d.dispose();
                        stopRecord();
                    } else {//已录制,删除
                        if (mediaPlayer != null) {
                            d.dispose();
                            releaseMediaPlayer();
                        }
                        File file = new File(audioFileName);
                        if (file.exists()) {
                            Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                            file.delete();
                        }
                        audioFileName = null;
                        control.setImageResource(R.mipmap.question_record);
                        play.setVisibility(View.GONE);
                        time.setTextColor(0xff999999);
                        time.setText("60\"");
                    }
                }
                break;
        }
    }

    private void playOrPause() {
        if (StringUtils.isNullOrBlanK(audioFileName)) {
            play.setVisibility(View.GONE);
            return;
        }
        if (!new File(audioFileName).exists()) {
            Toast.makeText(this, "语音文件不存在", Toast.LENGTH_SHORT).show();
            play.setVisibility(View.GONE);
            return;
        }
        if (mediaPlayer == null) {
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
                    mediaPlayer.start();
                }
            });

            play.setImageResource(R.mipmap.question_stop);
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
                            QuestionEditActivity.this.d = d;
                        }

                        @Override
                        public void onNext(Long aLong) {
                            time.setText((int) (aLong / 1000) + "\"");
                            progress.setMax(mediaPlayer.getDuration());
                            progress.setProgress(mediaPlayer.getCurrentPosition());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            releaseMediaPlayer();
                        }
                    });
        } else {
            d.dispose();
            releaseMediaPlayer();
        }
    }

    private void releaseMediaPlayer() {
        time.setText(mediaPlayer.getDuration() / 1000 + "\"");
        play.setImageResource(R.mipmap.question_play);
        d = null;
        progress.setMax(60);
        progress.setProgress(0);
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void stopRecord() {
        d = null;
        isRecording = false;
        Toast.makeText(this, "停止录音", Toast.LENGTH_SHORT).show();
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        progress.setMax(60);
        progress.setProgress(0);
        control.setImageResource(R.mipmap.question_delete);
        play.setVisibility(View.VISIBLE);
    }

    private void recordAudio() {
        audioFileName = Constant.CACHEPATH + "/audio";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File files = new File(audioFileName);
            if (!files.exists()) {
                files.mkdirs();
            }
            File[] file = files.listFiles();
            for (File f : file) {
                f.delete();
            }
        }
        audioFileName = audioFileName + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".aac";
        try {
            new File(audioFileName).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setOutputFile(audioFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
        isRecording = true;

        Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                QuestionEditActivity.this.d = d;
            }

            @Override
            public void onNext(Long aLong) {
                progress.setProgress(aLong.intValue());
                time.setText(aLong.intValue() + "\"");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                stopRecord();
            }
        };
        Observable.interval(1, TimeUnit.SECONDS)
                .take(61)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        progress.setMax(60);
                        time.setTextColor(0xff666666);
                        control.setImageResource(R.mipmap.question_stop);
                    }
                })
                .subscribe(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (d != null) {
            d.dispose();
        }
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
