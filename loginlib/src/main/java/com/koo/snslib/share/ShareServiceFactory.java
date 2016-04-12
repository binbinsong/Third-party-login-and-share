package com.koo.snslib.share;

import android.util.Log;

import com.koo.snslib.util.AuthPlatFrom;

/**
 *
 * Created by songbinbin on 2015/9/30.
 */
public class ShareServiceFactory {
    public static ShareService getShareService(AuthPlatFrom param) {
        Log.i("param-----------", param.name());
        if (param == AuthPlatFrom.QQ) {
            return new QQShareService();
        } else if (param == AuthPlatFrom.SINA_WEIBO) {
            return new SinaWeiBoShareService();
        } else if (param == AuthPlatFrom.BAIDU) {
            return new BaiDuShareService();
        } else if (param == AuthPlatFrom.WEIXIN) {
            return new WeiXinShareService();
        }
        return null;
    }
}
