package com.mumu.realmadrid.view.ball;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mumu.realmadrid.R;
import com.mumu.realmadrid.view.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BallFragment extends BaseFragment {

    @Bind(R.id.image)
    ImageView image;

    public BallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ball, container, false);
        ButterKnife.bind(this, view);
        Glide.with(this).load("http://192.168.1.106:8080/realmadrid/images/test.png").placeholder(R.drawable.bg_banner_2).into(image);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
