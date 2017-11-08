package cn.qatime.player.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import cn.qatime.player.R;

/**
 * @author luntify
 * @date 2017/11/3 10:33
 * @Description: 友盟分享工具
 */

public class ShareUtil {
    private static ShareUtil instance;
    private final ShareAction shareAction;

    private ShareUtil(final Activity context, final String url, final String title, final String describe, final ShareListener listener) {
        shareAction = new ShareAction(context).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .addButton("umeng_sharebutton_copyurl", "umeng_sharebutton_copyurl", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setPrimaryClip(ClipData.newPlainText("答疑时间", url));
                            Toast.makeText(context, "复制链接成功", Toast.LENGTH_LONG).show();
                        } else {
                            UMWeb web = new UMWeb(url);
                            web.setTitle(title);
                            web.setDescription(describe);
                            web.setThumb(new UMImage(context, R.mipmap.ic_launcher));

                            new ShareAction(context).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(new UMShareListener() {
                                        @Override
                                        public void onStart(SHARE_MEDIA share_media) {
                                        }

                                        @Override
                                        public void onResult(SHARE_MEDIA platform) {
                                            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                                                    && platform != SHARE_MEDIA.EMAIL
                                                    && platform != SHARE_MEDIA.FLICKR
                                                    && platform != SHARE_MEDIA.FOURSQUARE
                                                    && platform != SHARE_MEDIA.TUMBLR
                                                    && platform != SHARE_MEDIA.POCKET
                                                    && platform != SHARE_MEDIA.PINTEREST
                                                    && platform != SHARE_MEDIA.INSTAGRAM
                                                    && platform != SHARE_MEDIA.GOOGLEPLUS
                                                    && platform != SHARE_MEDIA.YNOTE
                                                    && platform != SHARE_MEDIA.EVERNOTE) {
                                                listener.onSuccess(platform);
                                            }
                                        }

                                        @Override
                                        public void onError(SHARE_MEDIA platform, Throwable t) {
                                            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                                                    && platform != SHARE_MEDIA.EMAIL
                                                    && platform != SHARE_MEDIA.FLICKR
                                                    && platform != SHARE_MEDIA.FOURSQUARE
                                                    && platform != SHARE_MEDIA.TUMBLR
                                                    && platform != SHARE_MEDIA.POCKET
                                                    && platform != SHARE_MEDIA.PINTEREST
                                                    && platform != SHARE_MEDIA.INSTAGRAM
                                                    && platform != SHARE_MEDIA.GOOGLEPLUS
                                                    && platform != SHARE_MEDIA.YNOTE
                                                    && platform != SHARE_MEDIA.EVERNOTE) {
//                                                listener.onError(platform);
//                                                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
                                                LogCatHelper.getInstance(null).log(platform.toString());

                                            }
                                        }

                                        @Override
                                        public void onCancel(SHARE_MEDIA platform) {
//                                            listener.onCancel(platform);
                                        }
                                    }).share();
                        }
                    }
                });

    }

    public void open() {
        shareAction.open();
    }

    public static ShareUtil getInstance(Activity context, String url, String title, String describe, ShareListener listener) {
        if (instance == null) {
            synchronized (ShareUtil.class) {
                instance = new ShareUtil(context, url, title, describe, listener);
            }
        }
        return instance;
    }


    public interface ShareListener {
        void onSuccess(SHARE_MEDIA platform);

//        void onError(SHARE_MEDIA platform);
//
//        void onCancel(SHARE_MEDIA platform);
    }
}
