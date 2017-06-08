package com.lenayeliieshvili.fbxparser.rx;


import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public abstract class BackgroundUseCase extends UseCase {
    @Override
    public Subscription execute(Subscriber observableTaskSubscriber) {
        final Observable requestObservable;
        if ((requestObservable = buildObservableTask()) == null)
            throw new RuntimeException("Error observable request not declared");
        return requestObservable
                .compose(RxUtils.applyAsyncSchedulers())
                .subscribe(observableTaskSubscriber);

    }
}
