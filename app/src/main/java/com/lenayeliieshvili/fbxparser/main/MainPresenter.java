package com.lenayeliieshvili.fbxparser.main;


import com.lenayeliieshvili.fbxparser.fbx.FbxModel;
import com.lenayeliieshvili.fbxparser.rx.ParseFbxTask;
import com.lenayeliieshvili.fbxparser.rx.ParseFbxUseCase;

import java.io.BufferedReader;

import rx.Subscriber;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View mView;
    private ParseFbxUseCase mParseFbxUseCase;

    public MainPresenter(MainContract.View view, ParseFbxTask fbxTask) {
        mView = view;
        mParseFbxUseCase = new ParseFbxUseCase(fbxTask);

    }

    public void parseFbx(BufferedReader reader) {
        mParseFbxUseCase.setReader(reader);
        mParseFbxUseCase.execute(new FbxSubscriber());
    }


    private class FbxSubscriber extends Subscriber<FbxModel> {

        @Override
        public void onStart() {
            if (mView == null) return;
            mView.showProgress(true);
        }

        @Override
        public void onCompleted() {
            if (mView == null) return;
            mView.showProgress(false);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(FbxModel fbxModel) {
            if (mView == null) return;
            mView.showModel(fbxModel);

        }
    }
}
