package com.koo.snslib.login;

public interface ILogin {
    /**
     * auy=th
     *
     * @param callBack
     */
    public void auth(AuthListener callBack);

    /**
     * @param logoutAuthListener
     */
    public void logoutAuth(LogoutAuthListener logoutAuthListener);

    /**
     * @return
     */
    public void checkAuth(IsAuthListener isAuthListener);

}
