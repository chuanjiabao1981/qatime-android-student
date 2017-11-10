package cn.qatime.player.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.qatime.player.R;
import libraryextra.utils.DateUtils;
import libraryextra.utils.DensityUtils;

/**
 * @author luntify
 * @date 2016/8/26 11:06
 * @Description
 */
public class MonthDateView extends View {
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private final ThreadLocal<Paint> mPaint = new ThreadLocal<>();
    private int mPreColor = Color.parseColor("#999999");
    private int mAfterColor = Color.parseColor("#333333");
    private int mSelectBGColor = Color.parseColor("#ff5842");
    private int mCurrentBGColor = Color.parseColor("#999999");
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize;
    private final int mDaySize = 13;
    private TextView tv_date, tv_week;
    private int weekRow;
    private int[][] daysString;
    private DateClick dateClick;
    private List<Integer> daysHasThingList = new ArrayList<>();
    private boolean pointerMode;//是否多点触摸

    private OnCalendarPageChangeListener onCalendarPageChangeListener;

    public OnCalendarPageChangeListener getOnCalendarPageChangeListener() {
        return onCalendarPageChangeListener;
    }

    public void setOnCalendarPageChangeListener(OnCalendarPageChangeListener onCalendarPageChangeListener) {
        this.onCalendarPageChangeListener = onCalendarPageChangeListener;
    }

    public interface OnCalendarPageChangeListener {
        void onPageChange(int type);
    }

    public MonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(0xffffffff);
        Calendar calendar = Calendar.getInstance();
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿

        mPaint.set(paint);
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        daysString = new int[6][7];
        String dayString;
        int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
//        Logger.e("DateView", "DateView:" + mSelMonth+"月1号周" + weekNumber);
        for (int day = 0; day < mMonthDays; day++) {
            dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;

            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.get().measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.get().ascent() + mPaint.get().descent()) / 2);

            int startRecX = mColumnSize * column;
            int startRecY = mRowSize * row;

            if (dayString.equals(mCurrDay + "") && mCurrDay != mSelDay && mCurrMonth == mSelMonth && mCurrYear == mSelYear) {
                //正常月，选中其他日期，则今日为红色
                mPaint.get().reset();
                mPaint.get().setAntiAlias(true);//抗锯齿
                mPaint.get().setColor(mCurrentBGColor);
                mPaint.get().setStyle(Paint.Style.STROKE);
                mPaint.get().setStrokeWidth(2);
//                canvas.drawRoundRect(startRecX + mColumnSize / 2, startRecY + mRowSize / 2, mRowSize / 2, mPaint.get());
                canvas.drawRect(startRecX, startRecY, startRecX + mColumnSize, startRecY + mRowSize, mPaint.get());
            }

            if (dayString.equals(mSelDay + "")) {//选中的日期--每月都会有
                mPaint.get().reset();
                mPaint.get().setAntiAlias(true);//抗锯齿
                mPaint.get().setColor(mSelectBGColor);
                mPaint.get().setStyle(Paint.Style.STROKE);
                mPaint.get().setStrokeWidth(2);
                canvas.drawRect(startRecX, startRecY, startRecX + mColumnSize, startRecY + mRowSize, mPaint.get());
                weekRow = row + 1;
            }

            //绘制事务圆形标志----需要提醒的日期
            drawCircle(column, row, day + 1, canvas);


            mPaint.get().reset();
            mPaint.get().setAntiAlias(true);//抗锯齿

            if (mSelYear < mCurrYear || (mSelYear <= mCurrYear && mSelMonth < mCurrMonth) || (mSelYear <= mCurrYear && mSelMonth <= mCurrMonth && Integer.valueOf(dayString) < mCurrDay)) {
                mPaint.get().setColor(mPreColor);
            } else {
                mPaint.get().setColor(mAfterColor);
            }

            if (dayString.equals(String.valueOf(mSelDay))) {
                mPaint.get().setColor(mSelectBGColor);
            }
            mPaint.get().setTextSize(DensityUtils.dp2px(getContext(), mDaySize));
            mPaint.get().setStyle(Paint.Style.FILL);
            mPaint.get().setStrokeWidth(1);
            canvas.drawText(dayString, startX, startY, mPaint.get());

            if (tv_date != null) {
                tv_date.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
            }

//            if (tv_week != null) {
//                tv_week.setText("第" + weekRow + "周");
//            }
        }
    }

    private void drawCircle(int column, int row, int day, Canvas canvas) {
        if (daysHasThingList != null && daysHasThingList.size() > 0) {
            if (!daysHasThingList.contains(day)) return;
            float circleX = (float) (mColumnSize * column + mColumnSize * 0.7);
            float circleY = (float) (mRowSize * row + mRowSize * 0.35);
            mPaint.get().reset();
            mPaint.get().setAntiAlias(true);//抗锯齿
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.pen), circleX, circleY, mPaint.get());
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                pointerMode = true;//多点触摸
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (pointerMode) {//多点触摸不处理
                    pointerMode = false;
                    break;
                }
                int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledPagingTouchSlop();
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
                    performClick();
                    doClickAction((upX + downX) / 2, (upY + downY) / 2);
                } else if (downX - upX > scaledTouchSlop) {
                    onRightClick();
                } else if (upX - downX > scaledTouchSlop) {
                    onLeftClick();
                }
                break;
        }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     *
     * @param year
     * @param month
     */
    private void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    /**
     * 执行点击事件
     *
     * @param x
     * @param y
     */
    private void doClickAction(int x, int y) {
        int row = y / mRowSize;
        int column = x / mColumnSize;
        if (daysString[row][column] != 0) {
            setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
            invalidate();
            //执行activity发送过来的点击处理事件
            if (dateClick != null) {
                dateClick.onClickOnDate();
            }
        }
    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 0) {//若果是1月份，则变成12月份
            year = mSelYear - 1;
            month = 11;
        } else if (DateUtils.getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month - 1;
            day = DateUtils.getMonthDays(year, month);
        } else {
            month = month - 1;
        }
        setSelectYearMonth(year, month, day);
        if (onCalendarPageChangeListener != null) {
            onCalendarPageChangeListener.onPageChange(0);
        }
        daysHasThingList.clear();
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else if (DateUtils.getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = DateUtils.getMonthDays(year, month);
        } else {
            month = month + 1;
        }
        setSelectYearMonth(year, month, day);
        daysHasThingList.clear();
        if (onCalendarPageChangeListener != null) {
            onCalendarPageChangeListener.onPageChange(1);
        }
        invalidate();
    }

    /**
     * 获取选择的年份
     *
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }

    /**
     * 获取选择的月份
     *
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }

    /**
     * 获取选择的日期
     */
    public int getmSelDay() {
        return this.mSelDay;
    }


    /**
     * 设置显示当前日期的控件
     *
     * @param tv_date 显示日期
     * @param tv_week 显示周
     */
    public void setTextView(TextView tv_date, TextView tv_week) {
        this.tv_date = tv_date;
        this.tv_week = tv_week;
        invalidate();
    }

    /**
     * 设置事务天数
     *
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList.clear();
        this.daysHasThingList.addAll(daysHasThingList);
        postInvalidate();
    }


    /**
     * 设置日期的点击回调事件
     *
     * @author shiwei.deng
     */
    public interface DateClick {
        public void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     *
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView() {
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        invalidate();
    }
}
