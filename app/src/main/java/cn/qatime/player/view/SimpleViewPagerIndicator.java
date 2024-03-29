package cn.qatime.player.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SimpleViewPagerIndicator extends LinearLayout {

    private static final int COLOR_TEXT_NORMAL = 0xFF999999;
    private static final int COLOR_TEXT_SELECT = 0xFF333333;
    private static final int COLOR_INDICATOR_COLOR = 0xffC4483C;

    private String[] mTitles;
    private int mTabCount = 3;
    private int mIndicatorColor = COLOR_INDICATOR_COLOR;
    private float mTranslationX;
    private Paint mPaint = new Paint();
    private int mTabWidth;
    private OnItemClickListener listener;
    private List<TextView> textViews;
    private int lastPoison = 0;

    public SimpleViewPagerIndicator(Context context) {
        this(context, null);
    }

    public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(mIndicatorColor);
        mPaint.setStrokeWidth(4.0F);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabWidth = w / mTabCount;
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
        mTabCount = titles.length;
        generateTitleView();

    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate(mTranslationX, getHeight() - 2);
        canvas.drawLine(0, 0, mTabWidth, 0, mPaint);
        canvas.restore();
    }

    public void scroll(int position, float offset) {
        /**
         * <pre>
         *  0-1:position=0 ;1-0:postion=0;
         * </pre>
         */
        mTranslationX = getWidth() / mTabCount * (position + offset);
        invalidate();
    }

    public void select(int position) {
        textViews.get(lastPoison).setTextColor(COLOR_TEXT_NORMAL);
        lastPoison = position;
        textViews.get(position).setTextColor(COLOR_TEXT_SELECT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void generateTitleView() {
        if (getChildCount() > 0)
            this.removeAllViews();
        final int count = mTitles.length;
        setWeightSum(count);
        textViews = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final TextView tv = new TextView(getContext());
            LayoutParams lp = new LayoutParams(0,
                    LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(i == 0 ? COLOR_TEXT_SELECT : COLOR_TEXT_NORMAL);
            tv.setText(mTitles[i]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setLayoutParams(lp);
            final int finalI = i;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnClick(finalI);
                    }
                }
            });
            addView(tv);
            textViews.add(tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    public interface OnItemClickListener {
        void OnClick(int position);
    }
}
