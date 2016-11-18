package com.mumu.realmadrid.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mumu.realmadrid.MyApplication;
import com.mumu.realmadrid.R;
import com.mumu.realmadrid.view.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lib.glide.GlideCircleTransform;
import lib.utils.ScreenUtil;
import lib.zxing.CaptureActivity;

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
            txtUser.setText(getString(R.string.user_login));
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
                break;
            case R.id.llay_mine_love:
                break;
            case R.id.llay_mine_task:
                break;
            case R.id.llay_mine_opinion:
                break;
            case R.id.llay_mine_aboutus:
                //暂时写跳转二维码
                intent = new Intent(getActivity(), CaptureActivity.class);
                getActivity().startActivityForResult(intent, 100);
                break;
            case R.id.llay_mine_set:
                break;
        }
    }
}
