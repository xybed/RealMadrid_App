package com.mumu.realmadrid.view;

/**
 * Created by 7mu on 2016/10/8.
 */
public interface BaseView {
    void showLoadingDialog(String message, boolean cancelable, boolean otoCancelable);
    void showLoadingDialog(String message);
    void dismissLoadingDialog();
    void hideSoftInput();
    void transparencyStatusBar(boolean isTransparency);
    void transparencyStatusBar();
}
