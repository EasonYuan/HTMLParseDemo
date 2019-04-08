package com.example.demo.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.demo.DemoApplication;
import com.example.demo.listener.HTMLDownloadImgListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * HTML里Base64编码图片和不规则路径图片处理
 *
 * @author: Bob
 * @date :2019/3/15 11:12
 */
public class HTMLParseUtil {
    public static final String IMAGE_FILE = Environment.getExternalStorageDirectory() + "/HTML/Image/";
    public static String LOCAL_URL = "file:///";
    public static String HTTP = "http://";
    public static String HTTPS = "https://";
    private static String IMG_PATH = "https://desk-fd.zol-img.com.cn";
    private static String PNG = "png";
    private static String JPG = "jpg";
    private static String JPEG = "jpeg";
    private static String SRC = "src";
    private static String IMG = "img";
    private static String WIDTH = "width";
    private static String BASE64_ENCODE_IMG = "data:image";

    public static HTMLDownloadImgListener mDownloadListener;
    private static List<String> imgUrlList = new ArrayList<>();

    private HTMLParseUtil() {
    }

    /**
     * 是否有图片
     *
     * @return true is have ,false is null
     */
    private static boolean imgListIsNull() {
        return (null != imgUrlList && imgUrlList.size() > 0) ? true : false;
    }

