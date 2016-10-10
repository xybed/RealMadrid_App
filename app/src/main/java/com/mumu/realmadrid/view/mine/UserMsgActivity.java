package com.mumu.realmadrid.view.mine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mumu.realmadrid.R;
import com.mumu.realmadrid.view.BaseActivity;
import com.mumu.realmadrid.viewmodel.mine.UserMsgViewModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.utils.DatePickUtil;
import lib.utils.PhotoUtil;
import lib.widget.ActionSheet;

public class UserMsgActivity extends BaseActivity implements UserMsgViewModel.UserMsgListener{

    @Bind(R.id.img_avatar)
    ImageView imgAvatar;
    @Bind(R.id.edit_nick_name)
    EditText editNickName;
    @Bind(R.id.edit_real_name)
    EditText editRealName;
    @Bind(R.id.txt_sex)
    TextView txtSex;
    @Bind(R.id.txt_birthday)
    TextView txtBirthday;
    @Bind(R.id.edit_email)
    EditText editEmail;
    @Bind(R.id.edit_city)
    EditText editCity;

    private UserMsgViewModel presentationModel;

    private String sexCode;//要传给后台的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);

        presentationModel = new UserMsgViewModel(this);
        initUI();
    }

    private void initUI(){
        ButterKnife.bind(this);
        Glide.with(this).load("").placeholder(R.drawable.icon_default_avatar).into(imgAvatar);
    }

    @OnClick({R.id.btn_left, R.id.txt_save, R.id.img_avatar, R.id.llay_sex, R.id.llay_birthday})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.txt_save://保存
                presentationModel.saveUserMsg(editNickName.getText().toString(), editRealName.getText().toString(),
                        sexCode, txtBirthday.getText().toString(), editEmail.getText().toString(), editCity.getText().toString());
                break;
            case R.id.img_avatar://更换头像
                changeAvatar();
                break;
            case R.id.llay_sex://性别
                changeSex();
                break;
            case R.id.llay_birthday://生日
                changeBirthday();
                break;
        }
    }

    private void changeAvatar(){
        ActionSheet.createBuilder(this)
                .setCancelableOnTouchOutside(true)
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拍照", "从手机相册中选择")
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onOtherButtonClick(int index) {
                        switch (index){
                            case 0:
                                PhotoUtil.takePhoto(UserMsgActivity.this);
                                break;
                            case 1:
                                PhotoUtil.selectPhoto(UserMsgActivity.this);
                                break;
                        }
                    }
                }).show();
    }

    private void changeSex(){
        ActionSheet.createBuilder(this)
                .setCancelableOnTouchOutside(true)
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("保密", "男", "女")
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onOtherButtonClick(int index) {
                        switch (index){
                            case 0:
                                sexCode = "0";
                                txtSex.setText("保密");
                                break;
                            case 1:
                                sexCode = "1";
                                txtSex.setText("男");
                                break;
                            case 2:
                                sexCode = "2";
                                txtSex.setText("女");
                                break;
                        }
                    }
                }).show();
    }

    private void changeBirthday(){
        DatePickUtil.datePicker(this, txtBirthday, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtil.onActivityResult(this, requestCode, data, new PhotoUtil.PhotoResultListener() {
            @Override
            public void photoResultSuccess(String path) {

            }

            @Override
            public void photoResultFail(String errMsg) {

            }
        });
    }

    @Override
    public void saveSuccess(String result) {

    }

    @Override
    public void saveFail(int errCode, String errMsg) {

    }
}
