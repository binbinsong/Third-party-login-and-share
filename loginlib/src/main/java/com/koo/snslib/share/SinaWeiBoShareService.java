package com.koo.snslib.share;

import android.os.Bundle;
import android.util.Log;

import com.koo.snslib.sinaweibo.AccessTokenKeeper;
import com.koo.snslib.sinaweibo.SinaConstants;
import com.koo.snslib.sinaweibo.SinaMultiMessageUtil;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 *
 * Created by songbinbin on 2015/9/30.
 */
public class SinaWeiBoShareService extends ShareService {
    /**
     */
    private IWeiboShareAPI mWeiboShareAPI;

    /**
     *
     * @param shareContent
     */
    @Override
    public void share(ShareContent shareContent) {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getmActivity(), getApp_key());
        mWeiboShareAPI.registerApp();
        sendShare(SinaMultiMessageUtil.getWeiboMessage(shareContent));
    }

    private void sendShare(WeiboMultiMessage weiboMessage) {
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        AuthInfo authInfo = new AuthInfo(getmActivity(), getApp_key(),
                SinaConstants.REDIRECT_URL, SinaConstants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper
                .readAccessToken(getmActivity());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(getmActivity(), request, authInfo, token,
                new WeiboAuthListener() {

                    @Override
                    public void onWeiboException(WeiboException arg0) {
                    }

                    @Override
                    public void onComplete(Bundle bundle) {
                        // TODO Auto-generated method stub
                        Oauth2AccessToken newToken = Oauth2AccessToken
                                .parseAccessToken(bundle);
                        AccessTokenKeeper.writeAccessToken(
                                getmActivity(), newToken);
                        Log.i("share-------", bundle.toString());
                    }

                    @Override
                    public void onCancel() {
                    }
                });
    }
}
