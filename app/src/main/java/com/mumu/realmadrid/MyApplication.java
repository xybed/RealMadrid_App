package com.mumu.realmadrid;

import android.support.multidex.MultiDexApplication;

import com.mumu.realmadrid.model.member.UserModel;

import cn.jpush.android.api.JPushInterface;
import lib.baidu.MyLocation;
import lib.cache.CacheJsonMgr;
import lib.crash.MyCrashHandler;

/**
 * Created by 7mu on 2016/4/22.
 */
public class MyApplication extends MultiDexApplication{
    private static MyApplication myApplication;

    private UserModel user;
    private boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        MyCrashHandler.getInstance().init(this);
        //获取位置信息
        MyLocation.getInstance().requestLocation();
        //初始化极光推送
        initJPush();
        //获取用户登录的信息
        initUserLoginInfo();
    }

    private void initJPush(){
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
    }

    /**
     * 获取用户登录的信息
     */
    private void initUserLoginInfo(){
        //读取用户登陆内容
        Object object = CacheJsonMgr.getInstance(this).getJsonObject(UserModel.class);
        if (object != null) {
            setUser((UserModel) object);
            setLogin(true);
        }
    }

    public boolean isLogin() {
        return isLogin;
    }
    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
    public UserModel getUser() {
        return user;
    }
    public void setUser(UserModel user) {
        this.user = user;
    }
    public static MyApplication getInstance(){
        return myApplication;
    }
}
