package cn.qatime.player.holder;


import android.content.Intent;

import cn.qatime.player.R;
import cn.qatime.player.activity.WatchMessagePictureActivity;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;

public class MsgViewHolderPicture extends MsgViewHolderThumbBase {

    public MsgViewHolderPicture(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_message_picture;
    }

    @Override
    protected void onItemClick() {
//        WatchMessagePictureActivity.start(context, message);
        Intent intent = new Intent(context, WatchMessagePictureActivity.class);
        intent.putExtra("message", message);
        context.startActivity(intent);
    }

    @Override
    protected String thumbFromSourceFile(String path) {
        return path;
    }
}
