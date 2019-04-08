package com.example.demo.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;


import com.example.demo.listener.PictureViewListener;
import com.example.demo.utils.HTMLParseUtil;

/**
 * 加载HTML文档数据和Base64编码,不规则图片处理
 *
 * @author: Bob
 * @date :2019/3/27 9:22
 */
public class LoadHtmlWebView extends WebView {

    private static final String MIME_TYPE = "text/html; charset=UTF-8";
    private static final String ENCODE = "charset=UTF-8";

    public LoadHtmlWebView(Context context) {
        this(context, null);
    }

    public LoadHtmlWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadHtmlWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化WebView配置
     */
    private void init() {
        WebSettings webSettings = getSettings();
        //允许注入js脚本
        webSettings.setJavaScriptEnabled(true);
        //允许webView对文件的操作
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
    }

    /**
     * 设置HTML数据和注入图片点击事件
     *
     * @param context 上下文
     * @param html    HTML数据
     */
    public void setHTMLData(Context context, String html) {
        this.addJavascriptInterface(new PictureViewListener(context), "imageListener");
        loadDataWithBaseURL(HTMLParseUtil.LOCAL_URL + HTMLParseUtil.IMAGE_FILE, html, MIME_TYPE, ENCODE, null);
        setWebViewClient(new WebClient());
    }

}
