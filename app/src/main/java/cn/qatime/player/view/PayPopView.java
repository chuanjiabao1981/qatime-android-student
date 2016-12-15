package cn.qatime.player.view;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import cn.qatime.player.R;


/**
 * @author Tianhaoranly
 * @date 2016/12/15 13:07
 * @Description:
 */
public class PayPopView {
    private String title;
    private String price;
    private PayEditText payEditText;
    private Keyboard keyboard;
    private Window window;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "<<"
    };
    private View view;

    public PayPopView(String title, String price, Window window) {
        this.title = title;
        this.price = price;
        this.window = window;
        init();
    }

    private void init() {
        view = View.inflate(window.getContext(), R.layout.dialog_pay_password, null);
        payEditText = (PayEditText) view.findViewById(R.id.PayEditText_pay);
        keyboard = (Keyboard) view.findViewById(R.id.KeyboardView_pay);
        //设置键盘
        keyboard.setKeyboardKeys(KEY);
        keyboard.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    payEditText.add(value);
                } else if (position == 11) {
                    payEditText.remove();
                }
            }
        });
        /**
         * 当密码输入完成时的回调
         */
        payEditText.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                Toast.makeText(window.getContext(), "您的密码是：" + password, Toast.LENGTH_SHORT).show();
                // TODO: 2016/12/15 验证密码
            }
        });
        payEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboard.setVisibility(keyboard.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//                KeyBoardUtils.openKeybord(editText,PayPSWActivity.this);
            }
        });
    }

    public void showPop() {

        PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setBackgroundDrawable(new ColorDrawable());
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.downDialogstyle);
        pop.showAtLocation(window.getDecorView(), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = 1f;
                window.setAttributes(lp);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
