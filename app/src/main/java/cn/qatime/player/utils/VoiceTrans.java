package cn.qatime.player.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import libraryextra.utils.FileUtil;
import libraryextra.utils.VolleyErrorListener;

/**
 */
public class VoiceTrans {
    private static final String TAG = VoiceTrans.class.getSimpleName();

    private final Activity baseActivity;

    // view
    private View textLayout;
    private TextView voiceTransText;
    private View cancelBtn;
    private ProgressBar refreshingIndicator;
    private View failIcon;

    private AbortableFuture<String> callFuture;

    public VoiceTrans(Activity baseActivity) {
        this.baseActivity = baseActivity;
        findViews();
        setListener();
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) baseActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (baseActivity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(baseActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void findViews() {
        textLayout = baseActivity.findViewById(R.id.voice_trans_layout);
        if (textLayout == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
            textLayout = layoutInflater.inflate(R.layout.voice_trans_layout, null);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            baseActivity.addContentView(textLayout, layoutParams);
        }
        voiceTransText = (TextView) textLayout.findViewById(R.id.voice_trans_text);
        cancelBtn = textLayout.findViewById(R.id.cancel_btn);
        refreshingIndicator = (ProgressBar) textLayout.findViewById(R.id.refreshing_indicator);
        failIcon = textLayout.findViewById(R.id.trans_fail_icon);
    }

    private void setListener() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        textLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    public void hide() {
        if (callFuture != null) {
            callFuture.abort();
        }
        voiceTransText.scrollTo(0, 0);
        textLayout.setVisibility(View.GONE);
    }

    public void show() {
        hideKeyBoard();
        textLayout.setVisibility(View.VISIBLE);
        voiceTransText.setText("正在转换");
    }

    public boolean isShow() {
        return textLayout.getVisibility() == View.VISIBLE;
    }

    public void voiceToText(IMMessage msg) {
        AudioAttachment attachment = (AudioAttachment) msg.getAttachment();
        String path = attachment.getPath();
        String speech = Base64.encodeToString(FileUtil.File2byte(path), Base64.DEFAULT);
        refreshStartUI();

        Map<String, String> map = new HashMap<>();
        map.put("format", "amr");
        map.put("rate", "8000");
        map.put("channel", "1");
        map.put("cuid", "8789341");
        map.put("token", "heKtzNNNShIkzh9A4FGepo2DU4wbyV5k");
        map.put("speech", speech);
        map.put("len", String.valueOf(attachment.getSize()));

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl("http://vop.baidu.com/server_api", map),
                null,
//                new VolleyListener(baseActivity) {
//                    @Override
//                    protected void onSuccess(JSONObject response) {
//                        Logger.e("转换" + response.toString());
//                        updateUI();
//                    }
//
//                    @Override
//                    protected void onError(JSONObject response) {
//                        voiceTransText.setText(R.string.trans_voice_failed);
//                        failIcon.setVisibility(View.VISIBLE);
//                        updateUI();
//                    }
//
//                    @Override
//                    protected void onTokenOut() {
//
//                    }
//                }
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Logger.e("转换" + jsonObject.toString());
                    }
                }
                , new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Logger.e(volleyError.getMessage());
                voiceTransText.setText(R.string.trans_voice_failed);
                failIcon.setVisibility(View.VISIBLE);
                updateUI();
            }
        });

        BaseApplication.getInstance().getRequestQueue().add(request);
//        callFuture = NIMClient.getService(MsgService.class).transVoiceToText(voiceUrl, path, attachment.getDuration());
//        callFuture.setCallback(new RequestCallback<String>() {
//            @Override
//            public void onSuccess(String param) {
//                voiceTransText.setText(param);
//
//            }
//
//            @Override
//            public void onFailed(int code) {
//                Logger.e(TAG, "voice to text failed, code=" + code);
//
//            }
//
//            @Override
//            public void onException(Throwable exception) {
//                Logger.e(TAG, "voice to text throw exception, e=" + exception.getMessage());
//                voiceTransText.setText("参数错误");
//                failIcon.setVisibility(View.VISIBLE);
//                updateUI();
//            }
//        });
        show();
    }

    private void refreshStartUI() {
        failIcon.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.VISIBLE);
        refreshingIndicator.setVisibility(View.VISIBLE);
    }

    private void updateUI() {
        refreshingIndicator.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
    }
}
