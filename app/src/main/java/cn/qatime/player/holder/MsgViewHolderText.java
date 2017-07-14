package cn.qatime.player.holder;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.Hashtable;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;
import cn.qatime.player.utils.ExpressionUtil;
import cn.qatime.player.view.GifDrawable;
import libraryextra.utils.DensityUtils;


public class MsgViewHolderText extends MsgViewHolderBase {
    private Hashtable<Integer, GifDrawable> cache = new Hashtable<>();

    public MsgViewHolderText(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_message_text;
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void bindContentView() {
        layoutDirection();

        final TextView bodyTextView = findViewById(R.id.message_item_text_body);
//        bodyTextView.setTextColor(isReceivedMessage() ? Color.BLACK : Color.WHITE);
        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
//        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
        bodyTextView.setText(ExpressionUtil.getExpressionString(
                context, message.getContent(), ExpressionUtil.emoji, cache, new GifDrawable.UpdateListener() {
                    @Override
                    public void update() {
                        bodyTextView.postInvalidateDelayed(100);
                    }
                }));
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
    }

    private void layoutDirection() {
        TextView bodyTextView = findViewById(R.id.message_item_text_body);
        if (isReceivedMessage()) {
            bodyTextView.setBackgroundResource(R.drawable.chatfrom_bg_normal);
            bodyTextView.setPadding(DensityUtils.dip2px(context, 15), DensityUtils.dip2px(context, 8), DensityUtils.dip2px(context, 10), DensityUtils.dip2px(context, 8));
        } else {
            bodyTextView.setBackgroundResource(R.drawable.chatto_bg_normal);
            bodyTextView.setPadding(DensityUtils.dip2px(context, 10), DensityUtils.dip2px(context, 8), DensityUtils.dip2px(context, 15), DensityUtils.dip2px(context, 8));
        }
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    protected String getDisplayText() {
        return message.getContent();
    }
}
