package com.koo.snslib.share;

import android.os.Bundle;
import android.util.Log;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
  * Created by songbinbin on 2015/9/30.
 */
public class QQShareService extends ShareService {
    public Tencent mTencent;
    private QQShareContent qqShareContent;

    @Override
    public void share(ShareContent shareContent) {
        super.share(shareContent);
        qqShareContent = (QQShareContent) shareContent;
        mTencent = Tencent.createInstance(getApp_key(),
                getmActivity());
        if (qqShareContent.getQqShareType() == QQShareType.QQ) {
            shareQQ();
        } else {
            shareQQZone();
        }

    }

    private void shareQQ() {
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, qqShareContent.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, qqShareContent.getTarget_url());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, qqShareContent.getContent());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, qqShareContent.getImage_url());
        //        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName.getText()
//                .toString());
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);
        doShareToQQ(params);
    }

    private void shareQQZone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, qqShareContent.getTitle());
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, qqShareContent.getContent());
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, qqShareContent.getTarget_url());
         ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add(qqShareContent.getImage_url());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        // params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, qqShareContent.getImage_url());
        doShareToQzone(params);
    }

    private void doShareToQQ(final Bundle params) {
         ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(getmActivity(),
                        params, null);
            }
        });
    }

    /**
      *
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
         ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone(getmActivity(), params, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Log.i("zong-------------", o.toString());

                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.i("zong-------------", uiError.errorMessage + "   " + uiError.errorCode);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });
    }
}
