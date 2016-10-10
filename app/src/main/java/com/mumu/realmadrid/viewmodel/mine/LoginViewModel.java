package com.mumu.realmadrid.viewmodel.mine;

import com.mumu.realmadrid.http.HttpRequestParams;

/**
 * Created by 7mu on 2016/7/15.
 */
public class LoginViewModel {
    public LoginViewModel(LoginListener listener){
        this.listener = listener;
    }

    public void login(String userName, String password){
        HttpRequestParams params = new HttpRequestParams();
        params.put("user_name", userName);
        params.put("password", password);
    }

    private LoginListener listener;
    public interface LoginListener{}
}
