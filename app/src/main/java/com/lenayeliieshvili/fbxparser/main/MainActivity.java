package com.lenayeliieshvili.fbxparser.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lenayeliieshvili.fbxparser.R;
import com.lenayeliieshvili.fbxparser.fbx.FbxModel;
import com.lenayeliieshvili.fbxparser.opengl.GestureGlSurfaceView;
import com.lenayeliieshvili.fbxparser.rx.ParseFbxTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private float density;
    private ProgressBar mProgressBar;
    MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this, new ParseFbxTask());

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        density = displayMetrics.density;


        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setIndeterminate(true);

        String filename;
        if (getIntent() != null) {
            filename = getIntent().getStringExtra("file_name");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
                mPresenter.parseFbx(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Can't open file", Toast.LENGTH_SHORT).show();
        }


    }

    public void createRender(FbxModel model) {
        mProgressBar.setVisibility(View.GONE);
        GestureGlSurfaceView glSurfaceView = new GestureGlSurfaceView(this, model);
        glSurfaceView.setDensity(density);
        setContentView(glSurfaceView);
    }

    @Override
    public void showModel(FbxModel model) {
        createRender(model);
    }

    @Override
    public void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}
