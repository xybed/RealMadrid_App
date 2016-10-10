package com.mumu.realmadrid.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mumu.realmadrid.R;
import com.mumu.realmadrid.view.BaseActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.share.ShareSDKLogin;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.edit_username)
    MaterialEditText editUsername;
    @Bind(R.id.edit_password)
    MaterialEditText editPassword;

    private ShareSDKLogin shareSDKLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        transparencyStatusBar();
        ButterKnife.bind(this);
        shareSDKLogin = new ShareSDKLogin(this);
    }


    @OnClick({R.id.btn_left, R.id.sinaweibo, R.id.qq, R.id.weixin, R.id.txt_login, R.id.txt_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.txt_login:
//                SMSSDK.getVerificationCode("86", editUsername.getText().toString());
//                startActivity(new Intent(this, TestTakePhotoActivity.class));
                break;
            case R.id.txt_register:
                startActivity(new Intent(this, RegisterActivity.class));
//                SMSSDK.submitVerificationCode("86", editUsername.getText().toString(), editPassword.getText().toString());
                break;
            case R.id.sinaweibo:
//                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//                shareSDKLogin.authorize(weibo);
                break;
            case R.id.qq:
//                Platform qq = ShareSDK.getPlatform(QQ.NAME);
//                shareSDKLogin.authorize(qq);
                break;
            case R.id.weixin:
//                Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
//                shareSDKLogin.authorize(weixin);
                break;
        }
    }

}
