package com.pad.tbswebviewtestdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WebView mView;
    private boolean isInitTbs;

    private static final int REQUEST_CODE_CONTACT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_open_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FileUtils.copyAssets(getApplicationContext(), "HowToLoadX5Core.doc", FileUtils.getTBSFileDir(getApplicationContext()).getPath() + "/HowToLoadX5Core.doc");
                boolean canLoadX5 = QbSdk.canLoadX5(getApplicationContext());
                Log.e(TAG, "canLoadX5: " + canLoadX5);
                if (canLoadX5) {
//                    startActivity(new Intent(MainActivity.this, FileDisplayActivity.class));
                    startActivity(new Intent(MainActivity.this,MainActivity2.class));
                }
            }
        });
        Log.e(TAG,"1");
        isInitTbs = QbSdk.canLoadX5(getApplicationContext());
        if (!isInitTbs || QbSdk.getTbsVersion(getApplicationContext()) < 46007) {
            Log.e(TAG,"2");
            FileUtils.copyAssets(getApplicationContext(), "046007_x5.tbs.apk", FileUtils.getTBSFileDir(getApplicationContext()).getPath() + "/046007_x5.tbs.apk");
            Log.e(TAG,"3");
        }

//        WebViewUtil.hookWebView();
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
//        final WebView view = initWebView();

        HashMap<String, Object> map = new HashMap<>(2);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);

        Log.e(TAG,"4");

        boolean canLoadX5 = QbSdk.canLoadX5(getApplicationContext());
        Log.e(TAG, "canLoadX5: " + canLoadX5+"|TbsVersion:"+QbSdk.getTbsVersion(getApplicationContext()));
        if (canLoadX5) {
//            view.loadUrl("http://10.0.1.40:4201/");
            return;
        }
        QbSdk.reset(getApplicationContext());
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {

            }

            @Override
            public void onInstallFinish(int i) {
                Log.e(TAG, "onInstallFinish: " + i);
                int tbsVersion = QbSdk.getTbsVersion(getApplicationContext());
                Log.e(TAG, "tbsVersion: " + tbsVersion);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LinearLayout frameLayout = findViewById(R.id.root);
//                        frameLayout.removeView(view);
//                        WebView view = initWebView();
//                        view.loadUrl("http://10.0.1.40:4201/");
//                    }
//                });
            }

            @Override
            public void onDownloadProgress(int i) {

            }
        });
//        QbSdk.installLocalTbsCore(getApplicationContext(), 45947, this.getExternalFilesDir("x5").getPath() + "/x5.tbs.apk");
        Log.e(TAG,"5");
        QbSdk.reset(getApplicationContext());
        QbSdk.installLocalTbsCore(getApplicationContext(), 46007, FileUtils.getTBSFileDir(getApplicationContext()).getPath() + "/046007_x5.tbs.apk");
        //动态下载内核安装
//        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
//            @Override
//            public void onCoreInitFinished() {
//                Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT);
//            }
//
//            @Override
//            public void onViewInitFinished(boolean b) {
//
//            }
//        });
        Log.e(TAG,"6");

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //验证是否许可权限
        for (String str : permissions) {
            if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissions(permissions, REQUEST_CODE_CONTACT);
                return;
            }
        }
    }

    public void debug(View view) {
        mView.loadUrl("http://debugtbs.qq.com");
        mView.loadUrl("javascript:window.location.reload( true )");
    }

    private WebView initWebView() {
        mView = new WebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout frameLayout = findViewById(R.id.root);
//        frameLayout.addView(mView, params);
        WebView.setWebContentsDebuggingEnabled(true);
        mView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
        return mView;
    }
}