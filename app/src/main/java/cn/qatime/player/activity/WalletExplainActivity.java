package cn.qatime.player.activity;

import android.os.Bundle;
import android.webkit.WebView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author lungtify
 * @Time 2017/6/26 13:38
 * @Describe
 */

public class WalletExplainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_explain);
        setTitles("说明");
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/wallet_explain.html");
    }
}
