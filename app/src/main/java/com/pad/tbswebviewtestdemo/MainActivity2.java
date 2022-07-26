package com.pad.tbswebviewtestdemo;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public class MainActivity2 extends AppCompatActivity {
    WebView webView;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_main2);
        webView=findViewById(R.id.webview);
        webSettings=webView.getSettings();

        //这个对宿主没什么影响，建议声明
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        webSettings.setJavaScriptEnabled(true);// webView支持javascript
        webView.setDrawingCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 不使用缓存，直接用网络加载
//        webView.getSettingsExtension().setShouldTrackVisitedLinks(false);
        webSettings.setDomStorageEnabled(true);

//        webView.loadUrl("https://www.baidu.com");
//        webView.loadUrl("https://debugtbs.qq.com");
        webView.loadUrl("http://10.1.42.38:9001/login");
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
//                fileDelete.DeleteFile(getExternalFilesDir("/VideoCache"));
//                webView.loadUrl(s);
//                return true;
//            }
//        });
    }

    //返回按键事件开始
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                webView.goBack();
                //fileDelete.DeleteFile(getExternalFilesDir("/VideoCache"));
                return true;
            } else {
                //fileDelete.DeleteFile(getExternalFilesDir("/VideoCache"));
                finish();
                return true;
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
    //返回按键事件结束
}