package com.robotboy.weathermvvm.api.network;

import com.robotboy.weathermvvm.BuildConfig;
import com.robotboy.weathermvvm.misc.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebApi {
    private WebInterface service;

    private WebApi() {
        initConnection();
    }

    public static WebApi getInstance() {
        return new WebApi();
    }

    private void initConnection() {
        HttpLoggingInterceptor loggerInterceptor = new HttpLoggingInterceptor().setLevel(getHttpLogLevel());

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggerInterceptor)
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        CallAdapter.Factory rxAdapter = RxCallAdapterFactory.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getEndPoint())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

        service = retrofit.create(WebInterface.class);
    }

    public WebInterface getService() {
        return service;
    }

    private static String getEndPoint() {
        return Constants.END_POINT;
    }

    private HttpLoggingInterceptor.Level getHttpLogLevel() {
        HttpLoggingInterceptor.Level body;
        if (BuildConfig.DEBUG) {
            body = HttpLoggingInterceptor.Level.BODY;
        } else {
            body = HttpLoggingInterceptor.Level.NONE;
        }
        return body;
    }
}
