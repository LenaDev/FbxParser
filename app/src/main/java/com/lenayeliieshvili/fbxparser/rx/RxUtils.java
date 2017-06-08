package com.lenayeliieshvili.fbxparser.rx;


import android.os.Looper;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxUtils {

    public static Scheduler getBackgroundScheduler() {
        //com.lenayeliieshvili.fbxparser.main thread
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            return Schedulers.io();
        } else {
            //we are already in bg thread
            return Schedulers.immediate();
        }
    }

    static final Observable.Transformer asyncTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Observable)o).subscribeOn(getBackgroundScheduler())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    static final Observable.Transformer syncTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Observable)o).subscribeOn(Schedulers.immediate())
                    .observeOn(Schedulers.immediate());
        }
    };

    static final Observable.Transformer mainThreadTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Observable)o).subscribeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applyAsyncSchedulers() {
        return (Observable.Transformer<T, T>) asyncTransformer;
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applySyncSchedulers() {
        return (Observable.Transformer<T, T>) syncTransformer;
    }

    public static <T> Observable.Transformer<T, T> applyMainThreadScheduler() {
        return (Observable.Transformer<T, T>) mainThreadTransformer;
    }
}
