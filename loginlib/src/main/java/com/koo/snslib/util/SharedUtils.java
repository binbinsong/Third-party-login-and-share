package com.koo.snslib.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
  */
public class SharedUtils {
    private final String SP_NAME = "auth";
    private SharedPreferences sharedPreferences = null;
    private Editor editor;
    private final String SINA_KEY_UID = "sina_uid";
    private final String SINA_KEY_ACCESS_TOKEN = "sina_access_token";
    private final String SINA_KEY_EXPIRES_IN = "sina_expires_in";
    private final String SINA_KEY_REFRESH_TOKEN = "sina_refresh_token";

    public SharedUtils(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSinaKeyUid(String sina_uid) {
        editor.putString(SINA_KEY_UID, sina_uid);
        editor.commit();
    }

    public String getSinaKeyUid() {
        return sharedPreferences.getString(SINA_KEY_UID, "");
    }

    public void setSinaKeyAccessToken(String sinaKeyAccessToken) {
        editor.putString(SINA_KEY_ACCESS_TOKEN, sinaKeyAccessToken);
        editor.commit();
    }

    public String getSinaKeyAccessToken() {
        return sharedPreferences.getString(SINA_KEY_ACCESS_TOKEN, "");
    }

    public void setSinaKeyExpiresIn(Long sinaKeyExpiresIn) {
        editor.putLong(SINA_KEY_EXPIRES_IN, sinaKeyExpiresIn);
        editor.commit();
    }

    public Long getSinaKeyExpiresIn() {
        return sharedPreferences.getLong(SINA_KEY_EXPIRES_IN, 0);

    }

    public void setSinaKeyRefreshToken(String sinaKeyRefreshToken) {
        editor.putString(SINA_KEY_REFRESH_TOKEN, sinaKeyRefreshToken);
        editor.commit();
    }

    public String getSinaKeyRefreshToken() {
        return sharedPreferences.getString(SINA_KEY_REFRESH_TOKEN, "");

    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);

    }

    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }
}
