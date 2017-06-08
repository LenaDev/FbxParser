package com.lenayeliieshvili.fbxparser.fbx;


import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class FbxModel {

    private List<Geometry> mGeometries = new ArrayList<>();

    public void addGeometry(Geometry geometry) {
        mGeometries.add(geometry);
    }

    public Geometry getLastAddedGeometry() {
        return mGeometries.get(mGeometries.size() - 1);
    }

    public List<Geometry> getGeometries() {
        return mGeometries;
    }

    public void draw(GL10 gl, Geometry currentGeometry) {
        if (currentGeometry != null) {
            gl.glFrontFace(GL10.GL_CW);

            if (currentGeometry.getVertex() != null) {
                    gl.glVertexPointer(3, GL10.GL_FLOAT, 3 * 4, currentGeometry.getVertex().getVertexBuffer());
                    gl.glColorPointer(4, GL10.GL_FLOAT, 3 * 4, currentGeometry.getVertex().getColorBuffer());
            }
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            if (currentGeometry.getIndex() != null) {
                gl.glDrawElements(currentGeometry.getIndex().getDrawMode(),
                        currentGeometry.getIndex().getIndicies().length , GLES20.GL_UNSIGNED_INT,
                        currentGeometry.getIndex().getIndexBuffer());
            }
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        } else {
            Log.d(FbxModel.class.getSimpleName(), "No geometries");
        }
    }
}
