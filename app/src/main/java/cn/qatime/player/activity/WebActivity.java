package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author luntify
 * @date 2017/11/15 15:18
 * @Description: 网页加载页面
 */

public class WebActivity extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(getIntent().getStringExtra("url"));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setTitles(view.getTitle());
            }
        });
    }

    @Override
    public void backClick(View v) {
        if (webView.canGoBack()) {
            webView.goBack();//返回上一页面
            return;
        }
        super.backClick(v);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();//返回上一页面
            return;
        }
        super.onBackPressed();
    }
}
