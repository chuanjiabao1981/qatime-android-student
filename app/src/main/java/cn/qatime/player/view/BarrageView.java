package cn.qatime.player.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import cn.qatime.player.utils.AnimationHelper;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.LogUtils;

/**
 * 弹幕父view
 */
public class BarrageView extends RelativeLayout {
    private Context mContext;
    private Handler hd = new Handler();
    private float textSize = 16;

    //记录当前仍在显示状态的弹幕的位置（避免重复）
    private Set<Integer> existMarginValues = new HashSet<>();
    private int validHeightSpace = 0;//父組件高度

    private int linesCount;
    //    private Random random = new Random();
//    private static final long BARRAGE_GAP_MIN_DURATION = 1000;//两个弹幕的最小间隔时间
//    private static final long BARRAGE_GAP_MAX_DURATION = 2000;//两个弹幕的最大间隔时间
//    private Style style = Style.FULL;//視頻展現方式  上部分  下部分  全屏
//    private int maxSpeed = 10000;//速度，ms
//    private int minSpeed = 5000;//速度，ms
////    private int maxSize = 30;//文字大小，dp
////    private int minSize = 15;//文字大小，dp
//
//    private int totalHeight = 0;
//    private int lineHeight = 0;//每一行弹幕的高度
//    private int totalLine = 0;//弹幕的行数
//
    private LinkedList<String> queue = new LinkedList();
    private boolean isRun = false;// 線程是否在輪詢
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hd.postDelayed(this, 100);
            if (getVisibility() == VISIBLE) {
                if (queue.size() > 0) {
                    isRun = true;
                    roll();
                } else {
                    hd.removeCallbacks(runnable);
                    isRun = false;
                }
            }
        }
    };


    private void roll() {
        String text;
        synchronized (this) {
            text = queue.getLast();
        }
        final TextView textView = new TextView(mContext);
        textView.setTextSize(textSize);
        textView.setText(text);
        textView.setTextColor(0xffffffff);
        synchronized (this) {
            queue.remove(text);
        }
        int leftMargin = getRight() - getLeft() - getPaddingLeft();
        //计算本条弹幕的topMargin(随机值，但是与屏幕中已有的不重复)
        int verticalMargin = getRandomTopMargin();
        LogUtils.e("屏幕高"+verticalMargin);
        textView.setTag(verticalMargin);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.topMargin = verticalMargin;

        textView.setLayoutParams(params);
        Animation anim = AnimationHelper.createTranslateAnim(mContext, leftMargin, -ScreenUtils.getScreenWidth(mContext));
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //移除该组件
                removeView(textView);
                //移除占位
                int verticalMargin = (int) textView.getTag();
                existMarginValues.remove(verticalMargin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textView.startAnimation(anim);

        addView(textView);
    }

    private int getRandomTopMargin() {
        //计算用于弹幕显示的空间高度
        if (validHeightSpace == 0) {
            validHeightSpace = ScreenUtils.getScreenHeight(mContext);
        }

        //计算可用的行数
        if (linesCount == 0) {
            linesCount = validHeightSpace / DensityUtils.dp2px(mContext, textSize * 1.5f);
            if (linesCount == 0) {
                throw new RuntimeException("Not enough space to show text.");
            }
        }

        //检查重叠
        while (true) {
            int randomIndex = (int) (Math.random() * linesCount);
            int marginValue = randomIndex * (validHeightSpace / linesCount);

            if (!existMarginValues.contains(marginValue)) {
                existMarginValues.add(marginValue);
                return marginValue;
            }
        }
    }

    public BarrageView(Context context) {
        this(context, null);
    }

    public BarrageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        setDrawingCacheBackgroundColor(Color.TRANSPARENT);
        hd.postDelayed(runnable, 100);
    }

    public void addItem(String text) {
        synchronized (this) {
            queue.addFirst(text);
        }
        if (!isRun) {
            hd.post(runnable);
        }
    }

