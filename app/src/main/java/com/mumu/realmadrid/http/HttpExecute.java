package com.mumu.realmadrid.http;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.mumu.realmadrid.BuildConfig;
import com.mumu.realmadrid.MyApplication;
import com.mumu.realmadrid.view.mine.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import lib.cache.CacheJsonMgr;
import lib.utils.MD5Util;
import lib.utils.MyLogUtil;
import lib.utils.StreamUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 7mu on 2016/5/12.
 */
public class HttpExecute {
    private static final String TAG = HttpExecute.class.getSimpleName();

    public static final int ERR_CODE_BEAN = -10005;
    public static final int ERR_CODE_JSON_PARSE = -10004;
    public static final int ERR_CODE_NET = -10000;
    public static final int ERR_CODE_UNKNOWN = -10001;
    public static final int ERR_CODE_TIMEOUT = -10002;
    public static final int ERR_CODE_TOKEN = -99;
    public static final int REQ_SUCCESS = 0;
    public static final int REQ_FAILURE = -1;
    public static final int ERR_CODE_JSON_UNKNOW = -10006;

    private static HttpExecute httpExecute;
    private Context context;
    private OkHttpClient okHttpClient;
    //缓存管理
    private CacheJsonMgr cacheJsonMgr;

    private HttpExecute(Context context){
        this.context = context;
        okHttpClient = new OkHttpClient();
        cacheJsonMgr = CacheJsonMgr.getInstance(context);
    }

    public static HttpExecute getInstance(){
        if(httpExecute == null)
            httpExecute = new HttpExecute(MyApplication.getInstance());
        return httpExecute;
    }

    public <T> Request getModel(Class<T> clazz, String url, HttpRequestParams params, String cacheKey, ResponseListener<T> responseListener){
        return request(1, clazz, url, params, cacheKey, responseListener);
    }

    public <T> Request getList(Class<T> clazz, String url, HttpRequestParams params, String cacheKey, ResponseListener<List<T>> responseListener){
        return request(1, clazz, url, params, cacheKey, responseListener);
    }

    public <T> Request postModel(Class<T> clazz, String url, HttpRequestParams params, String cacheKey, ResponseListener<T> responseListener){
        return request(2, clazz, url, params, cacheKey, responseListener);
    }

    public <T> Request postList(Class<T> clazz, String url, HttpRequestParams params, String cacheKey, ResponseListener<List<T>> responseListener){
        return request(2, clazz, url, params, cacheKey, responseListener);
    }

    private <T> Request request(int method, Class<T> clazz, String url, HttpRequestParams params, String cacheKey, ResponseListener responseListener){
        url = addBaseParams(url, params);
        if(method == 1){
            //get请求
            String paramString = "";
            for(ConcurrentHashMap.Entry<String, String> entry : params.urlParams.entrySet()){
                paramString = paramString + "&" + entry.getKey() + "=" + entry.getValue();
            }
            if(!paramString.equals("")){
                //如果包含？说明url中可以直接添加参数：此时如果包含了&说明url中已经存在参数，则再添加新的参数时候需要添加一个&
                url += url.contains("?") ? (url.contains("&") ? "&" : "") : "?";
                //为了标准化url的格式，把paramString的第一个“&”去掉,如果url的最后一个字符为&，则应该去掉；如果为?则也应该去掉
                paramString = paramString.substring(1);
                url += paramString;
            }
            Request request = new Request.Builder().url(url).build();
            executeRequest(clazz, request, new HttpHandler(responseListener), cacheKey);
            MyLogUtil.e(TAG, url);
            return request;
        }else{
            //post请求
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);//为请求成功而加的参数(之前研究了好久)
            for(ConcurrentHashMap.Entry<String, String> entry : params.urlParams.entrySet()){
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
            for(ConcurrentHashMap.Entry<String, HttpRequestParams.StreamWrapper> entry : params.streamParams.entrySet()){
                byte[] bytes = StreamUtil.inputStream2Byte(entry.getValue().inputStream);
                builder.addFormDataPart(entry.getKey(), entry.getValue().name, RequestBody.create(MediaType.parse(entry.getValue().contentType), bytes));
            }
            for(ConcurrentHashMap.Entry<String, HttpRequestParams.FileWrapper> entry : params.fileParams.entrySet()){
                builder.addFormDataPart(entry.getKey(), entry.getValue().customFileName, RequestBody.create(MediaType.parse(entry.getValue().contentType), entry.getValue().file));
            }
            Request request = new Request.Builder().url(url).post(builder.build()).build();
            executeRequest(clazz, request, new HttpHandler(responseListener), cacheKey);
            MyLogUtil.e(TAG, url + (url.contains("?") ? "" : "?") + params);
            return request;
        }
    }

