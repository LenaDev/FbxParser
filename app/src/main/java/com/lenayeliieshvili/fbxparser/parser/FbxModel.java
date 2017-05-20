package com.lenayeliieshvili.fbxparser.parser;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_UNSIGNED_INT;

public class FbxModel {

    private List<Geometry> mGeometries = new ArrayList<>();

    public void parse() {
    }


    public void addGeometry(Geometry geometry) {
        mGeometries.add(geometry);
    }

    public Geometry getLastAddedGeometry() {
        return mGeometries.get(mGeometries.size() - 1);
    }

    public List<Geometry> getGeometries() {
        return mGeometries;
    }

    public void draw(GL10 gl) {
        if (!mGeometries.isEmpty()) {
            Geometry currentGeometry = mGeometries.get(0);
            gl.glFrontFace(GL10.GL_CW);
             gl.glColor4f(0.8f,0.8f,0.8f, 1f);

            gl.glVertexPointer(3, GL10.GL_FLOAT, 3*4, currentGeometry.getVertex().getVertexBuffer());
            gl.glColorPointer(4, GL10.GL_FLOAT, 3*4, currentGeometry.getVertex().getColorBuffer());

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            gl.glDrawElements(GL10.GL_TRIANGLES,
                    currentGeometry.getIndex().getIndicies().length, GL_UNSIGNED_INT,
                    currentGeometry.getIndex().getIndexBuffer());

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        } else {
            Log.d(FbxModel.class.getSimpleName(), "No geometries");
        }
    }
}
