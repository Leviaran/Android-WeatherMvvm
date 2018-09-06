package com.robotboy.weathermvvm.api.network;

import android.support.annotation.NonNull;

import com.robotboy.weathermvvm.api.response.BaseResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxCallAdapterFactory extends CallAdapter.Factory {
    private final RxJava2CallAdapterFactory original;

    private RxCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter<?, ?> adapter = original.get(returnType, annotations, retrofit);
        return new RxCallAdapterWrapper<>(adapter);
    }

    private static final class RxCallAdapterWrapper<R> implements CallAdapter<R, Observable<R>> {
        private final CallAdapter<R, ?> wrapped;

        RxCallAdapterWrapper(CallAdapter<R, ?> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Observable<R> adapt(final Call<R> call) {
            Observable observable = (Observable) wrapped.adapt(call);

            return observable
                    .onErrorResumeNext(getResumeFunction())
                    .flatMap(getMapFunction());
        }

        @NonNull
        private Function<Throwable, Observable> getResumeFunction() {
            return new Function<Throwable, Observable>() {
                @Override
                public Observable apply(Throwable throwable) {
                    WeatherException exception = WeatherException.create(throwable);
                    return Observable.error(exception);
                }
            };
        }

        @NonNull
        private Function getMapFunction() {
            return new Function() {
                @Override
                public Object apply(Object response) {
                    BaseResponse baseResponse = (BaseResponse) response;
                    if (baseResponse.getSuccess()) {
                        return Observable.just(response);
                    } else {
                        return Observable.error(WeatherException.create(baseResponse.getErrorCode()));
                    }
                }
            };
        }
    }
}
