package com.mumu.realmadrid.api.mine;

import com.mumu.realmadrid.model.BaseModel;
import com.mumu.realmadrid.model.member.UserModel;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by 7mu on 2016/10/8.
 */
public interface RegisterService {
    @POST("user/register")
    Observable<BaseModel<UserModel>> register(@QueryMap Map<String, String> map);
}
