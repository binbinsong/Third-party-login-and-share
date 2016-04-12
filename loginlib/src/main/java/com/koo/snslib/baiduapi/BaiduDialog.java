/**
 * Copyright (c) 2011 Baidu.com, Inc. All Rights Reserved
 */
package com.koo.snslib.baiduapi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * @author chenhetong(chenhetong@baidu.com)
 */
public class BaiduDialog extends Dialog {

    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

    private static final String LOG_TAG = "Baidu-WebView";

    private String mUrl;

    private BaiduDialogListener mListener;

    private ProgressDialog mSpinner;

    private WebView mWebView;

    private FrameLayout mContent;


    public BaiduDialog(Context context, String url, BaiduDialogListener listener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mUrl = url;
        mListener = listener;
        BaiDuUtil.logd(LOG_TAG, "Redirect URL:--- " + url);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mListener.onCancel();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContent = new FrameLayout(getContext());
        setUpWebView();
        addContentView(mContent, FILL);
    }

    private void setUpWebView() {
        RelativeLayout webViewContainer = new RelativeLayout(getContext());
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new BdWebViewClient());
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mWebView.setVisibility(View.INVISIBLE);
        mWebView.getSettings().setSavePassword(false);
        webViewContainer.addView(mWebView);
        mContent.addView(webViewContainer, FILL);
    }

    private class BdWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            BaiDuUtil.logd(LOG_TAG, "Redirect URL: " + url);
            if (url.startsWith(Baidu.SUCCESS_URI)) {
                Bundle values = BaiDuUtil.parseUrl(url);
                if (values != null && !values.isEmpty()) {
                    String error = values.getString("error");
                    if ("access_denied".equals(error)) {
                        mListener.onCancel();
                        BaiduDialog.this.dismiss();
                        return true;
                    }
                    String errorDesp = values.getString("error_description");
                    if (error != null && errorDesp != null) {
                        mListener.onBaiduException(new BaiduException(error, errorDesp));
                        BaiduDialog.this.dismiss();
                        return true;
                    }
                    mListener.onComplete(values);
                    BaiduDialog.this.dismiss();
                    return true;
                }
            } else if (url.startsWith(Baidu.CANCEL_URI)) {
                mListener.onCancel();
                BaiduDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(new BaiduDialogError(description, errorCode, failingUrl));
            BaiduDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            BaiDuUtil.logd(LOG_TAG, "Webview loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSpinner.dismiss();
            mContent.setBackgroundColor(Color.TRANSPARENT);
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    public static interface BaiduDialogListener {


        public void onComplete(Bundle values);


        public void onBaiduException(BaiduException e);


        public void onError(BaiduDialogError e);

        public void onCancel();
    }
}
