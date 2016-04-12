package com.koo.snslib.login;

import android.app.Activity;
import android.content.Intent;

import com.sina.weibo.sdk.auth.sso.SsoHandler;

public abstract class LoginService implements ILogin {
    private String app_id = "";//app_id
    private String app_key = "";//app_key
    private String app_secret = "";//app_secret
    private String redirect_url = "";//
    private Activity mActivity;// activity
    private SsoHandler ssoHandler;//sina handler

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getApp_key() {
        return app_key;
    }

    public String getApp_secret() {
        return app_secret;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    @Override
    public void auth(AuthListener callBack) {

    }

    @Override
    public void logoutAuth(LogoutAuthListener logoutAuthListener) {

    }

    @Override
    public void checkAuth(IsAuthListener isAuthListener) {

    }


    public void setQQCallBack(Intent data) {

    }

    public SsoHandler getSsoHandler() {
        return ssoHandler;
    }

    public void setSsoHandler(SsoHandler ssoHandler) {
        this.ssoHandler = ssoHandler;
    }
}
