package cn.qatime.player.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.neliveplayer.util.string.StringUtil;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;

public abstract class MsgViewHolderThumbBase extends MsgViewHolderBase {

    public MsgViewHolderThumbBase(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    protected ImageView thumbnail;
    protected View progressCover;
    protected TextView progressLabel;

    @Override
    protected void inflateContentView() {
        thumbnail = findViewById(R.id.message_item_thumb_thumbnail);
        progressBar = findViewById(R.id.message_item_thumb_progress_bar); // 覆盖掉
        progressCover = findViewById(R.id.message_item_thumb_progress_cover);
        progressLabel = findViewById(R.id.message_item_thumb_progress_text);
    }

    @Override
    protected void bindContentView() {
        FileAttachment msgAttachment = (FileAttachment) message.getAttachment();
        String path = msgAttachment.getPath();
        String thumbPath = msgAttachment.getThumbPath();
        if (!TextUtils.isEmpty(thumbPath)) {
            loadThumbnailImage(thumbPath);
        } else if (!TextUtils.isEmpty(path)) {
            loadThumbnailImage(thumbFromSourceFile(path));
        } else {
            loadThumbnailImage(null);
            if (message.getAttachStatus() == AttachStatusEnum.transferred
                    || message.getAttachStatus() == AttachStatusEnum.def) {
                downloadAttachment();
            }
        }

        refreshStatus();
    }

    private void refreshStatus() {
        FileAttachment attachment = (FileAttachment) message.getAttachment();
        if (TextUtils.isEmpty(attachment.getPath()) && TextUtils.isEmpty(attachment.getThumbPath())) {
            if (message.getAttachStatus() == AttachStatusEnum.fail || message.getStatus() == MsgStatusEnum.fail) {
                alertButton.setVisibility(View.VISIBLE);
            } else {
                alertButton.setVisibility(View.GONE);
            }
        }

        if (message.getStatus() == MsgStatusEnum.sending || (isReceivedMessage() && message.getAttachStatus() == AttachStatusEnum.transferring)) {
            progressCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressLabel.setVisibility(View.VISIBLE);
            progressLabel.setText(StringUtil.getPercentString(getMsgAdapter().getProgress(message)));
        } else {
            progressCover.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressLabel.setVisibility(View.GONE);
        }
    }

    private void loadThumbnailImage(String thumbPath) {
        setImageSize();
        Glide.with(context).load(thumbPath).crossFade().placeholder(R.mipmap.message_error_image).centerCrop().into(thumbnail);
//        if (path != null) {
//            //thumbnail.loadAsPath(thumbPath, getImageMaxEdge(), getImageMaxEdge(), maskBg());
//            thumbnail.loadAsPath(isOriginal, path, message.getUuid(), getImageMaxEdge(), getImageMaxEdge(), maskBg());
//        }
//        else {
//            thumbnail.loadAsResource(R.drawable.nim_image_default, maskBg());
//        }
    }

    private void setImageSize() {
//        int[] bounds = null;
//        if (thumbPath != null) {
//            bounds = BitmapDecoder.decodeBound(new File(thumbPath));
//        }
//        if (bounds == null) {
//            if (message.getMsgType() == MsgTypeEnum.image) {
//                ImageAttachment attachment = (ImageAttachment) message.getAttachment();
//                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
//            } else if (message.getMsgType() == MsgTypeEnum.video) {
//                VideoAttachment attachment = (VideoAttachment) message.getAttachment();
//                bounds = new int[]{attachment.getWidth(), attachment.getHeight()};
//            }
//        }
//
//        if (bounds != null) {
//            ImageUtil.ImageSize imageSize = ImageUtil.getThumbnailDisplaySize(bounds[0], bounds[1], getImageMaxEdge(), getImageMinEdge());
//            setLayoutParams(imageSize.width, imageSize.height, thumbnail);
//        }
    }

//    private int maskBg() {
//        return R.drawable.nim_message_item_round_bg;
//    }
//
//    public static int getImageMaxEdge() {
//        return (int) (165.0 / 320.0 * ScreenUtil.screenWidth);
//    }
//
//    public static int getImageMinEdge() {
//        return (int) (76.0 / 320.0 * ScreenUtil.screenWidth);
//    }

    protected abstract String thumbFromSourceFile(String path);
}
