package com.login.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.koo.snslib.share.QQShareContent;
import com.koo.snslib.share.QQShareType;
import com.koo.snslib.share.ShareContent;
import com.koo.snslib.share.ShareService;
import com.koo.snslib.share.ShareServiceFactory;
import com.koo.snslib.share.WeiXinShareContent;
import com.koo.snslib.share.WeiXinShareType;
import com.koo.snslib.util.AuthPlatFrom;
import com.koo.snslib.util.ShareType;

public class ShareActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        findViewById(R.id.btn_share_weibo).setOnClickListener(this);
        findViewById(R.id.btn_share_qq).setOnClickListener(this);
        findViewById(R.id.btn_share_qqzone).setOnClickListener(this);
        findViewById(R.id.btn_share_weixin).setOnClickListener(this);
        findViewById(R.id.btn_share_weixin_friends).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share_weibo:
                share("", "", AuthPlatFrom.SINA_WEIBO, getShareContent());
                break;
            case R.id.btn_share_qq:
                share("", "", AuthPlatFrom.QQ, getQQShareContent(QQShareType.QQ));
                break;
            case R.id.btn_share_qqzone:
                share("", "", AuthPlatFrom.QQ, getQQShareContent(QQShareType.QQ_ZONE));
                break;
            case R.id.btn_share_weixin:
                share("", "", AuthPlatFrom.WEIXIN, getWeiXinShareContent(WeiXinShareType.WEIXIN));
                break;
            case R.id.btn_share_weixin_friends:
                share("", "", AuthPlatFrom.WEIXIN, getWeiXinShareContent(WeiXinShareType.WEIXIN_FRIENDS));
                break;
        }


    }

    private WeiXinShareContent getWeiXinShareContent(WeiXinShareType type) {
        WeiXinShareContent weiXinShareContent = new WeiXinShareContent(type, ShareType.SHARE_WEB_PAGE);
        weiXinShareContent.setTitle("微信分享标题");
        weiXinShareContent.setContent("微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容微信分享内容");
        weiXinShareContent.setTarget_url("http://www.baidu.com");
        weiXinShareContent.setImage_url("http://h.hiphotos.baidu.com/image/pic/item/c995d143ad4bd1130c0ee8e55eafa40f4afb0521.jpg");
        return weiXinShareContent;

    }

    private QQShareContent getQQShareContent(QQShareType type) {
        QQShareContent qqShareContent = new QQShareContent(type, ShareType.SHARE_TEXT);
        if (type == QQShareType.QQ) {
            qqShareContent.setTitle("QQ分享标题");
            qqShareContent.setContent("QQ分享内容---------");
            qqShareContent.setTarget_url("http://www.baidu.com");
            qqShareContent.setImage_url("http://b.hiphotos.baidu.com/image/pic/item/eaf81a4c510fd9f9deeb2368272dd42a2834a436.jpg");
        } else {
            qqShareContent.setTitle("QQ空间分享标题");
            qqShareContent.setContent("QQ空间分享内容---------");
            qqShareContent.setTarget_url("http://www.baidu.com");
            qqShareContent.setImage_url("http://h.hiphotos.baidu.com/image/pic/item/c995d143ad4bd1130c0ee8e55eafa40f4afb0521.jpg");
        }
        return qqShareContent;
    }

    private ShareContent getShareContent() {
        ShareContent shareContent = new ShareContent(ShareType.SHARE_TEXT);
        shareContent.setBitmap(null);
        shareContent.setContent("新浪微博分享测试分享测试分享测试分享测试分享测试");
        shareContent.setWab_page_url("http://www.baidu.com");
        return shareContent;

    }

    private void share(@Nullable String app_id, @Nullable String key, AuthPlatFrom param, ShareContent shareContent) {
        ShareService share = ShareServiceFactory.getShareService(param);
        share.setApp_key(key);
        share.setApp_id(app_id);
        share.setmActivity(this);
        share.share(shareContent);
    }
}
