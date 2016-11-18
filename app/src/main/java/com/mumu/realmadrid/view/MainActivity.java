package com.mumu.realmadrid.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.mumu.realmadrid.R;
import com.mumu.realmadrid.http.HttpExecute;
import com.mumu.realmadrid.http.HttpUrl;
import com.mumu.realmadrid.http.HttpRequestParams;
import com.mumu.realmadrid.http.ResponseListener;
import com.mumu.realmadrid.view.ball.BallFragment;
import com.mumu.realmadrid.view.home.HomeFragment;
import com.mumu.realmadrid.view.mine.MineFragment;
import com.mumu.realmadrid.view.news.NewsFragment;

import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import lib.utils.PhotoUtil;
import lib.utils.StreamUtil;
import lib.utils.ToastUtil;
import lib.zxing.CaptureActivity;
import lib.zxing.ScanResultActivity;

public class MainActivity extends BaseActivity {

    @Bind(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    private Fragment content;
    private Fragment homeFragment;
    private Fragment ballFragment;
    private Fragment newsFragment;
    private Fragment mineFragment;

    private long firstTime;
    private long secondTime;
    private long spaceTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initFragment();
        initBottomNavigationBar();
    }

    private void initFragment() {
        content = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, content).commit();
        transparencyStatusBar();
    }

    private void initBottomNavigationBar(){
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setActiveColor(R.color.theme_color_white);
        bottomNavigationBar.setInActiveColor(R.color.theme_color_white);
        bottomNavigationBar.setBarBackgroundColor(R.color.theme_color);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_home_tab, "皇"))
                .addItem(new BottomNavigationItem(R.drawable.icon_ball_tab, "马"))
                .addItem(new BottomNavigationItem(R.drawable.icon_news_tab, "必"))
                .addItem(new BottomNavigationItem(R.drawable.icon_mine_tab, "胜"))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switchFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private Fragment getHomeFragment() {
        if (homeFragment == null)
            homeFragment = new HomeFragment();
        return homeFragment;
    }

    private Fragment getBallFragment() {
        if (ballFragment == null)
            ballFragment = new BallFragment();
        return ballFragment;
    }

    private Fragment getNewsFragment() {
        if (newsFragment == null)
            newsFragment = new NewsFragment();
        return newsFragment;
    }

    private Fragment getMineFragment() {
        if (mineFragment == null)
            mineFragment = new MineFragment();
        return mineFragment;
    }

    private void switchFragment(int position) {
        switch (position) {
            case 0:
                switchFragment(content, getHomeFragment());
                transparencyStatusBar();
                break;
            case 1:
                switchFragment(content, getBallFragment());
                transparencyStatusBar(false);
                break;
            case 2:
                switchFragment(content, getNewsFragment());
                transparencyStatusBar(false);
                break;
            case 3:
                switchFragment(content, getMineFragment());
                transparencyStatusBar();
                break;
        }
    }

    /**
     * 切换fragment
     *
     * @param from
     * @param to
     */
    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            content = to;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {//如果已经加过了就显示，这样的做法更有效率
                fragmentTransaction.hide(from).add(R.id.content, to).commit();
            } else {
                fragmentTransaction.hide(from).show(to).commit();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            firstTime = System.currentTimeMillis();
            spaceTime = firstTime - secondTime;
            secondTime = firstTime;
            if (spaceTime > 2000) {
                ToastUtil.show("再按一次退出程序");
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("http", ""+requestCode);
        Log.e("http", ""+resultCode);
        if (requestCode == 100) {
            Bundle bundle2 = data.getExtras();
            if (null != bundle2 &&
                    bundle2.containsKey(CaptureActivity.Params_RetState) &&
                    bundle2.containsKey(CaptureActivity.Params_RetCode)) {
                boolean isSuc = bundle2.getBoolean(CaptureActivity.Params_RetState);
                String code = bundle2.getString(CaptureActivity.Params_RetCode);
                dealScanResult(isSuc, code);
            }
        }
        PhotoUtil.onActivityResult(this, requestCode, data, new PhotoUtil.PhotoResultListener() {
            @Override
            public void photoResultSuccess(String path) {
                Log.e("photo", path);
                InputStream is = StreamUtil.bitmap2InputStream(PhotoUtil.file2Bitmap(path));
                Log.e("photo", "转成inputstream");
                showLoadingDialog("正在上传");
                uploadImg(is);
            }

            @Override
            public void photoResultFail(String errMsg) {
                ToastUtil.show(errMsg);
            }
        });
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

    private void dealScanResult(boolean isSuc, String val) {
        if (!isSuc) return;
//        if (null != val && val.length() > Common.MEQR_Head.length() &&
//                val.startsWith(Common.MEQR_Head)) {
//            String sID = val.substring(Common.MEQR_Head.length(), val.length());
//            long uID = Methods.ParseToLong(sID, 0L);
//            if (uID > 0) {
//                showUserDetailActivity(uID);
//                return;
//            }
//        }
//        if (null != val && val.length() > Common.PAYQR_Head.length() &&
//                val.startsWith(Common.PAYQR_Head)) {
//            String sID = val.substring(Common.PAYQR_Head.length(), val.length());
//            int uID = Methods.ParseToInt(sID, 0);
//            if (uID > 0) {
//                showScanToPayActivity(uID);
//                return;
//            }
//        }
        ShowScanResultActivity(val);
    }
    private void ShowScanResultActivity(String val) {
        Intent intent = new Intent(this, ScanResultActivity.class);
        intent.putExtra(ScanResultActivity.Params_Val, val);
        startActivity(intent);
    }
}
