package com.mumu.realmadrid.viewmodel.mine;

import com.mumu.realmadrid.http.HttpExecute;
import com.mumu.realmadrid.http.HttpUrl;
import com.mumu.realmadrid.http.HttpRequestParams;
import com.mumu.realmadrid.http.ResponseListener;

/**
 * Created by 7mu on 2016/7/15.
 */
public class UserMsgViewModel {

    public UserMsgViewModel(UserMsgListener listener){
        this.listener = listener;
    }

    public void saveUserMsg(String nickName, String realName, String sexCode, String birthday, String email, String city){
        HttpRequestParams params = new HttpRequestParams();
        params.put("nick_name", nickName);
        params.put("real_name", realName);
        params.put("sex", sexCode);
        params.put("birthday", birthday);
        params.put("email", email);
        params.put("city", city);
        HttpExecute.getInstance().getModel(String.class, HttpUrl.Get_Banner, params, "", new ResponseListener<String>() {
            @Override
            public void onSuccess(String result) {
                if(listener != null)
                    listener.saveSuccess(result);
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                if(listener != null)
                    listener.saveFail(errCode, errMsg);
            }
        });
    }

    private UserMsgListener listener;
    public interface UserMsgListener{
        void saveSuccess(String result);
        void saveFail(int errCode, String errMsg);
    }
}
