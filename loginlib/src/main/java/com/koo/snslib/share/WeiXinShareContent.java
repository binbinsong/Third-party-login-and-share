package com.koo.snslib.share;

import com.koo.snslib.util.ShareType;

/**
 * Created by songbinbin on 2015/9/29.
 */
public class WeiXinShareContent extends ShareContent {
    public WeiXinShareContent(WeiXinShareType weiXinShareType, ShareType shareType) {
        super(shareType);
        this.weiXinShareType = weiXinShareType;
    }

    private WeiXinShareType weiXinShareType;

    public WeiXinShareType getWeiXinShareType() {
        return weiXinShareType;
    }


}
