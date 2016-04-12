package com.koo.snslib.sinaweibo;

import android.graphics.Bitmap;

import com.koo.snslib.share.ShareContent;
import com.koo.snslib.util.ShareType;
import com.koo.snslib.weixinapi.WeiXinUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by songbinbin on 2015/9/30.
 */
public class SinaMultiMessageUtil {
    public static WeiboMultiMessage getWeiboMessage(ShareContent shareContent) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (shareContent.getShareType() == ShareType.SHARE_TEXT) {
            weiboMessage.textObject = getTextObj(shareContent);
        } else if (shareContent.getShareType() == ShareType.SHARE_IMAGE) {
            weiboMessage.imageObject = getImageObj(shareContent);
        } else if (shareContent.getShareType() == ShareType.SHARE_TEXT_AND_IMAGE) {
            weiboMessage.textObject = getTextObj(shareContent);
            weiboMessage.imageObject = getImageObj(shareContent);
        } else if (shareContent.getShareType() == ShareType.SHARE_WEB_PAGE) {
            weiboMessage.textObject = getTextObj(shareContent);
            weiboMessage.imageObject = getImageObj(shareContent);
            weiboMessage.mediaObject = getWebpageObj(shareContent);
        } else if (shareContent.getShareType() == ShareType.SHARE_MUSIC) {
            weiboMessage.textObject = getTextObj(shareContent);
            weiboMessage.imageObject = getImageObj(shareContent);
            weiboMessage.mediaObject = getMusicObj(shareContent);
        } else if (shareContent.getShareType() == ShareType.SHARE_VIDEO) {
            weiboMessage.textObject = getTextObj(shareContent);
            weiboMessage.imageObject = getImageObj(shareContent);
            weiboMessage.mediaObject = getVideoObj(shareContent);
        }
        return weiboMessage;
    }


    private static TextObject getTextObj(ShareContent shareContent) {
        TextObject textObject = new TextObject();
        textObject.text = shareContent.getContent();
        return textObject;
    }


    private static ImageObject getImageObj(ShareContent shareContent) {
        ImageObject imageObject = new ImageObject();
         Bitmap bmp = shareContent.getBitmap();
        if (bmp == null) {
            bmp = WeiXinUtil.getBitmapFromUrl(shareContent.getImage_url());
        }
        imageObject.setImageObject(bmp);
        return imageObject;
    }


    private static WebpageObject getWebpageObj(ShareContent shareContent) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareContent.getTitle();
        mediaObject.description = shareContent.getDescription();
         mediaObject.setThumbImage(shareContent.getBitmap());
        mediaObject.actionUrl = shareContent.getWab_page_url();
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }


    private static MusicObject getMusicObj(ShareContent shareContent) {
         MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = shareContent.getDescription();
        musicObject.description = shareContent.getDescription();
         musicObject.setThumbImage(shareContent.getBitmap());
        musicObject.actionUrl = shareContent.getTarget_url();
        musicObject.dataUrl = shareContent.getTarget_url();
        musicObject.dataHdUrl = shareContent.getTarget_url();
        musicObject.duration = 10;
        musicObject.defaultText = "Music 默认文案";
        return musicObject;
    }


    private static VideoObject getVideoObj(ShareContent shareContent) {
         VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = shareContent.getTitle();
        videoObject.description = shareContent.getDescription();
        Bitmap bitmap = shareContent.getBitmap();
         ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Weibo.BaseMediaObject", "put thumb failed");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        videoObject.setThumbImage(bitmap);
        videoObject.actionUrl = shareContent.getTarget_url();
        videoObject.dataUrl = shareContent.getTarget_url();
        videoObject.dataHdUrl = shareContent.getTarget_url();
        videoObject.duration = 10;
        videoObject.defaultText = "Vedio 默认文案";
        return videoObject;
    }
}
