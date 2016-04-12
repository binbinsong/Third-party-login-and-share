package com.koo.snslib.share;

import android.os.StrictMode;

import com.koo.snslib.weixinapi.WXMediaMessageUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by songbinbin on 2015/9/30.
 */
public class WeiXinShareService extends ShareService {
    private WeiXinShareContent weiXinShareContent;
    private IWXAPI api;

    @Override
    public void share(ShareContent shareContent) {
        super.share(shareContent);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        api = WXAPIFactory.createWXAPI(getmActivity(), getApp_id());
        weiXinShareContent = (WeiXinShareContent) shareContent;
        sendShare();
    }

    public void sendShare() {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("weixin");
        req.message = WXMediaMessageUtil.getMessageObject(weiXinShareContent);
        req.scene = weiXinShareContent.getWeiXinShareType() == WeiXinShareType.WEIXIN_FRIENDS ? SendMessageToWX.Req.WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
         api.sendReq(req);
    }


    private String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }
}
