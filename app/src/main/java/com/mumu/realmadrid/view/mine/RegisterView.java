package com.mumu.realmadrid.view.mine;

import com.mumu.realmadrid.model.member.UserModel;
import com.mumu.realmadrid.view.BaseView;

/**
 * Created by 7mu on 2016/9/27.
 */
public interface RegisterView extends BaseView{
    void onTimerRunning(int totalTime);
    void onTimerEnd();
    void registerSuccess(UserModel result);
    void registerFail(String errMsg);
}
