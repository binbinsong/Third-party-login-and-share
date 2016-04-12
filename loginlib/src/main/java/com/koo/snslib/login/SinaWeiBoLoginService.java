package com.koo.snslib.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.koo.snslib.sinaweibo.AccessTokenKeeper;
import com.koo.snslib.sinaweibo.LogoutAPI;
import com.koo.snslib.sinaweibo.SinaConstants;
import com.koo.snslib.util.AuthPlatFrom;
import com.koo.snslib.util.SharedUtils;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SinaWeiBoLoginService extends LoginService {
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private LogoutAuthListener logoutAuthListener;

    private IWeiboShareAPI mWeiboShareAPI;

    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();


    @Override
    public void auth(AuthListener callBack) {
        super.auth(callBack);
        sinauAuth(callBack);
    }

    /**
     * @param logoutAuthListener
     */
    @Override
    public void logoutAuth(LogoutAuthListener logoutAuthListener) {
        super.logoutAuth(logoutAuthListener);
        this.logoutAuthListener = logoutAuthListener;
        mAuthInfo = new AuthInfo(getmActivity(), getApp_key(),
                getRedirect_url(), SinaConstants.SCOPE);
        mSsoHandler = new SsoHandler(getmActivity(), mAuthInfo);
        setSsoHandler(mSsoHandler);
        new LogoutAPI(getmActivity(), getApp_key(),
                AccessTokenKeeper.readAccessToken(getmActivity())).logout(mLogoutListener);
    }

    /**
     * @param isAuthListener
     */
    @Override
    public void checkAuth(IsAuthListener isAuthListener) {
        super.checkAuth(isAuthListener);
        SharedUtils sharedUtils = new SharedUtils(getmActivity());
        if ("".equals(sharedUtils.getSinaKeyAccessToken())) {
            isAuthListener.isAuth(true);
        } else {
            isAuthListener.isAuth(false);
        }
    }


    private void sinauAuth(final AuthListener callBack) {
        mAuthInfo = new AuthInfo(getmActivity(), getApp_key(),
                getRedirect_url(), SinaConstants.SCOPE);
        mSsoHandler = new SsoHandler(getmActivity(), mAuthInfo);
        setSsoHandler(mSsoHandler);
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle values) {
                Log.i("sina----", values.toString());
                Map<String, Object> result_map = new HashMap<String, Object>();
                mAccessToken = Oauth2AccessToken.parseAccessToken(values);
                if (mAccessToken.isSessionValid()) {
                    AccessTokenKeeper.writeAccessToken(getmActivity(), mAccessToken);
                    result_map.put("uid", mAccessToken.getUid());
                    result_map.put("accessToken", mAccessToken.getToken());
                    result_map.put("userName", values.getString("userName"));
                    callBack.onAuthSuccess(result_map, AuthPlatFrom.SINA_WEIBO);
                } else {
                    String code = values.getString("code");
                    result_map.put("error_code", code);
                    result_map.put("error_message", "授权失败");
                    callBack.onAuthError(result_map, AuthPlatFrom.SINA_WEIBO);
                }
                setNull();
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }

            @Override
            public void onCancel() {
                callBack.onAuthCancle();
            }
        });

    }

    /**
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");
                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(getmActivity());
                        logoutAuthListener.logoutAuthSuccess();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                logoutAuthListener.logoutAuthError();

            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            logoutAuthListener.logoutAuthError();
        }
    }

    private void setNull() {
        mAuthInfo = null;
        mAccessToken = null;
        mSsoHandler = null;
        logoutAuthListener = null;
        mWeiboShareAPI = null;
    }

}
