package com.lenayeliieshvili.fbxparser;

import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.lenayeliieshvili.fbxparser.parser.FbxModel;
import com.lenayeliieshvili.fbxparser.parser.Geometry;
import com.lenayeliieshvili.fbxparser.parser.GeometryIndex;
import com.lenayeliieshvili.fbxparser.parser.GeometryVertex;
import com.lenayeliieshvili.fbxparser.render.CustomRender;

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

    //// TODO: 5/17/17 add gesture listeners
    private FbxModel mFbxModel;
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)

        try {
            new ReadFileTask().execute(getAssets().open("cube.fbx"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void createRender() {
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setRenderer(new CustomRender(mFbxModel));
        setContentView(glSurfaceView);

    }

    public class ReadFileTask extends AsyncTask<InputStream, Integer, FbxModel> {
        @Override
        protected FbxModel doInBackground(InputStream... params) {
            FbxModel fbxModel = new FbxModel();
            String currentObj = "";
            boolean isReadingArray = false;
            StringBuilder stringBuilder = new StringBuilder();
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
                    stringBuilder.append(line);
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return fbxModel;
        }

        @Override
        protected void onPostExecute(FbxModel model) {
            mFbxModel = model;
            List<Geometry> geometries = model.getGeometries();
            for (Geometry g : geometries) {
                if(g.getIndex() != null)  {
                    g.getIndex().convertStringToArray();
                }
                if (g.getVertex() != null) {
                    g.getVertex().convertStringToArray();
                }
            }
            createRender();
        }
    }
}
