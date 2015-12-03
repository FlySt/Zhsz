package com.learn.swl.zhsz.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.learn.swl.zhsz.R;

public class WebActivity extends Activity {
    private WebView mWebView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView = (WebView)findViewById(R.id.wv_web);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        String result = getIntent().getStringExtra("url");
        // mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
       // mWebView.getSettings().setUseWideViewPort(true);//双击缩放
        //mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(result);
        // mWebView.loadDataWithBaseURL(result,data,"text/html","utf-8",null);

    }

}
