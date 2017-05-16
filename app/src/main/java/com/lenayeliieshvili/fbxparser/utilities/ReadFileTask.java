package com.lenayeliieshvili.fbxparser.utilities;

import android.os.AsyncTask;

import com.lenayeliieshvili.fbxparser.parser.FbxModel;
import com.lenayeliieshvili.fbxparser.parser.Geometry;
import com.lenayeliieshvili.fbxparser.parser.GeometryIndex;
import com.lenayeliieshvili.fbxparser.parser.GeometryVertex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.ARRAY;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.GEOMETRY;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.OBJECT_END;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.OBJECT_START;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.POLYGON_VERTEX_INDEX;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.PROPERTY;
import static com.lenayeliieshvili.fbxparser.parser.FbxKeys.VERTICES;

/**
 * Created by lenayeliieshvili on 09.05.17.
 */

public class ReadFileTask extends AsyncTask<InputStream, Integer, FbxModel> {
    @Override
    protected FbxModel doInBackground(InputStream... params) {
        FbxModel fbxModel = new FbxModel();
        String currentObj = "";
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
                if (!currentObj.isEmpty() && line.contains(PROPERTY)) {
                    if (currentObj.equals(VERTICES) && line.contains(ARRAY)) {
                        fbxModel.getLastAddedGeometry().getVertex().setStrVertices(line);
                    }
                    if (currentObj.equals(POLYGON_VERTEX_INDEX) && line.contains(ARRAY)) {
                        fbxModel.getLastAddedGeometry().getIndex().setIndexStr(line);
                    }
                }
                if(line.contains(OBJECT_END)) {
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
    protected void onPostExecute(FbxModel s) {
        super.onPostExecute(s);
    }
}