    @SuppressLint("HandlerLeak") static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String newHTML = (String) msg.obj;
            downloadImage(newHTML);
        }
    };

    /**
     * Base64编码图片转换成Bitmap
     *
     * @param string Base64编码
     * @return Bitmap
     */
    public static byte[] stringToBytes(String string) {
        byte[] bitmapArray = null;
        try {
            bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmapArray;
    }

    /**
     * 判断本地文件是否存在
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean fileIsExists(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            return new File(filePath).exists();
        } else {
            return false;
        }
    }

    /**
     * 保存到图片本SD卡中
     *
     * @param filePath 地图地址和名称
     * @param bytes    图片byte数据
     * @return
     * @throws Exception
     */
    private static boolean saveFileToSD(String filePath, byte[] bytes) throws Exception {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(IMAGE_FILE);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if(mkdirs){

                }
            }

            FileOutputStream output = new FileOutputStream(filePath);
            output.write(bytes);
            output.close();
            return true;
        }
        return false;
    }

    /**
     * Base64编码转成图片保存到本地目录
     *
     * @param base64ImgCode Base64编码
     * @param imgType       保存的图片类型
     * @return 本地图片地址
     */
    public static String saveImgToFile(String base64ImgCode, String imgType) {
        if (TextUtils.isEmpty(base64ImgCode)) {
            Log.e("HTMLParseUtil", "base64编码为空不能转为图片");
            return null;
        }
        if (PNG.equals(imgType)) {
            imgType = PNG;
        } else if (JPG.equals(imgType)) {
            imgType = JPG;
        } else if (JPEG.equals(imgType)) {
            imgType = JPEG;
        }
        byte[] bytes = stringToBytes(base64ImgCode);
        String imagePath = IMAGE_FILE;
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        imagePath = imagePath + new Date().getTime() + "." + imgType;
        FileOutputStream out;
        try {
            out = new FileOutputStream(new File(imagePath));
            out.write(bytes);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    /**
     * HTML中处理不同类型图片加载问题(目前支持图片类型:base64编码,png,jpg,jpeg)
     *
     * @param htmlStr     要处理的HTML原文本
     * @param screenWidth 屏幕宽度,用于处理图片宽度超出屏幕
     * @return 已处理完成的HTML文本
     */
    public static void handleHtmlToString(String htmlStr, int screenWidth, HTMLDownloadImgListener listener) {
        mDownloadListener = listener;
        if (imgListIsNull()) {
            imgUrlList.clear();
        }
        //获取得可编辑的HTML文本对象
        final Document document = Jsoup.parse(htmlStr);
        //获取指定的节点对象数组
        Elements imgList = document.getElementsByTag(IMG);
        for (Element img : imgList) {
            String src = img.attr(SRC);
            if (!TextUtils.isEmpty(src)) {
                //如果是已经处理好的图片地址,就不用处理了
                if (src.contains(LOCAL_URL) || src.contains(HTTP) || src.contains(HTTPS)) {
                    continue;
                    //如果是Base64图片(data:image),保存到本地目录并添加本地头(file:///)
                } else if (src.contains(BASE64_ENCODE_IMG)) {
                    String localUrl = "";
                    if (src.contains(PNG)) {
                        localUrl = LOCAL_URL + saveImgToFile(src, PNG);
                    } else if (src.contains(JPG)) {
                        localUrl = LOCAL_URL + saveImgToFile(src, JPG);
                    } else if (src.contains(JPEG)) {
                        localUrl = LOCAL_URL + saveImgToFile(src, JPEG);
                    }
                    img.attr(SRC, localUrl);
                } else {
                    //如果是不规则图片(/xxx/xxx.png),直接添加HTTP地址和端口号
                    String httpUrl = IMG_PATH + src;
                    img.attr(SRC, httpUrl);
                    //添加处理好的网络图片地址
                    imgUrlList.add(httpUrl);
                }
                //图片的宽度处理,style类型
                String style = img.attr("style");
                if (!TextUtils.isEmpty(style)) {
                    img.attr("style", "width:" + screenWidth + " ;height:auto;");
                } else {
                    //处理图片宽度,直接添加width
                    String width = img.attr(WIDTH);
                    if (!TextUtils.isEmpty(width)) {
                        int w = Integer.parseInt(width);
                        if (w > screenWidth) {
                            img.attr(WIDTH, String.valueOf(screenWidth));
                        }
                    } else {
                        img.attr(WIDTH, String.valueOf(screenWidth));
                    }
                }
            }
        }
        String newHtml = document.toString();
        //为不了影响加载显示,开个线程去把HTTP图片标签替换成本地标签,并下载图片
        if (imgListIsNull()) {
            Message msg = new Message();
            msg.obj = newHtml;
            handler.sendMessage(msg);
        } else {
            //没有图片,直接回调返回HTML串
            if (null != listener) {
                listener.onDownload(newHtml);
            }
        }
    }

    /**
     * 下载HTTP开头的路径图片
     */
    private static void downloadImage(final String newHTML) {
        //有网络且有图片就去下载,并替换HTML里面的图片地址指向本地缓存图片路径
        if (ActivityUtils.isNetWorkConnected(DemoApplication.mContext) && imgListIsNull()) {
            //先加载显示图片后再去下载
            final String imgUrl = imgUrlList.get(0);
            Glide.with(DemoApplication.mContext).load(imgUrl).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
                @Override
                public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                    try {
                        String imgType = "png";
                        if (imgUrl.contains(PNG)) {
                            imgType = PNG;
                        } else if (imgUrl.contains(JPG)) {
                            imgType = JPG;
                        } else if (imgUrl.contains(JPEG)) {
                            imgType = JPEG;
                        }
                        String imgPath = IMAGE_FILE + new Date().getTime() + "." + imgType;
                        boolean isComplete = saveFileToSD(imgPath, bytes);
                        String htmlStr = null;
                        //图片存储完成后匹配处理HTML串中图片地址手指向本地
                        if (isComplete) {
                            if (null != newHTML) {
                                String localUrl = LOCAL_URL + imgPath;
                                htmlStr = replaceUrl(newHTML, imgUrl, localUrl);
                            }
                            imgUrlList.remove(0);
                            downloadImage(htmlStr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            /**
             * 走else的三种情况:
             * 1.没有网络,无法下载图上
             * 2.没有图片,直接返回处理好的HTML串
             * 3.下载完成,返回处理好的HTML串
             * 回调处理HTML串返回
             */
            if (null != mDownloadListener) {
                mDownloadListener.onDownload(newHTML);
            }

        }
    }

    /**
     * 查找整个HTML标签把网络路径替换成本地路径
     *
     * @param newHtml  整个HTML标签数据
     * @param httpUrl  网络路径
     * @param localUrl 本地路径
     */
    private static String replaceUrl(String newHtml, String httpUrl, String localUrl) {
        Document doc = Jsoup.parse(newHtml);
        Elements srcList = doc.getElementsByTag(IMG);
        for (Element ele : srcList) {
            String src = ele.attr(SRC);
            if (src.equals(httpUrl)) {
                ele.attr(SRC, localUrl);
                break;
            }
        }
        //经过编辑过的HTML,要转换成string后才能使用
        return doc.toString();
    }

}
