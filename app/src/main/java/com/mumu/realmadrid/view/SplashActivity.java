package com.mumu.realmadrid.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mumu.realmadrid.BuildConfig;
import com.mumu.realmadrid.MyApplication;
import com.mumu.realmadrid.R;

import java.util.Timer;
import java.util.TimerTask;

import lib.baidu.Analyse;

public class SplashActivity extends BaseActivity {

    private int delay = 3 * 1000;
    private Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            timer.cancel();
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //百度统计的初始化，放在第一个启动的activity的onCreate方法中
        Analyse.init(MyApplication.getInstance());
        transparencyStatusBar();
        goMain();
    }

    private void goMain(){
        if(BuildConfig.DEBUG)
            delay = 1500;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, delay);
    }
}
