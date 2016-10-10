package com.mumu.realmadrid.view.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mumu.realmadrid.R;
import com.mumu.realmadrid.view.BaseFragment;

import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import lib.widget.BannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.banner_view)
    BannerView bannerView;
    @Bind(R.id.listview)
    ListView listView;

    private int[] img = {R.drawable.bg_banner_1, R.drawable.bg_banner_2, R.drawable.bg_banner_3, R.drawable.bg_banner_4};
    private String[] strList = {"皇家马德里", "巴塞罗那", "AC米兰", "马德里竞技", "拜仁慕尼黑", "国际米兰", "曼联", "阿森纳", "尤文图斯","皇家马德里", "巴塞罗那", "AC米兰", "马德里竞技", "拜仁慕尼黑", "国际米兰", "曼联", "阿森纳", "尤文图斯"};

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        /***兼容4.4以上透明状态栏  start****/
        //系统大于4.4
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 44));
//            params.topMargin = getStatusBarHeight();
//            rlayTitleBar.setLayoutParams(params);
//        }
        /**end**/
        ButterKnife.bind(this, view);
        bannerView.setImgData(img);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strList));
        return view;
    }

    @Override
    public void onResume() {
        bannerView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        bannerView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        bannerView.onDestroy();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
