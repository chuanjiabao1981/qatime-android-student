package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.view.FlowLayout;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.StringUtils;

/**
 * @author Tianhaoranly
 * @date 2017/6/9 10:49
 * @Description:
 */
public class SearchActivity extends BaseActivity {
    private FlowLayout flow;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        initView();
    }

    private void initView() {
        edit = ((EditText) findViewById(R.id.edit_search));
        edit.requestFocus();
        findViewById(R.id.right_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edit.getText().toString();
                if (!StringUtils.isNullOrBlanK(s)) {
                    search(s);
                }
            }
        });
        flow = (FlowLayout) findViewById(R.id.flow);
        String[] value = new String[]{"高考", "语文", "初二", "动态电路", "力学", "必修"};
        for (int i = 0; i < value.length; i++) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(0xff666666);
            textView.setTextSize(13);
            textView.setBackgroundResource(R.drawable.text_background_corners_f7);
            textView.setPadding(DensityUtils.dip2px(this, 15), DensityUtils.dip2px(this, 5), DensityUtils.dip2px(this, 15), DensityUtils.dip2px(this, 5));
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = DensityUtils.dip2px(this, 5);
            params.rightMargin = DensityUtils.dip2px(this, 5);
            params.topMargin = DensityUtils.dip2px(this, 3);
            params.bottomMargin = DensityUtils.dip2px(this, 3);
            textView.setLayoutParams(params);

            final String s = value[i];
            textView.setText(s);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search(s);
                }
            });
            flow.addView(textView);
        }

    }

    private void search(String s) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("search_key",s);
        startActivity(intent);
        finish();
    }
}
