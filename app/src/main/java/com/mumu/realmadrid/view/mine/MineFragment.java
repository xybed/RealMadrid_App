package com.mumu.realmadrid.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mumu.realmadrid.MyApplication;
import com.mumu.realmadrid.R;
import com.mumu.realmadrid.api.UserService;
import com.mumu.realmadrid.http.HttpRetrofit;
import com.mumu.realmadrid.http.HttpRequestParams;
import com.mumu.realmadrid.http.RetroResListener;
import com.mumu.realmadrid.model.User;
import com.mumu.realmadrid.view.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.glide.GlideCircleTransform;
import lib.utils.ScreenUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {

    @Bind(R.id.img_avatar)
    ImageView imgAvatar;
    @Bind(R.id.txt_user)
    TextView txtUser;
    @Bind(R.id.llay_top_img)
    LinearLayout llayTopImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        ButterKnife.bind(this, view);
        initBanner();
        return view;
    }

    private void initBanner() {
        ScreenUtil.measureViewByImg(getActivity(), llayTopImg, 1.8);
        if(!MyApplication.getInstance().isLogin()){
            Glide.with(this).load(R.drawable.icon_default_avatar)
                    .transform(new GlideCircleTransform(getActivity()))
                    .into(imgAvatar);
            txtUser.setText("登录");
        }else {
            Glide.with(this).load(MyApplication.getInstance().getUser().getAvatar())
                    .placeholder(R.drawable.icon_default_avatar)
                    .transform(new GlideCircleTransform(getActivity()))
                    .into(imgAvatar);
            txtUser.setText(MyApplication.getInstance().getUser().getNickname());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.llay_top_img, R.id.llay_mine_ball, R.id.llay_mine_love, R.id.llay_mine_task, R.id.llay_mine_opinion, R.id.llay_mine_aboutus, R.id.llay_mine_set})
    public void onClick(View view) {
        Intent intent;
        HttpRequestParams params;
        HttpRetrofit httpRetrofit = HttpRetrofit.getInstance();
        switch (view.getId()) {
            case R.id.llay_top_img:
                if(!MyApplication.getInstance().isLogin()){
                    intent = new Intent(getActivity(), LoginActivity.class);
                }else {
                    intent = new Intent(getActivity(), UserMsgActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.llay_mine_ball:
                params = new HttpRequestParams();
                params.put("id", "1");
                httpRetrofit.getList(httpRetrofit.getApiService(UserService.class, "user/showUser", params).getList(params.urlParams), "", new RetroResListener<List<User>>() {
                    @Override
                    protected void onSuccess(List<User> result) {
                        for(User user : result){
                            Log.e("http", user.getUsername() + "::" +user.getPassword());
                        }
                    }

                    @Override
                    protected void onFailure(String errMsg) {
                        Log.e("http", errMsg);
                    }
                });
                break;
            case R.id.llay_mine_love:
                params = new HttpRequestParams();
                params.put("id", "1");
                httpRetrofit.getModel(httpRetrofit.getApiService(UserService.class, "user/showUser", params).getUser(params.urlParams), "", new RetroResListener<User>() {
                    @Override
                    protected void onSuccess(User result) {
                        Log.e("http", result.getUsername() + "::" +result.getPassword());
                    }

                    @Override
                    protected void onFailure(String errMsg) {
                        Log.e("http", errMsg);
                    }
                });
                break;
            case R.id.llay_mine_task:
                break;
            case R.id.llay_mine_opinion:
                break;
            case R.id.llay_mine_aboutus:
                break;
            case R.id.llay_mine_set:
                break;
        }
    }
}
