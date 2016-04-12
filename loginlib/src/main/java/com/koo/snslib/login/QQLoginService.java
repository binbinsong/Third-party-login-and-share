package com.koo.snslib.login;

import android.content.Intent;

import com.koo.snslib.util.AuthPlatFrom;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * QQ
 */
public class QQLoginService extends LoginService implements IUiListener {
    public Tencent mTencent;
    private AuthListener callBack;
    private Map<String, Object> result_map = new HashMap<String, Object>();

    @Override
    public void auth(AuthListener callBack) {
        super.auth(callBack);
        this.callBack = callBack;
        mTencent = Tencent.createInstance(getApp_id(),
                getmActivity());
        mTencent.login(getmActivity(), "all", this);
    }


    @Override
    public void setQQCallBack(Intent data) {
        mTencent.handleLoginData(data, this);
    }

    @Override
    public void onComplete(Object response) {
        if (response == null || callBack == null || mTencent == null) {
            return;
        }
        try {
            JSONObject jo = (JSONObject) response;
            int ret = jo.getInt("ret");
            if (ret == 0) {
                String openID = jo.getString("openid");
                String accessToken = jo.getString("access_token");
                result_map.put("uid", openID);
                result_map.put("accessToken", accessToken);
                mTencent.setAccessToken(accessToken, jo.getString("expires_in"));
                mTencent.setOpenId(openID);
                UserInfo userInfo = new UserInfo(getmActivity(), mTencent.getQQToken());
                userInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object json) {
                        JSONObject jsonObject = (JSONObject) json;
                        try {
                            String nickname = jsonObject.getString("nickname");
                            result_map.put("userName", nickname);
                            callBack.onAuthSuccess(result_map, AuthPlatFrom.QQ);
                            setNull();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });


            }
        } catch (Exception e) {
        }
    }

    private void setNull() {
        mTencent = null;
        callBack = null;
        result_map.clear();
        result_map = null;

    }

    @Override
    public void onError(UiError uiError) {
        Map<String, Object> result_error_map = new HashMap<String, Object>();
        result_error_map.put("error_code", uiError.errorCode);
        result_error_map.put("error_message", uiError.errorMessage);
        callBack.onAuthError(result_error_map, AuthPlatFrom.QQ);
        setNull();

    }

    @Override
    public void onCancel() {

    }

}
