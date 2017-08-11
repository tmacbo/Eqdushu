package com.tmacbo.eqdushu.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.tmacbo.eqdushu.R;
import com.tmacbo.eqdushu.common.view.WebView;

/**
 * @Author tmacbo
 * @Since on 2017/8/11  11:39
 * @mail tang_bo@hotmail.com
 * @Description TODO
 */

public class WebActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * 主视图
     */
    private WebView mWebView;
    private String loadUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mWebView = (WebView) findViewById(R.id.webView);

        loadUrl = "http://book.douban.com/isbn/" + getIntent().getStringExtra("BookNum") + "/";

        mWebView.loadUrl(loadUrl);
    }
}
