package com.fb.multilogin;

import android.os.Bundle;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.webkit.GeolocationPermissions;
import java.io.File;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = this.bridge.getWebView();

        // ওয়েবভিউ ক্লায়েন্ট কনফিগারেশন
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // ১. স্যান্ডবক্স ডিরেক্টরি ইনিশিয়ালাইজেশন
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String defaultProfile = "fb_master_sandbox";
            try {
                File dataDir = new File(getDataDir(), defaultProfile);
                if (!dataDir.exists()) {
                    dataDir.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ২. ওয়েবভিউ সেটিংস কনফিগারেশন
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        // ৩. কুকিজ এবং স্যান্ডবক্স আইসোলেশন
        CookieManager.getInstance().setAcceptCookie(true);

        // ৪. জিওলোকেশন এবং পারমিশন ওভাররাইড
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
    }
}