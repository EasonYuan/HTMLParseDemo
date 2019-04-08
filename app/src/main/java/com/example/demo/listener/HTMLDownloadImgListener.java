package com.example.demo.listener;


/**
 * 用于HTML里面下载图片完成回调监听
 *
 * @author: Bob
 * @date :2019/4/2 12:01
 */
public interface HTMLDownloadImgListener {
    /**
     * 返回处理好HTML串
     *
     * @param htmlStr
     */
    void onDownload(String htmlStr);
}
