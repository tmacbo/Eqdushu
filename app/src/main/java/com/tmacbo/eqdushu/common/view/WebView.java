package com.tmacbo.eqdushu.common.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.tmacbo.eqdushu.common.CommonConfig;


/**
 * @author Quan
 * @version 1.0
 * @date 2015-06-18 10:02
 * @since 云融自有的WebView
 * --------------------------------------------------------------------
 */
public class WebView extends android.webkit.WebView {

    /**
     * 地址
     */
    private String url;
    /**
     * 载入中
     */
    private boolean isLoading = false;
    /**
     * 载入完成
     */
    private boolean isLoadSuccess = false;
    /**
     * 载入条
     */
    private Progressbar progressbar;
    /**
     * 标题
     */
    private String title;

    private onWebViewClient mOnWebViewClient;

    public WebView(Context context) {
        super(context);
        init();
    }

    public WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * 初始化
     */
    private void init() {
        super.setWebViewClient(webViewClient);
        super.setWebChromeClient(chromeClient);

        // 修改ua使得web端正确判断
        String ua = getSettings().getUserAgentString();
        getSettings().setUserAgentString(ua + "; Symdata/" + CommonConfig.getVERSION());

        //基本设置
        getSettings().setDomStorageEnabled(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //getSettings().setBuiltInZoomControls(true);
        getSettings().setSupportZoom(true);
        //支持空格符号
        getSettings().setUseWideViewPort(true);
        //适应压缩宽度以适应浏览
        //getSettings().setLoadWithOverviewMode(true);

        // 通过如下设置，来启动外部浏览器打开下载链接
        setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                openUrl(url);
            }
        });

        //初始化载入条
        progressbar = new Progressbar(getContext());
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10, 0, 0));
        addView(progressbar);

    }


    /**
     * 设置进度条颜色
     *
     * @param color
     */
    public void setProgressColor(int color) {
        progressbar.setColor(color);
    }

    /**
     * 启动外部浏览器打开一个链接
     *
     * @param url 需要打开的URL地址
     */
    public void openUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 载入view视图
     *
     * @param html
     */
    public void openView(String html) {
        String encoding = "UTF-8";
        getSettings().setDefaultTextEncodingName(encoding);
//        LogUtils.debug(this, "webView:" + html);
        loadData(html, "text/html", encoding);
    }

    @Override
    public void stopLoading() {
        super.stopLoading();
        isLoading = false;
    }

    @Override
    public void loadUrl(String url) {
//        NetworkUtils.synCookies(getContext(), url);
//        LogUtils.debug(this, url);
        super.loadUrl(url);
    }

    WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onReceivedTitle(android.webkit.WebView view, String title) {
            super.onReceivedTitle(view, title);
            WebView.this.title = title;
            if (mOnWebViewClient != null) mOnWebViewClient.onReceivedTitle(title);
        }

        @Override
        public boolean onJsAlert(android.webkit.WebView view, String url, String message, JsResult result) {
            if (mOnWebViewClient != null)
                return mOnWebViewClient.onJsAlert(url, message, result);
            else {
                return super.onJsAlert(view, url, message, result);
            }
        }

        @Override
        public boolean onJsConfirm(android.webkit.WebView view, String url, String message, JsResult result) {
            if (mOnWebViewClient != null)
                return mOnWebViewClient.onJsConfirm(url, message, result);
            else {
                return super.onJsConfirm(view, url, message, result);
            }
        }

        @Override
        public void onProgressChanged(android.webkit.WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
        }
    };

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            isLoading = false;
            isLoadSuccess = true;
            super.onPageFinished(view, url);
            if (mOnWebViewClient != null)
                mOnWebViewClient.onPageFinished(url);
        }

        @Override
        public void onReceivedError(android.webkit.WebView view, int errorCode, String description,
                                    String failingUrl) {
        }

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            if (mOnWebViewClient != null)
                return mOnWebViewClient.shouldOverrideUrlLoading(url);
                // doc上的注释为:
                // True if the host application wants to handle the key event itself,
                // otherwise return false
                // (如果程序需要处理,那就返回true,如果不处理,那就返回false)
                // 我们这个地方返回false, 并不处理它,把它交给webView自己处理.
            else return false;
        }

        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
            WebView.this.url = url;
            isLoading = true;
            if (mOnWebViewClient != null)
                mOnWebViewClient.onPageStarted(url, favicon);
        }

    };

    /**
     * webview 中的进度条
     */
    private class Progressbar extends View {

        Paint mPaint;//画笔
        boolean isinit;//是否初始化
        int per;//每分长度
        int progress;//当前长度（0-100）

        public Progressbar(Context context) {
            super(context);
            mPaint = new Paint();
            isinit = false;
//            mPaint.setColor(Color.parseColor("#F8E71C"));
            mPaint.setColor(0xFFFF5B50);
            per = 1;
            progress = 1;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            init();
            canvas.drawLine(0, 0, per * progress, 0, mPaint);
        }

        public void setProgress(int progress) {
            this.progress = progress;
            postInvalidate();
        }

        public void setColor(int color) {
            mPaint.setColor(color);
        }

        /**
         * 初始化数据
         */
        public void init() {
            if (isinit) return;
            mPaint.setStrokeWidth(getHeight());
            per = getWidth() / 100;
            isinit = true;
        }
    }


    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isLoadSuccess() {
        return isLoadSuccess;
    }

    public void setIsLoadSuccess(boolean isLoadSuccess) {
        this.isLoadSuccess = isLoadSuccess;
    }

    public interface onWebViewClient {
        void onReceivedTitle(String title);

        boolean shouldOverrideUrlLoading(String url);

        void onPageFinished(String url);

        void onPageStarted(String url, Bitmap favicon);

        boolean onJsAlert(String url, String message, final JsResult result);

        boolean onJsConfirm(String url, String message, JsResult result);
    }

    public onWebViewClient getOnWebViewClient() {
        return mOnWebViewClient;
    }

    public void setOnWebViewClient(onWebViewClient onWebViewClient) {
        mOnWebViewClient = onWebViewClient;
    }
}
