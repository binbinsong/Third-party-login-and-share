/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.koo.snslib.sinaweibo;

import android.content.Context;

import com.koo.snslib.util.SharedUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
  *
 * @author SINA
 * @since 2013-10-07
 */
public class AccessTokenKeeper {
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context || null == token) {
            return;
        }
        SharedUtils sharedUtils = new SharedUtils(context);
        sharedUtils.setSinaKeyUid(token.getUid());
        sharedUtils.setSinaKeyAccessToken(token.getToken());
        sharedUtils.setSinaKeyRefreshToken(token.getRefreshToken());
        sharedUtils.setSinaKeyExpiresIn(token.getExpiresTime());
    }


    public static Oauth2AccessToken readAccessToken(Context context) {
        if (null == context) {
            return null;
        }
        SharedUtils sharedUtils = new SharedUtils(context);
        Oauth2AccessToken token = new Oauth2AccessToken();
        token.setUid(sharedUtils.getSinaKeyUid());
        token.setToken(sharedUtils.getSinaKeyAccessToken());
        token.setRefreshToken(sharedUtils.getSinaKeyRefreshToken());
        token.setExpiresTime(sharedUtils.getSinaKeyExpiresIn());
        return token;
    }


    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        SharedUtils sharedUtils = new SharedUtils(context);
        sharedUtils.clearData();
    }
}
