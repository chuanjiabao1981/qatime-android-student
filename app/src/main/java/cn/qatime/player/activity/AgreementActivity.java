package cn.qatime.player.activity;

import android.os.Bundle;
import android.webkit.WebView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author lungtify
 * @Time 2017/6/20 15:48
 * @Describe
 */

public class AgreementActivity extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        setTitles("用户协议");
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/agreement.html");
    }
}
