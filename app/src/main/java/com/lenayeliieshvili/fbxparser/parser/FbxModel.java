package com.lenayeliieshvili.fbxparser.parser;


import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class FbxModel {

    private List<Geometry> mGeometries = new ArrayList<>();

    public void parse() {
    }


    public void addGeometry(Geometry geometry) {
        mGeometries.add(geometry);
    }

    public void addGeometries(List<Geometry> geometries) {
        mGeometries.clear();
        mGeometries.addAll(geometries);
    }

    public Geometry getLastAddedGeometry() {
        return mGeometries.get(mGeometries.size() - 1);
    }

    public List<Geometry> getGeometries() {
        return mGeometries;
    }

    public void draw(GL10 gl) {
        Geometry currentGeometry = mGeometries.get(0);
        gl.glFrontFace(GL10.GL_CW);
        gl.glColor4f(1,0,1,1);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 3*4, currentGeometry.getVertex().getVertexBuffer());
       // gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
       // gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDrawElements(currentGeometry.getIndex().getDrawMode(),
                currentGeometry.getIndex().getIndicies().length, GL10.GL_UNSIGNED_SHORT,
                currentGeometry.getIndex().getIndexBuffer());

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
      //  gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}
