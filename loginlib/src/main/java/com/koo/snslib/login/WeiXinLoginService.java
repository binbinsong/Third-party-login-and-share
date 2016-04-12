package com.koo.snslib.login;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.koo.snslib.baiduapi.BaiDuUtil;
import com.koo.snslib.share.WeiXinShareContent;
import com.koo.snslib.util.AuthPlatFrom;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class WeiXinLoginService extends LoginService {
    private final static String GET_TOOKEN_API = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private final static String GET_USER_INFO_API = "https://api.weixin.qq.com/sns/userinfo";

    private IWXAPI api;
    private static AuthListener callBack;
    private static String app_id_;
    private static String app_scrept_;
    private WeiXinShareContent weiXinShareContent;
    private static MyHandler myHandler = new MyHandler();
    private static Map<String, Object> result_map = new HashMap<String, Object>();

    @Override
    public void auth(AuthListener callBack) {
        super.auth(callBack);
        this.callBack = callBack;
        if (!checkApkExist(getmActivity(), "com.tencent.mm")) {
            getmActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getmActivity(), "请先安装微信客户端再操作", Toast.LENGTH_SHORT).show();
                }
            });
            callBack.onAuthCancle();
        }
        app_id_ = getApp_id();
        app_scrept_ = getApp_secret();
        Log.i("token_str----------==", app_id_);
        api = WXAPIFactory.createWXAPI(getmActivity(),
                app_id_, false);
        api.registerApp(app_id_);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "carjob_wx_login";
        api.sendReq(req);
    }


    public static void setWeiXinAuthResult(int errCode, String code) {
        if (callBack == null) {
            return;
        }
        switch (errCode) {
            case BaseResp.ErrCode.ERR_OK:
                getWinXinToken(code, app_id_);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                callBack.onAuthCancle();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
    }


    private static void getWinXinToken(final String code, final String app_id) {
        new Thread() {
            public void run() {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("code",
                            code);
                    bundle.putString("appid",
                            app_id);
                    bundle.putString("secret",
                            app_scrept_);
                    bundle.putString("grant_type",
                            "authorization_code");
                    String token_str = BaiDuUtil
                            .openUrl(
                                    GET_TOOKEN_API,
                                    "GET", bundle);
                    result_map.put("code", code);
                    Message message = myHandler.obtainMessage();
                    message.obj = token_str;
                    message.what = 0;
                    myHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            JSONObject jsonObject = null;
            switch (msg.what) {
                case 0:
                    try {
                        jsonObject = new JSONObject(result);
                        if (jsonObject.has("errcode")) {
                            result_map.put("error_code", jsonObject.getString("errcode"));
                            result_map.put("error_message", jsonObject.getString("errmsg"));
                            callBack.onAuthError(result_map, AuthPlatFrom.WEIXIN);

                        } else {
                            result_map.put("uid", jsonObject.getString("unionid"));
                            result_map.put("accessToken", jsonObject.getString("access_token"));
                            getWeiXinUserInfo(jsonObject.getString("openid"), result_map.get("accessToken").toString());
                            //  callBack.onAuthSuccess(result_map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        jsonObject = new JSONObject(result);
                        result_map.put("userName", jsonObject.getString("nickname"));
                        callBack.onAuthSuccess(result_map, AuthPlatFrom.WEIXIN);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private static void getWeiXinUserInfo(final String open_id, final String access_token) {
        new Thread() {
            public void run() {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("openid",
                            open_id);
                    bundle.putString("access_token",
                            access_token);
                    String token_str = BaiDuUtil
                            .openUrl(
                                    GET_USER_INFO_API,
                                    "GET", bundle);
                    Message message = myHandler.obtainMessage();
                    message.obj = token_str;
                    message.what = 1;
                    myHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static String getApp_id_() {
        return app_id_;
    }

    public static void setApp_id_(String app_id_) {
        WeiXinLoginService.app_id_ = app_id_;
    }

    private void setNull() {
        api = null;
        callBack = null;
        result_map.clear();
        result_map = null;
        myHandler.removeCallbacksAndMessages(null);
        myHandler = null;
    }

    /**
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}