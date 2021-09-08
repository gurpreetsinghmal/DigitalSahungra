package com.sahungra.digitalsahungra;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sahungra.digitalsahungra.R;

public class Detailpostactivity extends Activity {

    ProgressBar progressBar;

    WebView webView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpostactivity);
        progressBar=findViewById(R.id.progressbar);

        webView=findViewById(R.id.detailwebview);

        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);

                webView.setVisibility(View.VISIBLE);
            }
        });

        webView.loadUrl(getIntent().getStringExtra("url"));
    }
}
