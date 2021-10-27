package com.example.ibrep.projectf2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Oraculo extends AppCompatActivity {

    SwipeRefreshLayout swipe;
    ProgressBar progressBar;

    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oraculo);

        myWebView = (WebView) findViewById(R.id.webview);


        progressBar = (ProgressBar) findViewById(R.id.awv_progressBar);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        LoadWeb();

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWebView.reload();
            }
        });

        progressBar.setMax(100);
        progressBar.setProgress(1);

        myWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {


                progressBar.setProgress(progress);
            }
        });

        myWebView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }


            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                //myWebView.loadUrl("file:///android_asset/error.html");
            }

            public void onLoadResource(WebView view, String url) { //Doesn't work
                //swipe.setRefreshing(true);
            }

            public void onPageFinished(WebView view, String url) {

                //Hide the SwipeReefreshLayout
                progressBar.setVisibility(View.GONE);
                swipe.setRefreshing(false);
            }

        });

    }

    public void LoadWeb() {

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        myWebView.setWebViewClient(webViewClient);

        myWebView.loadUrl("http://ibrep.alfamaoraculo.com.br/vendedor/");

        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);

        WebSettings webSettings = myWebView.getSettings();
        //Habilitando o JavaScript
        webSettings.setJavaScriptEnabled(true);
        swipe.setRefreshing(true);
    }


    boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("WrongConstant")
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public static class WebViewClientImpl extends WebViewClient {
        private Activity activity = null;
        public WebViewClientImpl(Activity activity) {
            this.activity = activity;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if(url.contains("ibrep.alfamaoraculo.com.br")) return false;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }
    }
}

