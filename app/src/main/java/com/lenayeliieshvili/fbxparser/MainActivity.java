package com.lenayeliieshvili.fbxparser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lenayeliieshvili.fbxparser.parser.FbxModel;
import com.lenayeliieshvili.fbxparser.parser.Geometry;
import com.lenayeliieshvili.fbxparser.parser.GeometryIndex;
import com.lenayeliieshvili.fbxparser.parser.GeometryVertex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.ARRAY;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.GEOMETRY;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.OBJECT_END;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.OBJECT_START;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.POLYGON_VERTEX_INDEX;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.PROPERTY;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.VERTICES;

public class MainActivity extends AppCompatActivity {

    private FbxModel mFbxModel;
    private GestureGlSurfaceView mGlSurfaceView;
    private float density;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        density = displayMetrics.density;


        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setIndeterminate(true);

        String filename;
        if (getIntent() != null) {
            filename = getIntent().getStringExtra("file_name");
            try {
                new ReadFileTask().execute(getAssets().open(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Can't open file", Toast.LENGTH_SHORT).show();
        }


    }

    public void createRender() {
        mProgressBar.setVisibility(View.GONE);
        mGlSurfaceView = new GestureGlSurfaceView(this, mFbxModel);
        mGlSurfaceView.setDensity(density);
        setContentView(mGlSurfaceView);
    }


    public class ReadFileTask extends AsyncTask<InputStream, Integer, FbxModel> {
        @Override
        protected FbxModel doInBackground(InputStream... params) {
            FbxModel fbxModel = new FbxModel();
            String currentObj = "";
            boolean isReadingArray = false;
            BufferedReader reader = new BufferedReader(new InputStreamReader(params[0]));
            String line = "";
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.contains(OBJECT_START)) {
                        if(line.contains(GEOMETRY)) {
                            currentObj = GEOMETRY;
                            fbxModel.addGeometry(new Geometry());
                        }
                        if(line.contains(VERTICES)) {
                            currentObj = VERTICES;
                            fbxModel.getLastAddedGeometry().setVertex(new GeometryVertex());
                        }
                        if(line.contains(POLYGON_VERTEX_INDEX)) {
                            currentObj = POLYGON_VERTEX_INDEX;
                            fbxModel.getLastAddedGeometry().setIndex(new GeometryIndex());
                        }
                    }
                    if (isReadingArray) {
                        if (currentObj.equals(VERTICES)) {
                            isReadingArray = true;
                            fbxModel.getLastAddedGeometry().getVertex().setStrVertices(line);
                        }
                        if (currentObj.equals(POLYGON_VERTEX_INDEX)) {
                            isReadingArray = true;
                            fbxModel.getLastAddedGeometry().getIndex().setIndexStr(line);
                        }
                    }
                    if (line.contains(PROPERTY)) {
                        if (currentObj.equals(VERTICES) && line.contains(ARRAY)) {
                            isReadingArray = true;
                            fbxModel.getLastAddedGeometry().getVertex().setStrVertices(line);
                        }
                        if (currentObj.equals(POLYGON_VERTEX_INDEX) && line.contains(ARRAY)) {
                            isReadingArray = true;
                            fbxModel.getLastAddedGeometry().getIndex().setIndexStr(line);
                        }
                    }
                    if(line.contains(OBJECT_END)) {
                        isReadingArray = false;
                        currentObj = "";
                    }
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Geometry> geometries = fbxModel.getGeometries();
            for (Geometry g : geometries) {
                if (g.getIndex() != null) {
                    g.getIndex().convertStringToArray();
                }
                if (g.getVertex() != null) {
                    g.getVertex().convertStringToArray();
                }
            }

            return fbxModel;
        }

        @Override
        protected void onPostExecute(FbxModel model) {
            mFbxModel = model;
            createRender();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
