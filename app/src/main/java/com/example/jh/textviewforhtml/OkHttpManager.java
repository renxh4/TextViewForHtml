package com.example.jh.textviewforhtml;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by jh on 2016/8/12.
 */
public class OkHttpManager {
    private static OkHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Context mContext;
    private static final String TAG = "OkHttpClientManager";
    private Call mCall;
    private static String sLithiumsessionid;
    private static String sLenovosessionid;


    private OkHttpManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        System.out.println("Authenticating for response: " + response);
                        System.out.println("Challenges: " + response.challenges());
                        String credential = Credentials.basic("lenovo", "L3novo");
                        // HTTP授权的授权证书  Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
                        return response
                                .request()
                                .newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }
                })
                .build();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (OkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }

    public Response _getAsyn1(String url) {
        Response mResponse = null;
        final Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            mResponse = mOkHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mResponse;
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _AsynAddheader(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("eSupprot_SessionID", sLenovosessionid)
                .addHeader("lithium_SessionID", sLithiumsessionid)
                .build();
        //Logger.d("mmmheadersLenovosessionid", sLenovosessionid);
        // Logger.d("mmmheadersLithiumsessionid", sLithiumsessionid);
        deliveryResult(callback, request);
    }


    private void _PostJson(String url, final ResultCallback callback, String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("eSupprot_SessionID", sLenovosessionid)
                .addHeader("lithium_SessionID", sLithiumsessionid)
                .post(body)
                .build();
        deliveryResult(callback, request);
    }

    private void _PostForm(String url, final ResultCallback callback, String messageid, String subject, String body, File file, String tag, String isEmail) {
        RequestBody requestBody;
        if (file != null) {
            // File file1 = new File(file.get(0).getPhotoPath());
            String name = file.getName();
            // Logger.d("mmmmfinalfile", "youwenjian" + file.length() + "/");
            MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"MessageId\""),
                            RequestBody.create(null, messageid))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"Subject\""),
                            RequestBody.create(null, subject))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"Body\""),
                            RequestBody.create(null, body))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; filename=" + name),
                            RequestBody.create(MEDIA_TYPE_PNG, file))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"tag\""),
                            RequestBody.create(null, tag))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"isEmail\""),
                            RequestBody.create(null, isEmail))
                    .build();
        } else {
            // Logger.d("mmmmtype", "no type");
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"MessageId\""),
                            RequestBody.create(null, messageid))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"Subject\""),
                            RequestBody.create(null, subject))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"Body\""),
                            RequestBody.create(null, body))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"tag\""),
                            RequestBody.create(null, tag))
                    .addPart(
                            Headers.of("Content-Disposition", "form-data; name=\"isEmail\""),
                            RequestBody.create(null, isEmail))
                    .build();
        }
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("eSupprot_SessionID", sLenovosessionid)
                .addHeader("lithium_SessionID", sLithiumsessionid)
                .post(requestBody)
                .build();
        deliveryResult(callback, request);
    }


    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }


    //*************对外公布的方法************


    public void getAsyn(String url, ResultCallback callback) {
        _getAsyn(url, callback);
    }

    public void addHeader(String url, ResultCallback callback) {
        _AsynAddheader(url, callback);
    }


    public void postAsyn(String url, final ResultCallback callback, Param... params) {
        _postAsyn(url, callback, params);
    }


    public void postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        _postAsyn(url, callback, params);
    }

    public void postJson(String url, final ResultCallback callback, String json) {
        _PostJson(url, callback, json);
    }

    public void postForm(String url, final ResultCallback callback, String messageid, String subject, String body, File file, String tag, String isEmail) {
        _PostForm(url, callback, messageid, subject, body, file, tag, isEmail);
    }
    //****************************


    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();

    private void deliveryResult(final ResultCallback callback, final Request request) {
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(call.request(), e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                int code = response.code();
                Log.d("mmmmresponse", response + "/");
                sendSuccessResultCallback(string, code, callback);
            }
        });

    }

    public void cancel() {
        if (!mCall.isCanceled()) {
            mCall.cancel();
            //Logger.d("mmmmiscancel", mCall.isCanceled() + "/");
        }
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final int code, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    try {
                        callback.onResponse(object, code);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .addHeader("eSupprot_SessionID", sLenovosessionid)
                .addHeader("lithium_SessionID", sLithiumsessionid)
                .post(requestBody)
                .build();
    }


    public static abstract class ResultCallback<T> {

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(Object response, int code) throws IOException;
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

}
