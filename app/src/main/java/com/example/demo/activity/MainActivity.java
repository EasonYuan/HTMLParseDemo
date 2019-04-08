package com.example.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.listener.HTMLDownloadImgListener;
import com.example.demo.utils.HTMLParseUtil;
import com.example.demo.utils.ViewUtil;
import com.example.demo.weight.LoadHtmlWebView;

import java.io.File;

/**
 * @author Bob
 * @date 2019/4/1
 */
public class MainActivity extends Activity {

    String content = "<p>每周例会，推迟到下午，时间另行通知</p>" + "<p>测试</p><p>测试自动换行<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAK4AAAAXCAYA" +
            "AACSyXRTAAAFcElEQVRoge3WX0xTVxwH8PuwuOjb/jyZrNnrsmTLYrJwzRaXLHFmca4FXIyNCRlu11atcYlzD4P1qswHH3Slf9SJkoDQolKwtKDQVSiIQoFWhkjbYS" +
            "/lQgsirQxcZsx3D+3F25Zbuhf+JKfJ56Hn/H6/e076u6eH6g7MgiDWG+" +
            "qufxYEsd5QXSNPQRDrDeUengFBrDdU+8MnIIj1hnINTUNK1X0erJPDYXsIhxyPoXWGUHmPl4wniJVCOR9MIV29ZxIHm0axtyWCHzpncdW/" +
            "gGr/Ao52zWJvyySK7GFY74cz8ghipVCtvgjELneFsccWwk89cyjrn0MrN4+qR3OoHplDW/g5TvX/jWM988i3hVHtCiA9nyBWAnXLOwlBvYdHQWMI" +
            "pb1zKGqbgjXwDOW+OHbZI9jVFIHpQRzW4ByKWqdwrO8f7LTxqL8XgrgGQawEqrl/AoLixiCOumfAOKPQuKKoehjD/rYomLYovm+LgnFGUD0cx0FX" +
            "FAecERTfncf+ukGIayyrnIWMZsHmFG9GPq1EfvnS8+dLNZDJDTifXp8xi+JcUMuVkNECFvmM+LtAA/W1/7EPYlVRDg8Ph4dHRQeHwpscDrmi2N" +
            "00jp1WDmd6pvH5NQ6f1YWwrS6EL66HcKZnGjvqOXzTNA61axpf2XhU/eGHUCeDjl2iSTIpdOm5tVDQGqgtLqjlGqgtafMWPfJyqCuT62FKriO" +
            "vxAWHh4eWSdaz6JHH1KaOSe2DWFMou2cCds8Ejjv+wh7bGL5u4KBqHYc9GIM3Oo+bgRhsSU2BGLyReTQFY2Buj0PewGGnIwr2hg9CnQzJhpGc" +
            "90zAWKKBQicasxiQRytFY2YoaA1UllcxWkaZqGsxII9mofWYoRBydIlTXSuxDq3EiSuuT6xtlN3Dw+7hwTQGkN/wGB9X+fFzB4/lPsdcPPKq" +
            "/dhum4DGMgChTna1yQaUjjGWaLKeoInm45PNmdtJLsQLuVomuYbkiZsyltM+iNW22LjFDUF8WRfAR1eGcaR1bLFB5/99iat/PoF5aAYLL14u" +
            "jqtucdhyZRif3hiDuqZf4gEuqOQ5/J0n/9IPMaLGFBpdrocxvbHlehhFjZjtJVDo+NQTVq6HSvLEXf0fhMixcR19PBx9PEpvDiGvJoD3Lgx" +
            "C1fx4sUEXXrxEeW8EJs8knosat9g+ivcvDmJL7Sh+NPdBqCPFVKJJ3DHF48K9UzIv0bgmqfk66XtutmdpGQ3Udcl8pjZ1bJl9EGsD1TIwgZaBCdR2BPFh5TBk+gEU24Ip1wI" +
            "3F0fXWDxlbF9jEO8aBvBB5TAMdh+EOku6bgBNK5GvF4+bkU+zOJEWe6E0+1WBLnWl1mXMmc/Ts6lxUmNS+cSaR932TkJQVOPFa2d9+M7mX/aO" +
            "u886gtfP+bD9917Ye8cgriPl5IFkAypYFChYnFw2x4wChQEXpeaTL4RUkxfoRbF6dvFKQCskXopf7uS0D2L1Uc7BCARNPRze/m0A22oeoZOLwT0W" +
            "h5uLozv8DN3hZ3BzcbjH4ujkYvik+iHe0nlxqdkHcY2sjCcgo5Uo1FpQZjSCppUoNGbLsaBQYUSF1LzVCFplWXKuQqtZrF2mSjTsYWvu+cTaRrUP" +
            "TUGstt0PmekBqNM92HDKjY1lndhU1oVNZV3YWNaJDafcoH7twTvnB3HW2of0/AymE69ONVVdxvxplRIyhRGV6bFZbGU7EvkNRmzNErfblLmeSvaIdG1hH" +
            "cSaR3U+mka6hu5RKCr7sdngxRs6L94815eg82KzwYcdFR5cbvZl5BHESqHu+WcgpebOCEpueFFc3Ytvq3px3NKHSy2DcPaHJXMIYiVQvaNPQRDrDdUfioEg1p" +
            "v/AEeb5MWJ04WDAAAAAElFTkSuQmCC\" alt=\"\" /><br /><br />效果测试自动换行效果测试自动换行效果测试自动换行效果测试自动换行效果测试自动" +
            "换行效果测试自动换行效果测试自动换行效果测试自动换行效果测试自动换行效果测试自动换行效果测试自动换行效果测试自动换行效果" +
            "测试自动换行效果测试自动换行</p><table style=\"width: 100%; border-width: 1px; border-color: #000000; border-collapse: collapse;\" " +
            "cellspacing=\"0\" cellpadding=\"3\"><tbody><tr><td style=\"width: 50%; border-width: 1px; border-style: solid; border-color: #000000;\">" +
            "cehsi<br /></td><td style=\"width: 50%; border-width: 1px; border-style: solid; border-color: #000000;\">测试<br /></td></tr><tr><td " +
            "style=\"width: 50%; border-width: 1px; border-style: solid; border-color: #000000;\">测试2<br /></td><td style=\"width: 50%; border-width: 1px;" +
            " border-style: solid; border-color: #000000;\">测试2<br />" +
            "</td></tr></tbody></table><p>效果测试自动换行效果测试自动换行效果测试自动换行效果<br /><br /><br />" +
            "<img src=\"/t_s960x600c5/g5/M00/0A/0F/ChMkJljCOqiIC2nQAAtOpt4p2pEAAaoGwOxtVkAC06-976.jpg\" alt=\"\" />" +
            "<br /><br />" + "<img style=\"width: 524px; height: 615.364px;\" src=\"/t_s960x600c5/g5/M00/0A/0F/ChMkJ1jCOQiIK4gzAAwoPJwwQtQAAaoGgDd1KAADChU618.jpg\" alt=\"\" />" +
            "</p>";
    private int screenWidth;
    private LoadHtmlWebView loadHtmlWebView;
    private final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenWidth = ViewUtil.getScreenWidth(this);
        setContentView(R.layout.activity_main);
        loadHtmlWebView = findViewById(R.id.webView);
        //Android6.0以上需要动态申请文件读写等...其他权限
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android
                    .Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            loadData();
        }
    }

    private void loadData() {
        final SharedPreferences preferences = this.getSharedPreferences("content", Context.MODE_PRIVATE);
        final String html = preferences.getString("html", null);
        if (!TextUtils.isEmpty(html)) {
            HTMLParseUtil.handleHtmlToString(html, screenWidth, new HTMLDownloadImgListener() {
                @SuppressLint("CommitPrefEdits")
                @Override
                public void onDownload(String htmlStr) {
                    loadHtmlWebView.setHTMLData(MainActivity.this, htmlStr);
                }
            });
        } else {
            //下载完成图片后再去加载HTML和图片
            /**
             * 缺点:如果HTML里面图片过大过多,第一次下载和缓存本地速度慢
             * 优点:如果缓存完成了,没网络情况下也可以查看图片,缓存完成的情况下,加载速度飞快
             */
            HTMLParseUtil.handleHtmlToString(this.content, screenWidth, new HTMLDownloadImgListener() {
                @SuppressLint("CommitPrefEdits")
                @Override
                public void onDownload(String htmlStr) {
                    loadHtmlWebView.setHTMLData(MainActivity.this, htmlStr);
                    SharedPreferences.Editor edit = preferences.edit();
                    boolean isSave = edit.putString("html", htmlStr).commit();
                    if (isSave) {
                        Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Android6.0以后创建文件夹再去加载,图片才能缓存到指定目录中
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File file = new File(HTMLParseUtil.IMAGE_FILE);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    loadData();
                }
            }
        }
    }
}


