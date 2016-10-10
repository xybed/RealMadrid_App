package com.mumu.realmadrid.http;

/**
 * Created by 7mu on 2016/5/12.
 */
public interface ResponseListener<T> {
    void onSuccess(T result);
    void onFailure(int errCode, String errMsg);
}
