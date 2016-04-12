package com.koo.snslib.login;

import android.util.Log;

import com.koo.snslib.util.AuthPlatFrom;

/**
 */
public class LoginServiceFactory {

    public static LoginService getLoginService(AuthPlatFrom param) {
        Log.i("param-----------", param.name());
        if (param == AuthPlatFrom.QQ) {
            return new QQLoginService();
        } else if (param == AuthPlatFrom.SINA_WEIBO) {
            return new SinaWeiBoLoginService();
        } else if (param == AuthPlatFrom.BAIDU) {
            return new BaiDuLoginService();
        } else if (param == AuthPlatFrom.WEIXIN) {
            return new WeiXinLoginService();
        }
        return null;
    }

}
