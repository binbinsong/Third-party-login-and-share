/**
 * Copyright (c) 2011 Baidu.com, Inc. All Rights Reserved
 */
package com.koo.snslib.baiduapi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.koo.snslib.baiduapi.BaiduDialog.BaiduDialogListener;

import java.io.IOException;

public class Baidu implements Parcelable {

    private static final String LOG_TAG = "Baidu";

    public static final String CANCEL_URI = "bdconnect://cancel";

    public static String SUCCESS_URI = "bdconnect://success";

    public static final String OAUTHORIZE_URL = "https://openapi.baidu.com/oauth/2.0/authorize";

    public static final String LoggedInUser_URL = "https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser";

    public static final String DISPLAY_STRING = "mobile";

    private static final String[] DEFAULT_PERMISSIONS = {"basic"};

    private static final String KEY_CLIENT_ID = "clientId";


    private String cliendId;

    private AccessTokenManager accessTokenManager;


    public Baidu(String clientId, Context context) {
        if (clientId == null) {
            throw new IllegalArgumentException("apiKey must");
        }
        this.cliendId = clientId;
        init(context);
    }


    public Baidu(Parcel in) {
        Bundle bundle = Bundle.CREATOR.createFromParcel(in);
        this.cliendId = bundle.getString(KEY_CLIENT_ID);
        this.accessTokenManager = AccessTokenManager.CREATOR
                .createFromParcel(in);
    }


    public void init(Context context) {
        if (context
                .checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.w(LOG_TAG,
                    "App miss permission android.permission.ACCESS_NETWORK_STATE! "
                            + "Some mobile's WebView don't display page!");
        } else {
            // WebView.enablePlatformNotifications();
        }
        this.accessTokenManager = new AccessTokenManager(context);
        this.accessTokenManager.initToken();
    }


    public void authorize(Activity activity, String redirectURl, boolean isForceLogin,
                          boolean isConfirmLogin, final BaiduDialogListener listener) {
        Log.i("redirectURl---1", redirectURl + "--au");

        this.authorize(activity, redirectURl, null, isForceLogin, isConfirmLogin, listener);
    }


    public void authorize(Activity activity, String redirectURl, String[] permissions,
                          boolean isForceLogin, boolean isConfirmLogin,
                          final BaiduDialogListener listener) {
        SUCCESS_URI = redirectURl == null || "".equals(redirectURl) ? SUCCESS_URI : redirectURl;
        Log.i("redirectURl---", redirectURl);
        if (this.isSessionValid()) {
            listener.onComplete(new Bundle());
            return;
        }
        this.authorize(activity, permissions, isForceLogin, isConfirmLogin,
                new BaiduDialogListener() {

                    @Override
                    public void onError(BaiduDialogError e) {
                        BaiDuUtil.logd("Baidu-BdDialogError", "DialogError " + e);
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete(Bundle values) {
                        getAccessTokenManager().storeToken(values);
                        listener.onComplete(values);
                    }

                    @Override
                    public void onCancel() {
                        BaiDuUtil.logd("Baidu-BdDialogCancel", "login cancel");
                        listener.onCancel();
                    }

                    @Override
                    public void onBaiduException(BaiduException e) {
                        Log.d("Baidu-BaiduException", "BaiduException : " + e);
                        listener.onBaiduException(e);
                    }
                }, SUCCESS_URI, "token");
    }


    private void authorize(Activity activity, String[] permissions,
                           boolean isForceLogin, boolean isConfirmLogin,
                           final BaiduDialogListener listener, String redirectUrl,
                           String responseType) {
        CookieSyncManager.createInstance(activity);
        Bundle params = new Bundle();
        params.putString("client_id", this.cliendId);
        params.putString("redirect_uri", redirectUrl);
        params.putString("response_type", responseType);
        params.putString("display", DISPLAY_STRING);
        if (isForceLogin) {
            params.putString("force_login", "1");
        }
        if (isConfirmLogin) {
            params.putString("confirm_login", "1");
        }
        if (permissions == null) {
            permissions = DEFAULT_PERMISSIONS;
        }
        if (permissions != null && permissions.length > 0) {
            String scope = TextUtils.join(" ", permissions);
            params.putString("scope", scope);
        }
        String url = OAUTHORIZE_URL + "?" + BaiDuUtil.encodeUrl(params);
        BaiDuUtil.logd("Baidu-Authorize URL", url);
        if (activity.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            BaiDuUtil.showAlert(activity, "no prossinon", "need internet prossion");
        } else {
            new BaiduDialog(activity, url, listener).show();
        }

    }

    /**
     * 将清除存储的token信息
     */
    public void clearAccessToken() {
        if (this.accessTokenManager != null) {
            this.accessTokenManager.clearToken();
            this.accessTokenManager = null;
        }
    }


    public String request(String url, Bundle parameters, String method)
            throws IOException, BaiduException {

        String[] splits = url.split("/");
        String type = splits[3];
        if ("rest".equals(type)) {
            return restRequest(url, parameters, method);
        }
        if ("public".equals(type)) {
            return publicRequest(url, parameters, method);
        }
        if ("file".equals(type)) {
            return fileRequest(url, parameters);
        }
        return null;
    }


    private String restRequest(String url, Bundle parameters, String method)
            throws IOException, BaiduException {
        Bundle params = new Bundle();
        params.putString("access_token", getAccessToken());
        if (parameters != null) {
            params.putAll(parameters);
        }
        String response = BaiDuUtil.openUrl(url, method, params);
        BaiDuUtil.checkResponse(response);
        return response;
    }


    private String publicRequest(String url, Bundle parameters, String method)
            throws IOException, BaiduException {
        Bundle params = new Bundle();
        params.putString("client_id", this.cliendId);
        if (parameters != null) {
            params.putAll(parameters);
        }
        String response = BaiDuUtil.openUrl(url, method, params);
        BaiDuUtil.checkResponse(response);
        return response;
    }


    private String fileRequest(String url, Bundle parameters)
            throws IOException, BaiduException {
        Bundle params = new Bundle();
        params.putString("access_token", getAccessToken());
        if (parameters != null) {
            params.putAll(params);
        }
        String response = BaiDuUtil.uploadFile(url, parameters);
        BaiDuUtil.checkResponse(response);
        return response;

    }


    public boolean isSessionValid() {
        return this.accessTokenManager.isSessionVaild();
    }

    public AccessTokenManager getAccessTokenManager() {
        return this.accessTokenManager;
    }


    public String getAccessToken() {
        return this.accessTokenManager.getAccessToken();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CLIENT_ID, this.cliendId);
        bundle.writeToParcel(dest, flags);
        this.accessTokenManager.writeToParcel(dest, flags);
    }

    public static final Creator<Baidu> CREATOR = new Creator<Baidu>() {

        public Baidu createFromParcel(Parcel in) {
            return new Baidu(in);
        }

        public Baidu[] newArray(int size) {
            return new Baidu[size];
        }
    };

}
