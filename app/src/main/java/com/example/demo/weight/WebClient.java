package com.example.demo.weight;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * HTML加载完成后注入JS用于图片点击事件
 *
 * @author: Bob
 * @date :2019/4/2 11:53
 */
public class WebClient extends WebViewClient {

    String JS_OPEN_IMG_CLICK = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            " var array=new Array(); " +
            " for(var j=0;j<objs.length;j++){ array[j]=objs[j].src; }" +
            "for(var i=0;i<objs.length;i++)  " +
            "{"
            + "    objs[i].onclick=function()  " +
            "    {  "
            + "        window.imageListener.onClickImage(this.src,array);  " +
            "    }  " +
            "}" +
            "})()";

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        view.loadUrl(JS_OPEN_IMG_CLICK);
    }
}
