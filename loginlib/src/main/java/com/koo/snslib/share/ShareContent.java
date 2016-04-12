package com.koo.snslib.share;

import android.graphics.Bitmap;

import com.koo.snslib.util.ShareType;

/**
 * Created by songbinbin on 2015/9/29.
 */
public class ShareContent {
    private ShareType shareType;
    private String title = "";
    private String content = "";
    private String description = "";
    private Bitmap bitmap = null;
    private String image_url = "";
    private String target_url = "";
    private String wab_page_url = "";

    public ShareType getShareType() {
        return shareType;
    }

    public ShareContent(ShareType shareType) {
        this.shareType = shareType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTarget_url() {
        return target_url;
    }

    public void setTarget_url(String target_url) {
        this.target_url = target_url;
    }

    public String getWab_page_url() {
        return wab_page_url;
    }

    public void setWab_page_url(String wab_page_url) {
        this.wab_page_url = wab_page_url;
    }
}
