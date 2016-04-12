package com.koo.snslib.login;

import com.koo.snslib.util.AuthPlatFrom;

import java.util.Map;

public interface AuthListener {
    /**
     * auth faild
     *
     * @param result_error_map
     */
    public void onAuthError(Map<String, Object> result_error_map, AuthPlatFrom authPlatFrom);

    /**
     * auth success
     *
     * @param result_map
     */
    public void onAuthSuccess(Map<String, Object> result_map, AuthPlatFrom authPlatFrom);

    /**
     * cancel auth
     */
    public void onAuthCancle();

    /**
     * start auth
     */
    public void onAuthStart();
}
