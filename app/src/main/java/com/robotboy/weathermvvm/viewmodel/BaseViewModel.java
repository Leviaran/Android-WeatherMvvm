package com.robotboy.weathermvvm.viewmodel;

import com.robotboy.weathermvvm.api.response.BaseResponse;
import com.robotboy.weathermvvm.misc.Constants;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseViewModel {

    protected <T extends BaseResponse> Observable<T> standartRequest(Observable<T> observable) {
        return observable
                .timeout(Constants.REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
