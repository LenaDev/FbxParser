package com.lenayeliieshvili.fbxparser.rx;


import java.io.BufferedReader;

import rx.Observable;

public class ParseFbxUseCase extends BackgroundUseCase {

    private final ParseFbxTask mParseFbxTask;
    private BufferedReader mReader;

    public ParseFbxUseCase(ParseFbxTask parseFbxTask) {
        mParseFbxTask = parseFbxTask;
    }

    public void setReader(BufferedReader reader) {
        mReader = reader;
    }

    @Override
    protected Observable buildObservableTask() {
        return mParseFbxTask.parseFbxFile(mReader);
    }
}
