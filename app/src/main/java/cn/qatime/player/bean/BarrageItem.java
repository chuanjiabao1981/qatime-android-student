package cn.qatime.player.bean;

import android.widget.TextView;

/**
 * 弹幕对象
 */
public class BarrageItem {
    public TextView textView;
    public int textColor;
    public String text;
    public int textSize;
    public int moveSpeed;//移动速度
    public int verticalPos;//垂直方向显示的位置
    public int textMeasuredWidth;//字体显示占据的宽度
}
