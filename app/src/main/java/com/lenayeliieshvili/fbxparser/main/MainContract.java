package com.lenayeliieshvili.fbxparser.main;

import com.lenayeliieshvili.fbxparser.fbx.FbxModel;

import java.io.BufferedReader;

interface MainContract {

    interface View {
        void showModel(FbxModel model);

        void showProgress(boolean show);
    }

    interface Presenter {
        void parseFbx(BufferedReader reader);
    }

}
