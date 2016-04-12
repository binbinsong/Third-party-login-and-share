package com.koo.snslib.weixinapi;

import android.graphics.Bitmap;

import com.koo.snslib.share.WeiXinShareContent;
import com.koo.snslib.util.ShareType;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

/**
 * Created by songbinbin on 2015/9/30.
 */
public class WXMediaMessageUtil {
    private static final int THUMB_SIZE = 150;

    public static WXMediaMessage getMessageObject(WeiXinShareContent weiXinShareContent) {
        WXMediaMessage msg = new WXMediaMessage();
        if (weiXinShareContent.getShareType() == ShareType.SHARE_TEXT) {
             WXTextObject textObj = new WXTextObject();
            textObj.text = weiXinShareContent.getContent();
             // msg.title = "Will be ignored";
            msg.messageExt = weiXinShareContent.getContent();
            msg.mediaObject = textObj;
        } else if (weiXinShareContent.getShareType() == ShareType.SHARE_IMAGE) {
            WXImageObject imgObj = new WXImageObject();
            Bitmap bmp = weiXinShareContent.getBitmap();
            if (bmp == null) {
                bmp = WeiXinUtil.getBitmapFromUrl(weiXinShareContent.getImage_url());
                Bitmap thumbBmp = Bitmap
                        .createScaledBitmap(
                                bmp,
                                THUMB_SIZE,
                                THUMB_SIZE,
                                true);
                bmp.recycle();
                imgObj.imageData = WeiXinUtil
                        .bmpToByteArray(
                                thumbBmp, true);
            } else {
                imgObj.imageData = WeiXinUtil
                        .bmpToByteArray(
                                bmp, true);
            }
            msg.mediaObject = imgObj;
            msg.title = weiXinShareContent.getTitle();
            msg.description = weiXinShareContent.getContent();
            msg.messageExt = weiXinShareContent.getContent();
        } else if (weiXinShareContent.getShareType() == ShareType.SHARE_WEB_PAGE) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = weiXinShareContent.getTarget_url();
            msg.mediaObject = webpage;
            msg.title = weiXinShareContent.getTitle();
            msg.description = weiXinShareContent.getContent();
            String url = weiXinShareContent.getImage_url();
            Bitmap bmp = weiXinShareContent.getBitmap();
            if (bmp == null) {
                bmp = WeiXinUtil.getBitmapFromUrl(weiXinShareContent.getImage_url());
                Bitmap thumbBmp = Bitmap
                        .createScaledBitmap(
                                bmp,
                                THUMB_SIZE,
                                THUMB_SIZE,
                                true);
                bmp.recycle();
                msg.thumbData = WeiXinUtil
                        .bmpToByteArray(
                                thumbBmp, true);
            } else {
                msg.thumbData = WeiXinUtil
                        .bmpToByteArray(
                                bmp, true);
            }
        } else if (weiXinShareContent.getShareType() == ShareType.SHARE_MUSIC) {
            WXMusicObject music = new WXMusicObject();
            // music.musicUrl =
            // "http://www.baidu.com";
            music.musicUrl = weiXinShareContent.getTarget_url();
            // music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";
            msg.mediaObject = music;
            msg.title = weiXinShareContent.getTitle();
            msg.description = weiXinShareContent.getDescription();
            Bitmap thumb = weiXinShareContent.getBitmap();
            if (thumb != null) {
                msg.thumbData = WeiXinUtil
                        .bmpToByteArray(thumb, true);
            }
        } else if (weiXinShareContent.getShareType() == ShareType.SHARE_MUSIC) {
            WXVideoObject video = new WXVideoObject();
            video.videoUrl = weiXinShareContent.getTarget_url();
            msg.mediaObject = video;
            msg.title = weiXinShareContent.getTitle();
            msg.description = weiXinShareContent.getDescription();
            Bitmap thumb = weiXinShareContent.getBitmap();
            if (thumb != null) {
                msg.thumbData = WeiXinUtil
                        .bmpToByteArray(thumb, true);
            }
        }
        return msg;
    }
}