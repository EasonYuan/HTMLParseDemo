package com.example.demo.listener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.example.demo.activity.ImageShowActivity;
import com.example.demo.utils.ActivityUtils;
import com.example.demo.utils.HTMLParseUtil;

import java.util.ArrayList;

/**
 * 图片查看监听回调
 *
 * @author: Bob
 * @date :2019/4/2 11:58
 */
public class PictureViewListener {
    private Context mContext;
    private ArrayList<String> imgList = new ArrayList<>();
    private int index = 0;

    public PictureViewListener(Context mContext) {
        this.mContext = mContext;
    }

    @JavascriptInterface
    public void onClickImage(String img, String[] imgs) {
        imgList.clear();
        for (int i = 0; i < imgs.length; i++) {
            String url = imgs[i];
            if (url.equals(img)) {
                index = i;
            }
            if (url.contains(HTMLParseUtil.LOCAL_URL)) {
                String[] splitUrl = url.split(HTMLParseUtil.LOCAL_URL);
                imgList.add(splitUrl[1]);
            } else {
                imgList.add(imgs[i]);
            }
        }
        //去掉文件头(file:///)
//        String localUrl = imgs[index].split(HTMLParseUtil.LOCAL_URL)[1];
//        if (!TextUtils.isEmpty(localUrl)) {
//            //调用系统图片查看器查看图片会变得清晰一些
//            ActivityUtils.openFileBySystem(mContext, localUrl);
//        } else {
        //调用自己写的图片查看器查看图片,放大时图片会模糊
        Intent intent = new Intent(mContext, ImageShowActivity.class);
        intent.putExtra("position", index);
        intent.putExtra("imgList", imgList);
        mContext.startActivity(intent);
//        }
    }
}
