package cn.qatime.player.qrcore.core;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.qrcore.camera.CameraManager;
import cn.qatime.player.qrcore.executor.ResultHandler;
import libraryextra.utils.ScreenUtils;

public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {
    private RequestQueue mQueue;
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    //    private Result lastResult;
    private boolean hasSurface;
    //    private IntentSource source;
    private Collection<BarcodeFormat> decodeFormats;
    //    private String characterSet;
    private InactivityTimer inactivityTimer;
    //    private PopupWindow pop;
    // private Button from_gallery;
    private final int from_photo = 010;
    static final int PARSE_BARCODE_SUC = 3035;
    static final int PARSE_BARCODE_FAIL = 3036;
    String photoPath;
    ProgressDialog mProgress;
//    CustomProgressDialog progressDialog;
//    private int type;
//    private Dialog dialog;
//    View view;
//    private TextView name, className, parentName, time, school_name, result;
//    private ImageView icon;

    // Dialog dialog;


    Handler barHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PARSE_BARCODE_SUC:
                    // viewfinderView.setRun(false);
                    // 选择图片
                    break;
                case PARSE_BARCODE_FAIL:
                    // showDialog((String) msg.obj);
                    if (mProgress != null && mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                    new AlertDialog.Builder(CaptureActivity.this)
                            .setTitle("提示")
                            .setMessage("扫描失败！")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                    break;
            }
            super.handleMessage(msg);
        }

    };
    private ImageView flashLamp;

    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mQueue = Volley.newRequestQueue(this);
        // Window window = getWindow();
        // window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);
        setTitles("扫描二维码");
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        cameraManager = new CameraManager(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        flashLamp = (ImageView) findViewById(R.id.flash_lamp);
        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) flashLamp.getLayoutParams();
        param.topMargin = ScreenUtils.getScreenWidth(this) * 3 / 8 + 30;
        flashLamp.setLayoutParams(param);
        flashLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraManager.isTorch()) {
                    cameraManager.setTorch(false);
                } else
                    cameraManager.setTorch(true);
            }
        });
