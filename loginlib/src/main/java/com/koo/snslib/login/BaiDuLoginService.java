package com.koo.snslib.login;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.koo.snslib.baiduapi.AccessTokenManager;
import com.koo.snslib.baiduapi.AsyncBaiduRunner;
import com.koo.snslib.baiduapi.Baidu;
import com.koo.snslib.baiduapi.BaiduDialog.BaiduDialogListener;
import com.koo.snslib.baiduapi.BaiduDialogError;
import com.koo.snslib.baiduapi.BaiduException;
import com.koo.snslib.util.AuthPlatFrom;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * baidu auth and  share
 */
public class BaiDuLoginService extends LoginService {
    private Baidu baidu = null;
    private boolean isForceLogin = false;

    private boolean isConfirmLogin = true;
    private Handler mHandler = null;
    private AuthListener authListener;

    @Override
    public void auth(AuthListener callBack) {
        super.auth(callBack);
        this.authListener = callBack;
        mHandler = new Handler();
        baidu = new Baidu(getApp_key(), getmActivity());
        baidu.authorize(getmActivity(), getRedirect_url(), isForceLogin,
                isConfirmLogin, new BaiduDialogListener() {
                    @Override
                    public void onComplete(Bundle values) {
                        Log.i("baidu---", values.toString());
                        String url = Baidu.LoggedInUser_URL;
                        AsyncBaiduRunner runner = new AsyncBaiduRunner(baidu);
                        runner.request(url, null, "POST", new DefaultRequstListener());
                    }

                    @Override
                    public void onBaiduException(BaiduException e) {
                        Map<String, Object> result_error_map = new HashMap<String, Object>();
                        result_error_map.put("error_code", e.getErrorCode());
                        result_error_map.put("error_message", e.getMessage());
                        authListener.onAuthError(result_error_map, AuthPlatFrom.BAIDU);
                        stNull();
                    }

                    @Override
                    public void onError(BaiduDialogError e) {
                        Map<String, Object> result_error_map = new HashMap<String, Object>();
                        result_error_map.put("error_code", e.getErrorCode());
                        result_error_map.put("error_message", e.getMessage());
                        authListener.onAuthError(result_error_map, AuthPlatFrom.BAIDU);
                    }

                    @Override
                    public void onCancel() {
                        authListener.onAuthCancle();
                    }
                });
    }

    public class DefaultRequstListener implements AsyncBaiduRunner.RequestListener {

        /* (non-Javadoc)
         * @see com.baidu.android.RequestListener#onBaiduException(com.baidu.android.BaiduException)
         */
        @Override
        public void onBaiduException(BaiduException e) {
            Map<String, Object> result_error_map = new HashMap<String, Object>();
            result_error_map.put("error_code", e.getErrorCode());
            result_error_map.put("error_message", e.getMessage());
            authListener.onAuthError(result_error_map, AuthPlatFrom.BAIDU);
            stNull();
        }

        /* (non-Javadoc)
         * @see com.baidu.android.RequestListener#onComplete(java.lang.String)
         */
        @Override
        public void onComplete(final String value) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    AccessTokenManager atm = baidu.getAccessTokenManager();
                    String accessToken = atm.getAccessToken();
                    Log.i("value--------------", value);

                    try {
                        JSONObject jsonObject = new JSONObject(value);
                        String uid = jsonObject.getString("uid");
                        Map<String, Object> result_map = new HashMap<String, Object>();
                        result_map.put("uid", uid);
                        result_map.put("accessToken", accessToken);
                        result_map.put("userName", jsonObject.get("uname"));
                        result_map.put("userName", jsonObject.get("uname"));
                        authListener.onAuthSuccess(result_map, AuthPlatFrom.BAIDU);
                        stNull();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        /* (non-Javadoc)
         * @see com.baidu.android.RequestListener#onIOException(java.io.IOException)
         */
        @Override
        public void onIOException(IOException arg0) {

        }

    }

    @Override
    public void logoutAuth(LogoutAuthListener logoutAuthListener) {
        super.logoutAuth(logoutAuthListener);
        baidu = new Baidu(getApp_key(), getmActivity());
        baidu.clearAccessToken();
        logoutAuthListener.logoutAuthSuccess();
    }

    @Override
    public void checkAuth(IsAuthListener isAuthListener) {
        super.checkAuth(isAuthListener);
        baidu = new Baidu(getApp_key(), getmActivity());
        AccessTokenManager atm = baidu.getAccessTokenManager();
        String accessToken = atm.getAccessToken();
        if ("".equals(accessToken) || null == accessToken) {
            isAuthListener.isAuth(false);
        } else {
            isAuthListener.isAuth(true);
        }
    }

    private void stNull() {
        baidu = null;
        mHandler = null;
        authListener = null;
    }
}
