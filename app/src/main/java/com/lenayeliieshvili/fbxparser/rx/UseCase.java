package com.lenayeliieshvili.fbxparser.rx;


import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public abstract class UseCase<T extends Subscriber> {

    protected abstract Observable buildObservableTask();

    public Subscription execute(final T observableTaskSubscriber) {
        return buildObservableTask().subscribe(observableTaskSubscriber);
    }
}
