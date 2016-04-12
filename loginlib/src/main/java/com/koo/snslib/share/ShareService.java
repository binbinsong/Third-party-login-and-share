package com.koo.snslib.share;

import android.app.Activity;

/**
 * Created by songbinbin on 2015/9/30.
 */
public class ShareService implements IShare {
    private String app_id = "";//app_id
    private String app_key = "";//app_key
    private String app_secret = "";//app_secret
    private Activity mActivity;//activity

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getApp_secret() {
        return app_secret;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void share(ShareContent shareContent) {

    }
}