//    private void generateItem() {
//
//        BarrageItem item = new BarrageItem();
//        item.textView = new TextView(mContext);
//        String tx = queue.getLast();
//        item.textView.setText(tx);
//        queue.remove(tx);
//        item.textView.setTextSize(14);
//        item.textView.setTextColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
//        item.textMeasuredWidth = (int) getTextWidth(item, tx);
//        item.moveSpeed = (int) (minSpeed + (maxSpeed - minSpeed) * Math.random());
//
//        if (style == Style.TOP) {
//            totalHeight = getMeasuredHeight() / 2;
//            lineHeight = getLineHeight();
//            totalLine = totalHeight / lineHeight;
//            item.verticalPos = random.nextInt(totalLine) * lineHeight;
//        } else if (style == Style.BOTTOM) {
//            if (totalLine == 0) {
//                totalHeight = getMeasuredHeight() / 2;
//                lineHeight = getLineHeight();
//                totalLine = totalHeight / lineHeight;
//                item.verticalPos = getMeasuredHeight() / 2 + random.nextInt(totalLine) * lineHeight;
//            }
//        } else {
//            if (totalLine == 0) {
//                totalHeight = ScreenUtils.getScreenHeight(getContext());
//                lineHeight = getLineHeight();
//                totalLine = totalHeight / lineHeight;
//                item.verticalPos = random.nextInt(totalLine) * lineHeight;
//                LogUtils.e("屏幕高"+totalHeight);
//            }
//        }
//        showBarrageItem(item);
//    }
//
//    private void showBarrageItem(final BarrageItem item) {
//
//        int leftMargin = this.getRight() - this.getLeft() - this.getPaddingLeft();
//
//        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        params.topMargin = item.verticalPos;
//        this.addView(item.textView, params);
//        Animation anim = generateTranslateAnim(item, leftMargin);
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                item.textView.clearAnimation();
//                BarrageView.this.removeView(item.textView);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        item.textView.startAnimation(anim);
//    }
//
//    public void setData(String data) {
//        queue.addFirst(data);
//        hd.post(runnable);
//    }
//
//    private TranslateAnimation generateTranslateAnim(BarrageItem item, int leftMargin) {
//        TranslateAnimation anim = new TranslateAnimation(leftMargin, -item.textMeasuredWidth, 0, 0);
//        anim.setDuration(item.moveSpeed);
//        anim.setInterpolator(new AccelerateDecelerateInterpolator());
//        anim.setFillAfter(true);
//        return anim;
//    }
//
//    /**
//     * 计算TextView中字符串的长度
//     *
//     * @param text 要计算的字符串
//     * @return TextView中字符串的长度
//     */
//    public float getTextWidth(BarrageItem item, String text) {
//        Rect bounds = new Rect();
//        TextPaint paint;
//        paint = item.textView.getPaint();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//        return bounds.width();
//    }
//
//    /**
//     * 获得每一行弹幕的最大高度
//     *
//     * @return
//     */
//    private int getLineHeight() {
//        BarrageItem item = new BarrageItem();
//        item.textView = new TextView(mContext);
//        item.textView.setText("0");
//        item.textView.setTextSize(14);
//
//        Rect bounds = new Rect();
//        TextPaint paint;
//        paint = item.textView.getPaint();
//        paint.getTextBounds("0", 0, 1, bounds);
//        return bounds.height();
//    }
//
//    //    class BarrageHandler extends Handler {
////        @Override
////        public void handleMessage(Message msg) {
////            super.handleMessage(msg);
////            generateItem();
////            //每个弹幕产生的间隔时间随机
////            int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math.random());
////            this.sendEmptyMessageDelayed(0, duration);
////        }
////    }
//    public enum Style {
//        TOP(0), BOTTOM(1), FULL(2);
//
//        Style(int i) {
//
//        }
//    }
}