//		dialog = new Dialog(this, R.style.MyDialogStyle);
//		dialog.setCanceledOnTouchOutside(true);
//		View view = View.inflate(this, R.layout.pop_qrdialog, null);
//		name = (TextView) view.findViewById(R.id.name);
//		className = (TextView) view.findViewById(R.id.className);
//		parentName = (TextView) view.findViewById(R.id.parentName);
//		time = (TextView) view.findViewById(R.id.time);
//		dialog.setContentView(view);
    }

    public String parsLocalPic(String path) {
        String parseOk = null;
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap bitmap;
        options.inJustDecodeBounds = false; // 获取新的大小
        // 缩放比
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "   " + h);
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader2 = new QRCodeReader();
        Result result;
        try {
            result = reader2.decode(bitmap1, hints);
            Logger.e("steven", "result:" + result);
            parseOk = result.getText();

        } catch (Exception e) {
            parseOk = null;
        }
        return parseOk;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("steven", "data.getData()" + data);
        if (data != null) {
            mProgress = new ProgressDialog(CaptureActivity.this);
            mProgress.setMessage("正在扫描...");
            mProgress.setCancelable(false);
            mProgress.show();
            if (requestCode == from_photo) {
                if (resultCode == RESULT_OK) {
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            String result = parsLocalPic(photoPath);
                            if (result != null) {
                                Message m = Message.obtain();
                                m.what = PARSE_BARCODE_SUC;
                                m.obj = result;
                                barHandler.sendMessage(m);
                            } else {
                                Message m = Message.obtain();
                                m.what = PARSE_BARCODE_FAIL;
                                m.obj = "扫描失败！";
                                barHandler.sendMessage(m);
                            }
                            Looper.loop();
                        }
                    }).start();
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = null;
        resetStatusView();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        inactivityTimer.onResume();
        decodeFormats = null;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        if (mProgress != null) {
            mProgress.dismiss();
        }
        super.onDestroy();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                cameraManager.setTorch(false);
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                cameraManager.setTorch(true);
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    // 这里初始化界面，调用初始化相机
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Logger.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private static ParsedResult parseResult(Result rawResult) {
        return ResultParser.parseResult(rawResult);
    }

    // 解析二维码
    public void handleDecode(Result rawResult, Bitmap barcode) {
        inactivityTimer.onActivity();

        ResultHandler resultHandler = new ResultHandler(parseResult(rawResult));

        if (barcode == null) {
            Logger.e("steven", "rawResult.getBarcodeFormat().toString():" + rawResult.getBarcodeFormat().toString());
            Logger.e("steven", "resultHandler.getType().toString():" + resultHandler.getType().toString());
            Logger.e("steven", "resultHandler.getDisplayContents():" + resultHandler.getDisplayContents());
        } else {
            showDialog();
            initData(resultHandler.getDisplayContents().toString());
            restartPreviewAfterDelay(3000L);
        }
    }

    /**
     * 扫描完成后请求签到
     */
    // childId Int true 孩子Id
    // parentId String true 家长Id
    // unitId String true 学校Id
    // operatorId String true 发送者id，即当前登录者uid
    // type Int true 1入校，2离校3 请假
    // token String true 令牌
    public void initData(String qr) {
        Logger.e("qr" + qr);
//        try {
//            JSONObject json = new JSONObject(qr);
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                    UrlUtils.getHttpsUrl(
//                            "/attendance/qr_attendance",
//                            new String[]{"parentId", "operatorId", "token",
//                                    "childId", "unitId", "type"},
//                            new String[]{json.getString("parentsId"),
//                                    MainActivity.contextUser.getUid(),
//                                    MainActivity.contextUser.getToken(),
//                                    json.getString("childId"),
//                                    MainActivity.contextUser.getUnitId(),
//                                    String.valueOf(type)}), null,
//                    new VolleyListerner(this) {
//                        @Override
//                        public void onSucess(JSONObject response) {
//                            setValues(response);
//                        }
//
//                        @Override
//                        public void onRet_0(JSONObject response) {
//                            super.onRet_0(response);
//                            errorValues();
//                        }
//                    }, new VolleyErrorListoner(this) {
//                @Override
//                public void onError(VolleyError error) {
//                    super.onError(error);
//                    errorValues();
//                }
//            });
//            mQueue.add(jsonObjectRequest);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    protected void showDialog() {
//        dialog = new Dialog(this, R.style.MyDialogStyle);
//        dialog.setCanceledOnTouchOutside(true);
//        view = View.inflate(this, R.layout.pop_qrdialog, null);
//        school_name = (TextView) view.findViewById(R.id.textView1);
//        school_name.setText(MainActivity.contextUser.getSchoolName());
//        name = (TextView) view.findViewById(R.id.name);
//        className = (TextView) view.findViewById(R.id.className);
//        parentName = (TextView) view.findViewById(R.id.parentName);
//        time = (TextView) view.findViewById(R.id.time);
//        result = (TextView) view.findViewById(R.id.textView2);
//        icon = (ImageView) view.findViewById(R.id.imageView_icon);
//        dialog.setContentView(view);
//        dialog.show();
    }

//    public void setValues(JSONObject json) {
//        try {
//            name.setText(json.getString("childName"));
//            className.setText(MainActivity.contextUser.getClassName());
//            parentName.setText(json.getString("parentName"));
//            time.setText(json.getString("time"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void errorValues() {
//        view.findViewById(R.id.layout_name).setVisibility(View.GONE);
//        view.findViewById(R.id.layout_class).setVisibility(View.GONE);
//        view.findViewById(R.id.layout_parent).setVisibility(View.GONE);
//        view.findViewById(R.id.layout_time).setVisibility(View.GONE);
//        icon.setImageResource(R.drawable.waring_icon);
//        result.setText("扫描失败");
//        result.setTextColor(0xfffe6669);
//    }

    // 初始化照相机，CaptureActivityHandler解码
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Logger.e(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, null, cameraManager);
            }
        } catch (IOException ioe) {
            Logger.e(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Logger.e(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.confirm, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
//        statusView.setText(R.string.msg_default_status);
//        statusView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

}
