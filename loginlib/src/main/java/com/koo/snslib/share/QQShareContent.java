package com.koo.snslib.share;

import com.koo.snslib.util.ShareType;

/**
 * Created by songbinbin on 2015/9/29.
 */
public class QQShareContent extends ShareContent {

    private QQShareType qqShareType;

    public QQShareContent(QQShareType qqShareType, ShareType shareType) {
        super(shareType);
        this.qqShareType = qqShareType;
    }

    public QQShareType getQqShareType() {
        return qqShareType;
    }


}
