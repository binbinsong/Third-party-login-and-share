/**
 * Copyright (c) 2011 Baidu.com, Inc. All Rights Reserved
 */
package com.koo.snslib.baiduapi;

import android.os.Bundle;

import java.io.IOException;

/**
 * @author chenhetong(chenhetong@baidu.com)
 */
public class AsyncBaiduRunner {

    private Baidu baidu;

    public AsyncBaiduRunner(Baidu baidu) {
        super();
        this.baidu = baidu;
    }


    public void request(final String url, final Bundle parameters,
                        final String method, final RequestListener listener) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String response = baidu.request(url, parameters, method);
                    listener.onComplete(response);
                } catch (BaiduException e) {
                    listener.onBaiduException(e);
                } catch (IOException e) {
                    listener.onIOException(e);
                }
            }
        }).start();
    }


    public static interface RequestListener {


        public void onComplete(String response);


        public void onIOException(IOException e);


        public void onBaiduException(BaiduException e);
    }

}
