package com.mumu.realmadrid.view.news;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mumu.realmadrid.R;
import com.mumu.realmadrid.api.UserService;
import com.mumu.realmadrid.http.HttpExecute;
import com.mumu.realmadrid.http.HttpRequestParams;
import com.mumu.realmadrid.http.HttpRetrofit;
import com.mumu.realmadrid.http.HttpUrl;
import com.mumu.realmadrid.http.ResponseListener;
import com.mumu.realmadrid.view.BaseFragment;

import java.io.InputStream;

import butterknife.ButterKnife;
import lib.utils.DensityUtil;
import lib.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment {
    private Button button;

    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        button = (Button) view.findViewById(R.id.button);
        /***兼容4.4以上透明状态栏  start****/
//        系统大于4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 44));
            params.topMargin = getStatusBarHeight();
            button.setLayoutParams(params);
        }
        /**end**/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void uploadImg(InputStream is) {
        HttpRequestParams params = new HttpRequestParams();
        params.put("img_file", is, "img.jpg");
        HttpExecute.getInstance().postModel(String.class, HttpUrl.Get_Banner, params, "", new ResponseListener<String>() {
            @Override
            public void onSuccess(String result) {
                dismissLoadingDialog();
                ToastUtil.show("成功");
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                dismissLoadingDialog();
                ToastUtil.show("失败");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
