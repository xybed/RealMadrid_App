package com.mumu.realmadrid.viewmodel.news;

import com.mumu.realmadrid.http.HttpExecute;
import com.mumu.realmadrid.http.HttpUrl;
import com.mumu.realmadrid.http.HttpRequestParams;
import com.mumu.realmadrid.http.ResponseListener;

/**
 * Created by 7mu on 2016/7/12.
 */
public class NewsViewModel {
    public NewsViewModel(NewsListener listener){
        this.listener = listener;
    }

    public void uploadImg(){
        HttpRequestParams params = new HttpRequestParams();
        HttpExecute.getInstance().postModel(String.class, HttpUrl.Get_Banner, params, "", new ResponseListener<String>() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
            }
        });
    }

    private NewsListener listener;
    public interface NewsListener{
        void uploadSuccess();
        void uploadFail();
    }
}
