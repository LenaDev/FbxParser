package com.lenayeliieshvili.fbxparser.menu;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.lenayeliieshvili.fbxparser.R;
import com.lenayeliieshvili.fbxparser.model_loader.ModelActivity;
import com.lenayeliieshvili.fbxparser.samplelist.ListActivity;

import org.andresoviedo.util.android.AndroidURLStreamHandlerFactory;
import org.andresoviedo.util.android.AndroidUtils;
import org.andresoviedo.util.android.AssetUtils;
import org.andresoviedo.util.android.ContentUtils;
import org.andresoviedo.util.android.FileUtils;

import java.io.File;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    private static final String SUPPORTED_FILE_TYPES_REGEX = "(?i).*\\.(obj|stl|dae)";


    static {
        System.setProperty("java.protocol.handler.pkgs", "org.andresoviedo.util.android");
        URL.setURLStreamHandlerFactory(new AndroidURLStreamHandlerFactory());
    }


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
    }

    @OnClick(R.id.btn_fbx)
    public void onFbxClicked() {
        startActivity(ListActivity.getLaunchIntent(this));
    }

    @OnClick(R.id.btn_collada)
    public void onColladaClicked() {
//        Intent intent = new Intent(this, ModelActivity.class);
//        intent.putExtra("uri", "assets:///cowboy.dae");
//        startActivity(intent);
//        Toast.makeText(this, "Collada comming soon", Toast.LENGTH_SHORT).show();

        // check permission starting from android API 23 - Marshmallow
//        if (!AndroidUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE)) {
//            return;
//        }

        AssetUtils.createChooserDialog(this, "Select file", null, "models", "(?i).*\\.(dae)",
                (String file) -> {
                    if (file != null) {
                        ContentUtils.provideAssets(this);
                        launchModelRendererActivity(Uri.parse("assets://" + getPackageName() + "/" + file));
                    }
                });
    }

    private void launchModelRendererActivity(final Uri parse) {
        Log.i("Menu", "Launching renderer for '" + parse + "'");
        Intent intent = new Intent(getApplicationContext(), ModelActivity.class);
        intent.putExtra("uri", parse.toString());
        intent.putExtra("immersiveMode", "true");

        // content provider case
//        if (!loadModelParameters.isEmpty()) {
//            intent.putExtra("type", loadModelParameters.get("type").toString());
//            loadModelParameters.clear();
//        }

        startActivity(intent);
    }

    @OnClick(R.id.btn_obj)
    public void onObjClicked() {
        AssetUtils.createChooserDialog(this, "Select file", null, "models", "(?i).*\\.(obj)",
                (String file) -> {
                    if (file != null) {
                        ContentUtils.provideAssets(this);
                        launchModelRendererActivity(Uri.parse("assets://" + getPackageName() + "/" + file));
                    }
                });
    }

    @OnClick(R.id.btn_stl)
    public void onStlClicked() {
        AssetUtils.createChooserDialog(this, "Select file", null, "models", "(?i).*\\.(stl)",
                (String file) -> {
                    if (file != null) {
                        ContentUtils.provideAssets(this);
                        launchModelRendererActivity(Uri.parse("assets://" + getPackageName() + "/" + file));
                    }
                });
    }

    @OnClick(R.id.btn_storage)
    public void onOpenStorage() {
        FileUtils.createChooserDialog(this, "Select file", null, null, SUPPORTED_FILE_TYPES_REGEX, (File file) -> {
            if (file != null) {
                ContentUtils.setCurrentDir(file.getParentFile());
                launchModelRendererActivity(Uri.parse("file://" + file.getAbsolutePath()));
                } });

    }
}
