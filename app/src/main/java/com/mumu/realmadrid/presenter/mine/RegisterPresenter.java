package com.mumu.realmadrid.presenter.mine;

import com.mumu.realmadrid.iviewmodel.mine.IRegisterViewModel;
import com.mumu.realmadrid.model.member.UserModel;
import com.mumu.realmadrid.view.mine.RegisterView;
import com.mumu.realmadrid.viewmodel.mine.RegisterViewModel;

import lib.utils.TimerUtil;

/**
 * Created by 7mu on 2016/9/27.
 */
public class RegisterPresenter implements TimerUtil.TimerListener, RegisterViewModel.RegisterListener{
    private RegisterView view;
    private IRegisterViewModel model;
    private TimerUtil timerUtil;

    public RegisterPresenter(RegisterView view){
        this.view = view;
        model = new RegisterViewModel(this);
        timerUtil = new TimerUtil(10, 10, this);
    }

    public void startTimer(){
        timerUtil.startTimer();
    }

    public void stopTimer(){
        timerUtil.stopTimer();
    }

    public void register(String username, String password, String verifyCode){
        model.register(username, password, verifyCode);
    }

    @Override
    public void onTimerRunning(int totalTime) {
        view.onTimerRunning(totalTime);
    }

    @Override
    public void onTimerEnd() {
        view.onTimerEnd();
    }

    @Override
    public void registerSuccess(UserModel result) {
        view.dismissLoadingDialog();
        view.registerSuccess(result);
    }

    @Override
    public void registerFail(String errMsg) {
        view.dismissLoadingDialog();
        view.registerFail(errMsg);
    }
}
