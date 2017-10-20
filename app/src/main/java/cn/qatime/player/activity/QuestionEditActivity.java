package cn.qatime.player.activity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.qatime.player.R;
import cn.qatime.player.adapter.QuestionEditAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.AttachmentsBean;
import cn.qatime.player.bean.QuestionsBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.RecorderUtil;
import cn.qatime.player.utils.UrlUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import libraryextra.bean.ImageItem;
import libraryextra.rx.HttpManager;
import libraryextra.rx.body.ProgressResponseCallBack;
import libraryextra.rx.callback.SimpleCallBack;
import libraryextra.rx.exception.ApiException;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
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
    private boolean isRecording = false;
    private Disposable d;
    private MediaPlayer mediaPlayer;
    private List<ImageItem> list = new ArrayList<>();
    private List<AttachmentsBean> imageAttachmentList = new ArrayList<>();
    private QuestionEditAdapter adapter;
    private int courseId;
    private Handler mp3Handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RecorderUtil.RECORDER_OK:
                    audioAttachment.file_url = audioFileName;
                    audioAttachment.file_type = "mp3";
                    play.setImageResource(R.mipmap.refresh);
                    addAttachments(audioAttachment);
                    break;
                case RecorderUtil.RECORDER_NG://error
                    Toast.makeText(QuestionEditActivity.this, "录音失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });
    private RecorderUtil recorderUtil;
    private AttachmentsBean audioAttachment = new AttachmentsBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_edit);
        setTitles("提问");
        courseId = getIntent().getIntExtra("courseId", 0);
        initView();
        setRightImage(R.mipmap.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadQuestion();
            }
        });
    }

    private void uploadQuestion() {
        KeyBoardUtils.closeKeybord(this);
        String title = head.getText().toString().trim();
        String body = content.getText().toString().trim();
        if (StringUtils.isNullOrBlanK(title)) {
            Toast.makeText(this, "请输入提问标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(body)) {
            Toast.makeText(this, "请输入提问内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (list.size() != imageAttachmentList.size()) {//判断图片有没有全部上传成功
            Toast.makeText(this, "图片上传未完成", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isNullOrBlanK(audioFileName) && audioAttachment.id == null) {//有语音但是上传失败或没上传完成
            Toast.makeText(this, "语音暂未上传", Toast.LENGTH_SHORT).show();
            return;
        }
        QuestionsBean.DataBean.AnswerBean answerBean = new QuestionsBean.DataBean.AnswerBean();
        answerBean.setBody(body);
        List<AttachmentsBean> attachments = new ArrayList<>();
        attachments.addAll(imageAttachmentList);
        if (!StringUtils.isNullOrBlanK(audioAttachment.file_url)) {
            attachments.add(audioAttachment);
        }
        answerBean.setAttachments(attachments);
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("body", content.getText().toString().trim());
        if (answerBean.getAttachments().size() > 0) {
            map.put("quotes_attributes", getContentString(answerBean));
        }
        JSONObject obj = new JSONObject(map);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlGroups + courseId + "/questions", obj,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        try {
                            JSONObject error = response.getJSONObject("error");
                            if (error.getInt("code") == 3002) {
                                Toast.makeText(QuestionEditActivity.this, error.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private String getContentString(QuestionsBean.DataBean.AnswerBean answerBean) {
        StringBuilder sb = new StringBuilder("[");
        for (AttachmentsBean attachment : answerBean.getAttachments()) {
            sb.append("{\"attachment_id\":\"")
                    .append(attachment.id)
                    .append("\"},");
        }
        if (sb.length() > 1) {
            sb.setCharAt(sb.length() - 1, ']');
            Logger.e(sb.toString());
            return sb.toString();
        } else {
            return "";
        }
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
                if (list.get(position).status != ImageItem.Status.UPLOADING) {
                    ImageItem remove = list.remove(position);
                    adapter.notifyDataSetChanged();
                    AttachmentsBean removeItem = new AttachmentsBean();
                    removeItem.file_url = remove.imagePath;
                    removeItem.id = "";
                    imageAttachmentList.remove(removeItem);
                } else {
                    Toast.makeText(QuestionEditActivity.this, "正在上传，请稍候", Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText(QuestionEditActivity.this, "看大图", Toast.LENGTH_SHORT).show();
                    ImageItem item = adapter.getItem(position);
                    Intent intent = new Intent(QuestionEditActivity.this, WatchPictureActivity.class);
                    intent.putExtra("src", item.imagePath);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
            if (data != null) {
                ImageItem imageItem = (ImageItem) data.getSerializableExtra("data");
                if (!StringUtils.isNullOrBlanK(imageItem.imagePath)) {
                    list.add(imageItem);
                    AttachmentsBean attachment = new AttachmentsBean();
                    attachment.file_url = imageItem.imagePath;
                    attachment.file_type = "image";
                    addAttachments(attachment);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "无效的路径", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                    imageItem.imageId = "";
                    AttachmentsBean attachment = new AttachmentsBean();
                    attachment.file_url = imageItem.imagePath;
                    attachment.file_type = "image";
                    addAttachments(attachment);
                    list.add(imageItem);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void addAttachments(final AttachmentsBean attachment) {
        final String path = attachment.file_url;
        File file = new File(path);
        if (file.exists()) {
            HttpManager.post(UrlUtils.urlLiveStudio + "attachments")
                    .headers("Remember-Token", BaseApplication.getInstance().getProfile().getToken())
                    .params("file", file, new ProgressResponseCallBack() {
                        @Override
                        public void onResponseProgress(long bytesWritten, long contentLength, boolean done) {
                        }
                    })
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onStart() {
                            if ("image".equals(attachment.file_type)) {
                                ImageItem item = new ImageItem();
                                item.imagePath = path;
                                int position = list.lastIndexOf(item);
                                if (position != -1) {
                                    list.get(position).status = ImageItem.Status.UPLOADING;
                                }
                            }
                        }

                        @Override
                        public void onError(ApiException e) {
                            if ("image".equals(attachment.file_type)) {
                                ImageItem item = new ImageItem();
                                item.imagePath = path;
                                int position = list.lastIndexOf(item);
                                if (position != -1) {
                                    list.get(position).status = ImageItem.Status.ERROR;
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(QuestionEditActivity.this, "语音上传失败，点击重试", Toast.LENGTH_SHORT).show();
                                audioAttachment.id = null;
                            }
                            super.onError(e);
                        }

                        @Override
                        public void onSuccess(String o) {
                            try {
                                JSONObject response = new JSONObject(o);
                                String id = response.getJSONObject("data").getString("id");
                                if ("image".equals(attachment.file_type)) {
                                    ImageItem item = new ImageItem();
                                    item.imagePath = path;
                                    int position = list.lastIndexOf(item);
                                    if (position != -1) {
                                        list.get(position).status = ImageItem.Status.SUCCESS;
                                    }
                                    attachment.id = id;
                                    imageAttachmentList.add(attachment);
                                } else {
                                    audioAttachment.id = id;
                                    play.setImageResource(R.mipmap.question_play);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onTokenOut() {
                        }
                    });
        } else {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            if ("image".equals(attachment.file_type)) {
                ImageItem item = new ImageItem();
                item.imagePath = path;
                int position = list.indexOf(item);
                if (position != -1) {
                    list.get(position).status = ImageItem.Status.ERROR;
                    adapter.notifyDataSetChanged();
                }
            } else {
                audioAttachment.id = null;
                audioAttachment.file_url = null;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (!StringUtils.isNullOrBlanK(audioFileName) && audioAttachment.id != null) {//有语音 上传完成
                    playOrPause();
                } else {
                    if (!StringUtils.isNullOrBlanK(audioFileName)) {
                        audioAttachment.file_url = audioFileName;
                        audioAttachment.file_type = "mp3";
                        addAttachments(audioAttachment);
                    }
                }
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
                    if (recorderUtil != null && isRecording) {//录音中    停止录音
                        d.dispose();
                        stopRecord();
                    } else {//已录制,删除
                        if (mediaPlayer != null) {
                            d.dispose();
                            releaseMediaPlayer();
                        }
                        File file = new File(audioFileName);
                        if (file.exists()) {
                            file.delete();
                        }
                        audioAttachment.id = null;
                        audioAttachment.file_url = null;//置空
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
        recorderUtil.stopRawRecording();
//        mRecorder.stop();
//        mRecorder.release();
//        mRecorder = null;
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
        audioFileName = audioFileName + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".mp3";
        try {
            new File(audioFileName).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.CHANNEL_IN_MONO);
//        mRecorder.setOutputFile(audioFileName);
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.ENCODING_PCM_16BIT);
//        try {
//            mRecorder.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mRecorder.start();
        recorderUtil = new RecorderUtil(Constant.CACHEPATH + "/audio", mp3Handler);
        isRecording = recorderUtil.startMp3Recording(audioFileName);

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
//        if (mRecorder != null) {
//            mRecorder.stop();
//            mRecorder.release();
//            mRecorder = null;
//        }
        if (recorderUtil != null) {
            recorderUtil.stopRawRecording();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
