package com.mumu.realmadrid.http;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * Created by 7mu on 2016/5/12.
 */
public class HttpHandler extends Handler{
    private ResponseListener responseListener;

    public HttpHandler(ResponseListener responseListener){
        this.responseListener = responseListener;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case HttpExecute.REQ_FAILURE:
                if(responseListener != null)
                    responseListener.onFailure(msg.arg1, (String) msg.obj);
                break;
            case HttpExecute.REQ_SUCCESS:
                if(responseListener != null)
                    responseListener.onSuccess(msg.obj);
                break;
        }
    }
}
