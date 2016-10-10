package com.mumu.realmadrid.api;

import com.mumu.realmadrid.model.BaseModel;
import com.mumu.realmadrid.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by 7mu on 2016/8/24.
 * 用于测试retrofit所用，之后可删除
 */
public interface UserService {
    @GET("user/showUser")
    Observable<BaseModel<List<User>>> getList(@QueryMap Map<String, String> map);
    @GET("user/showUser")
    Observable<BaseModel<User>> getUser(@QueryMap Map<String, String> map);
}
