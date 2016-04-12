package com.login.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.koo.snslib.login.AuthListener;
import com.koo.snslib.login.IsAuthListener;
import com.koo.snslib.login.LoginService;
import com.koo.snslib.login.LoginServiceFactory;
import com.koo.snslib.login.LogoutAuthListener;
import com.koo.snslib.util.AuthPlatFrom;

import java.util.Map;

public class AuthActivity extends Activity implements View.OnClickListener {
    private Button btn;
    private Button qq_btn;
    private AuthPlatFrom param;
    private LoginService service;
    private LoginService logoutAuth;
    private LoginService checkAuth;
    private int auth_type = 1;//1登录/2注销

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.btn_qq).setOnClickListener(this);
        findViewById(R.id.btn_baidu).setOnClickListener(this);
        findViewById(R.id.btn_weixin).setOnClickListener(this);
        findViewById(R.id.btn_zhuxiao_sianlang).setOnClickListener(new OnClickLogoutAuthListener());
        findViewById(R.id.btn_zhuxiao_baidu).setOnClickListener(new OnClickLogoutAuthListener());
        findViewById(R.id.btn_check_sina).setOnClickListener(new OnClickCheckAuthListener());
        findViewById(R.id.btn_check_baidu).setOnClickListener(new OnClickCheckAuthListener());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (auth_type == 1) {
            if (param == AuthPlatFrom.QQ) {
                service.setQQCallBack(data);
            } else if (param == AuthPlatFrom.SINA_WEIBO) {
                service.getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
            }
        } else {
            if (param == AuthPlatFrom.SINA_WEIBO) {
                logoutAuth.getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
            }
        }
    }

    /**
     * @param app_id
     * @param app_key
     * @param app_scrept
     */
    private void auth(@Nullable String app_id, @Nullable String app_key, @Nullable String app_scrept, @Nullable String redirect_url) {
        service = LoginServiceFactory.getLoginService(param);
        service.setApp_id(app_id);
        service.setApp_key(app_key);
        service.setApp_secret(app_scrept);
        service.setmActivity(this);
//      service.setRedirect_url();//回调地址
        service.auth(new AuthListener() {
            @Override
            public void onAuthError(Map<String, Object> result_error_map, AuthPlatFrom authPlatFrom) {
                Toast.makeText(AuthActivity.this, "授权失败\n" + result_error_map.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthSuccess(Map<String, Object> result_map, AuthPlatFrom authPlatFrom) {
                Toast.makeText(AuthActivity.this, "登录成功\n" + result_map.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthCancle() {

            }

            @Override
            public void onAuthStart() {

            }
        });

    }

    /**
     * 运行前请先填写对应的key信息
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        auth_type = 1;
        switch (v.getId()) {
            case R.id.btn_qq:
                param = AuthPlatFrom.QQ;
                auth("", "", "", "");
                break;
            case R.id.button:
                param = AuthPlatFrom.SINA_WEIBO;
                auth("", "", "", "");
                break;
            case R.id.btn_baidu:
                param = AuthPlatFrom.BAIDU;
                auth("", "", "", "");
                break;
            case R.id.btn_weixin:
                param = AuthPlatFrom.WEIXIN;
                auth("", "", "", "");
                break;

        }
    }

    private void logoutAuth(String app_key) {
        logoutAuth = LoginServiceFactory.getLoginService(param);
        logoutAuth.setApp_key(app_key);
        logoutAuth.setmActivity(AuthActivity.this);
        logoutAuth.logoutAuth(new LogoutAuthListener() {
            @Override
            public void logoutAuthError() {
                Toast.makeText(AuthActivity.this, "註銷失敗", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void logoutAuthSuccess() {
                Toast.makeText(AuthActivity.this, "註銷成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class OnClickLogoutAuthListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            auth_type = 2;
            switch (v.getId()) {
                case R.id.btn_zhuxiao_sianlang:
                    param = AuthPlatFrom.SINA_WEIBO;
                    logoutAuth("");
                    break;
                case R.id.btn_zhuxiao_baidu:
                    param = AuthPlatFrom.BAIDU;
                    logoutAuth("");
                    break;
            }
        }
    }

    class OnClickCheckAuthListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_check_sina:
                    param = AuthPlatFrom.SINA_WEIBO;
                    checkAuth("");
                    break;
                case R.id.btn_check_baidu:
                    param = AuthPlatFrom.BAIDU;
                    checkAuth("");
                    break;
            }
        }
    }

    public void checkAuth(String app_key) {
        logoutAuth = LoginServiceFactory.getLoginService(param);
        logoutAuth.setmActivity(AuthActivity.this);
        logoutAuth.setApp_key(app_key);
        logoutAuth.checkAuth(new IsAuthListener() {
            @Override
            public void isAuth(boolean isAuth) {
                if (isAuth) {
                    Toast.makeText(AuthActivity.this, "已经授权", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AuthActivity.this, "没有授权", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
