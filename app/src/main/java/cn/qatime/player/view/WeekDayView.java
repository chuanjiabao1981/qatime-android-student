package cn.qatime.player.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * @author luntify
 * @date 2016/8/26 11:13
 * @Description
 */
public class WeekDayView extends View {
    //上横线颜色
    private int mTopLineColor = Color.parseColor("#eeeeee");
    //下横线颜色
    private int mBottomLineColor = Color.parseColor("#ffffff");
    //周一到周五的颜色
    private int mWeedayColor = Color.parseColor("#58cff9");
    //周六、周日的颜色
    private int mWeekendColor = Color.parseColor("#ff5842");
    //线的宽度
    private int mStrokeWidth = 4;
    private int mWeekSize = 14;
    private Paint paint;
    private DisplayMetrics mDisplayMetrics;
    private String[] weekString = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public WeekDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xffffffff);
        mDisplayMetrics = getResources().getDisplayMetrics();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 40;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //进行画上下线
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mTopLineColor);
        paint.setStrokeWidth(mStrokeWidth);
        canvas.drawLine(0, 0, width, 0, paint);

        //画下横线
        paint.setColor(mBottomLineColor);
        canvas.drawLine(0, height, width, height, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(mWeekSize * mDisplayMetrics.scaledDensity);
        int columnWidth = width / 7;
        for (int i = 0; i < weekString.length; i++) {
            String text = weekString[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
            int startY = (int) (height / 2 - (paint.ascent() + paint.descent()) / 2);
            if (text.contains("周日") || text.contains("周六")) {
                paint.setColor(mWeekendColor);
            } else {
                paint.setColor(mWeedayColor);
            }
            canvas.drawText(text, startX, startY, paint);
        }
    }

    /**
     * 设置顶线的颜色
     *
     * @param mTopLineColor
     */
    public void setmTopLineColor(int mTopLineColor) {
        this.mTopLineColor = mTopLineColor;
    }

    /**
     * 设置底线的颜色
     *
     * @param mBottomLineColor
     */
    public void setmBottomLineColor(int mBottomLineColor) {
        this.mBottomLineColor = mBottomLineColor;
    }

    /**
     * 设置周一-五的颜色
     *
     * @return
     */
    public void setmWeedayColor(int mWeedayColor) {
        this.mWeedayColor = mWeedayColor;
    }

    /**
     * 设置周六、周日的颜色
     *
     * @param mWeekendColor
     */
    public void setmWeekendColor(int mWeekendColor) {
        this.mWeekendColor = mWeekendColor;
    }

    /**
     * 设置边线的宽度
     *
     * @param mStrokeWidth
     */
    public void setmStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }


    /**
     * 设置字体的大小
     *
     * @param mWeekSize
     */
    public void setmWeekSize(int mWeekSize) {
        this.mWeekSize = mWeekSize;
    }


    /**
     * 设置星期的形式
     *
     * @param weekString 默认值	"日","一","二","三","四","五","六"
     */
    public void setWeekString(String[] weekString) {
        this.weekString = weekString;
    }
}
