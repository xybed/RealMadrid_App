package com.mumu.realmadrid.viewmodel.mine;

import com.mumu.realmadrid.IViewModel.mine.IRegisterViewModel;
import com.mumu.realmadrid.api.mine.RegisterService;
import com.mumu.realmadrid.http.HttpRequestParams;
import com.mumu.realmadrid.http.HttpRetrofit;
import com.mumu.realmadrid.http.HttpUrl;
import com.mumu.realmadrid.http.RetroResListener;
import com.mumu.realmadrid.model.member.UserModel;

/**
 * Created by 7mu on 2016/10/8.
 */
public class RegisterViewModel implements IRegisterViewModel {

    public RegisterViewModel(RegisterListener listener){
        this.listener = listener;
    }

    @Override
    public void register(String username, String password, String verifyCode){
        HttpRetrofit httpRetrofit = HttpRetrofit.getInstance();
        HttpRequestParams params = new HttpRequestParams();
        params.put("username", username);
        params.put("password", password);
        params.put("verifyCode", verifyCode);
        httpRetrofit.getModel(httpRetrofit.getApiService(RegisterService.class, HttpUrl.Register, params).register(params.urlParams), UserModel.class.getSimpleName(), new RetroResListener<UserModel>() {
            @Override
            protected void onSuccess(UserModel result) {
                if(listener != null)
                    listener.registerSuccess(result);
            }

            @Override
            protected void onFailure(String errMsg) {
                if(listener != null)
                    listener.registerFail(errMsg);
            }
        });
    }

    private RegisterListener listener;
    public interface RegisterListener{
        void registerSuccess(UserModel result);
        void registerFail(String errMsg);
    }
}
