package com.lenayeliieshvili.fbxparser.parser;


import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class GeometryIndex {

    private int[] mIndicies;
    private StringBuilder mIndexStr = new StringBuilder();
    private int mDrawMode;

    private IntBuffer mIndexBuffer;


    public GeometryIndex() {
    }

    public void setIndexStr(String str) {
        mIndexStr.append(str);
    }

    public void convertStringToArray() {
        int index = mIndexStr.indexOf(FbxKeys.ARRAY);
        mIndexStr.delete(index, index + FbxKeys.ARRAY.length());
        String result = mIndexStr.toString().replaceAll(FbxKeys.REGEX_CLEAN, FbxKeys.REPLACE_EMPTY);
        mIndexStr = null;
        String[] split = result.split(",");
        mIndicies = new int[split.length];
        try {
            for (int i = 0; i < split.length; i++) {
                mIndicies[i] = Integer.parseInt(split[i]);
                if (mIndicies[i] < 0) {
                    if (i == 3) mDrawMode = GLES20.GL_LINES;
                    else if (i == 2) mDrawMode = GLES20.GL_TRIANGLES;
                    mIndicies[i] = (Math.abs(mIndicies[i]) - 1);
                }
            }
            initBuffer();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public int[] getIndicies() {
        return mIndicies;
    }

    public void setIndicies(int[] indicies) {
        mIndicies = indicies;
    }

    private void initBuffer() {
        ByteBuffer indexBuffer = ByteBuffer.allocateDirect(Integer.SIZE/8 * mIndicies.length);
        indexBuffer.order(ByteOrder.nativeOrder());
        mIndexBuffer = indexBuffer.asIntBuffer();
        mIndexBuffer.put(mIndicies);
        mIndexBuffer.position(0);
    }

    public IntBuffer getIndexBuffer() {
        return mIndexBuffer;
    }

    public  int getDrawMode() {
        return mDrawMode;
    }



//    // return drawMode: GL_TRIANGLE_FAN || GL_TRIANGLE_STRIP, if error = -1
//    private int transformIndices() {
//        /*
//        * A FBX file can export three-sided polygons (triangles) or more.
//         * The majority of files export four-sided polygons (quads).
//         * The syntax is similar to that of the vertices, but there is a thing that needs attention:
//            PolygonVertexIndex: i1, i2,-i3, i4, i5,-i6,[…] <—- triangles syntax
//            PolygonVertexIndex: i1, i2, i3, -i4, i5, i6, i7, -i8, […] <—- quads syntax
//            The indices that make up the polygon are in order and a negative
//            index means that it’s the last index of the polygon. That index needs to
//            be made positive and then you have to subtract 1 from it!*/
//        int drawMode = -1;
//        if(mIndicies == null || mIndicies.length == 0) {
//            Log.e(GeometryIndex.class.getSimpleName(), "Can't transform - indicies null or empty");
//        } else {
//            for (int i = 0; i < mIndicies.length; i++) {
//                if (mIndicies[i] < 0) {
//                    if (i == 3) drawMode = GL10.GL_TRIANGLE_FAN;
//                    else if (i == 2) drawMode = GL10.GL_TRIANGLE_STRIP;
//                    mIndicies[i] = (byte) (Math.abs(mIndicies[i]) - 1);
//                }
//            }
//        }
//        return drawMode;
//    }

}