    public void cancelRequest(Request request){
        okHttpClient.newCall(request).cancel();
    }

    private String addBaseParams(String url, HttpRequestParams params){
        //添加必要请求参数
        params.put("ver", BuildConfig.DEVELOP_VERSION);
        params.put("platform", "android");
        if(MyApplication.getInstance().isLogin()){
            params.put("token", MyApplication.getInstance().getUser().getToken());
        }else{
            params.put("token", "");
        }
        params.put("sign", MD5Util.createParamSign(params.urlParams, HttpUrl.TOKEN_KEY));
        return (url.startsWith("http") ? "" : HttpUrl.BASE_API_URL) + url;
    }

    private <T> void executeRequest(final Class<T> clazz, Request request, final HttpHandler httpHandler, final String cacheKey){
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailureMsg(httpHandler, ERR_CODE_NET);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string().replace("\r\n", "");
                MyLogUtil.e(TAG, responseString);
                handleSuccessHttpResponse(clazz, httpHandler, responseString, cacheKey);
            }
        });
    }

    private <T> void handleSuccessHttpResponse(Class<T> clazz, HttpHandler httpHandler, String responseString, String cacheKey){
        /*
        1.如果返回空，则处理未知错误
        2.用JSONObject解析json,{"DataType":0,"Return":0,"Detail":"","Data":...}
        3.若return不为0，代表请求不成功。调用失败方法，把detail里的msg传入
        4.return为0，请求成功，拿到Data里的数据，不为空的话
            --若为[开头，则是数组
            --若为{开头，则是对象
            --其他则是字符串，对应解析出json，转换成数据类型
            调用成功处理的方法
        5.若有缓存，则处理
        6.若返回的数据为null或"null"，调用成功处理方法的时候传入null
        7.若抛json解析异常，则errCode为ERR_CODE_BEAN
        8.其他异常，则errCode为ERR_CODE_JSON_PARSE
         */
        if(TextUtils.isEmpty(responseString)){
            sendFailureMsg(httpHandler, ERR_CODE_UNKNOWN, "response is nothing");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            int mReturn = Integer.parseInt(jsonObject.getString("return"));
            if(mReturn != REQ_SUCCESS){
                sendFailureMsg(httpHandler, mReturn, jsonObject.getString("detail"));
                return;
            }
            Object obj = null;
            String data = jsonObject.getString("data");
            if(data != null && !"null".equals(data)){
                if(data.startsWith("[")){
                    obj = JSON.parseArray(data, clazz);
                }else if(data.startsWith("{")){
                    obj = JSON.parseObject(data, clazz, Feature.IgnoreNotMatch, Feature.InitStringFieldAsEmpty);
                }else{
                    obj = data;
                }
            }
            sendSuccessMsg(httpHandler, obj);
            if (!TextUtils.isEmpty(cacheKey)) {
                cacheJsonMgr.saveJson(data, cacheKey);
            }
        } catch (JSONException e) {
            sendFailureMsg(httpHandler, ERR_CODE_BEAN);
        } catch (Exception e){
            sendFailureMsg(httpHandler, ERR_CODE_JSON_PARSE);
        }
    }

    private void sendFailureMsg(HttpHandler httpHandler, int errCode){
        String errMsg = "未知错误";
        switch (errCode){
            case ERR_CODE_BEAN:
                errMsg = "JSON格式不符";
                break;
            case ERR_CODE_JSON_PARSE:
                errMsg = "JSON解析异常";
                break;
            case ERR_CODE_NET:
                errMsg = "网络异常";
                break;
            case ERR_CODE_UNKNOWN:
                errMsg = "未知错误";
                break;
            case ERR_CODE_TIMEOUT:
                errMsg = "连接超时";
                break;
            case ERR_CODE_TOKEN:
                errMsg = "未登录";
                break;
            default:
                break;
        }
        sendFailureMsg(httpHandler, errCode, errMsg);
    }

    private Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    };

    private void sendFailureMsg(HttpHandler httpHandler, int errCode, String errMsg){
        if(errCode == ERR_CODE_TOKEN){
            Message message = new Message();
            loginHandler.sendMessageDelayed(message, 300);
        }

        MyLogUtil.e(TAG, "errCode=" + errCode + ", errMsg=" + errMsg);

        Message message = httpHandler.obtainMessage();
        message.what = REQ_FAILURE;
        message.arg1 = errCode;
        message.obj = errMsg;
        httpHandler.sendMessage(message);
    }

    private <T> void sendSuccessMsg(HttpHandler httpHandler, T obj){
        Message message = httpHandler.obtainMessage();
        message.what = REQ_SUCCESS;
        message.obj = obj;
        httpHandler.sendMessage(message);
    }
}
