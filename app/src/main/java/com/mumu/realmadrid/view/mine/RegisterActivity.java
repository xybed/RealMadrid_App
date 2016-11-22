package com.mumu.realmadrid.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mumu.realmadrid.MyApplication;
import com.mumu.realmadrid.R;
import com.mumu.realmadrid.model.member.UserModel;
import com.mumu.realmadrid.presenter.mine.RegisterPresenter;
import com.mumu.realmadrid.view.BaseActivity;
import com.mumu.realmadrid.view.MainActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.SMSSDK;
import lib.share.ShareSDKLogin;
import lib.utils.RegexUtil;
import lib.utils.StringUtil;
import lib.utils.ToastUtil;

public class RegisterActivity extends BaseActivity implements RegisterView, ShareSDKLogin.SMSListener{

    @Bind(R.id.edit_username)
    MaterialEditText editUsername;
    @Bind(R.id.edit_password)
    MaterialEditText editPassword;
    @Bind(R.id.img_password_look)
    ImageView imgPasswordLook;
    @Bind(R.id.edit_verify_code)
    MaterialEditText editVerifyCode;
    @Bind(R.id.txt_get_verify)
    TextView txtGetVerify;
    @Bind(R.id.txt_register)
    TextView txtRegister;
    @Bind(R.id.cb_agree_rule)
    CheckBox cbAgreeRule;

    private RegisterPresenter presenter;
    private ShareSDKLogin shareSDKLogin;
    private boolean isLook = false;
    private boolean isAgree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        transparencyStatusBar();
        presenter = new RegisterPresenter(this);
        initUI();
        initLogic();
    }

    private void initUI(){
        ButterKnife.bind(this);
        txtRegister.setClickable(false);
        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setRegisterButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setRegisterButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setRegisterButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initLogic(){
        shareSDKLogin = new ShareSDKLogin(this, this);
    }

    @OnClick({R.id.btn_left, R.id.img_password_look, R.id.txt_get_verify, R.id.cb_agree_rule, R.id.txt_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.img_password_look:
                togglePassword();
                break;
            case R.id.txt_get_verify:
                getVerifyCode();
                break;
            case R.id.cb_agree_rule:
                toggleCbAgree();
                break;
            case R.id.txt_register:
                register();
                break;
        }
    }

    /**
     * 切换密码可见
     */
    private void togglePassword() {
        if(!isLook){
            imgPasswordLook.setImageResource(R.drawable.icon_password_look);
            editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            imgPasswordLook.setImageResource(R.drawable.icon_password_unlook);
            editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        isLook = !isLook;
        editPassword.setSelection(editPassword.getText().length());
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        /*
        1.如果手机号符合规则，则去获取验证码
        2.开启倒计时，设置按钮状态
         */
        if(RegexUtil.checkMobile(editUsername.getText().toString())){
            SMSSDK.getVerificationCode("86", editUsername.getText().toString());
            presenter.startTimer();
        }else{
            ToastUtil.show(getString(R.string.user_input_phone_number));
        }
    }

    /**
     * 勾选框
     */
    private void toggleCbAgree() {
        cbAgreeRule.setChecked(!isAgree);
        isAgree = !isAgree;
        setRegisterButtonState();
    }

    /**
     * 设置注册按钮状态
     */
    private void setRegisterButtonState(){
        if(!StringUtil.isEmpty(editUsername.getText().toString())
                && !StringUtil.isEmpty(editPassword.getText().toString())
                && !StringUtil.isEmpty(editVerifyCode.getText().toString())
                && isAgree){
            txtRegister.setBackgroundResource(R.drawable.selector_rect_theme_color);
            txtRegister.setClickable(true);
        }else{
            txtRegister.setBackgroundResource(R.drawable.shape_rect_gray_h);
            txtRegister.setClickable(false);
        }
    }

    private void register(){
        if(!RegexUtil.checkMobile(editUsername.getText().toString())){
            ToastUtil.show(getString(R.string.user_input_phone_number));
            return;
        }
        if(editPassword.getText().toString().length() < 6){
            ToastUtil.show(getString(R.string.user_password_cannot_less_six));
            return;
        }
        SMSSDK.submitVerificationCode("86", editUsername.getText().toString(), editVerifyCode.getText().toString());
    }

    @Override
    public void onTimerRunning(int totalTime) {
        txtGetVerify.setText(getString(R.string.com_second_placeholder, totalTime));
        txtGetVerify.setBackgroundResource(R.drawable.shape_rect_gray_h);
        txtGetVerify.setClickable(false);
    }

    @Override
    public void onTimerEnd() {
        txtGetVerify.setText(getString(R.string.user_achieve_verify_code));
        txtGetVerify.setBackgroundResource(R.drawable.selector_rect_theme_color);
        txtGetVerify.setClickable(true);
    }

    @Override
    public void registerSuccess(UserModel result) {
        //注册成功，设置下用户信息
        MyApplication.getInstance().setUser(result);
        MyApplication.getInstance().setLogin(true);
        //自动登录
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void registerFail(String errMsg) {
        ToastUtil.show(getString(R.string.user_register_fail));
    }

    @Override
    protected void onDestroy() {
        presenter.stopTimer();
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    @Override
    public void submitSuccess() {
        //短信验证成功,去注册
        showLoadingDialog(getString(R.string.com_wait));
        presenter.register(editUsername.getText().toString(), editPassword.getText().toString(), editVerifyCode.getText().toString());
    }

    @Override
    public void smsError() {
        presenter.stopTimer();
    }
}
