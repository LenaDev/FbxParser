package com.lenayeliieshvili.fbxparser.parser;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GeometryIndex {

    private short[] mIndicies;
    private StringBuilder mIndexStr = new StringBuilder();
    private int mDrawMode;
    //// TODO: 5/17/17 try to use int buffer
    private ShortBuffer mIndexBuffer;


    public GeometryIndex() {
    }

    public GeometryIndex(short[] indicies) {
        mIndicies = indicies;
    }

    public void setIndexStr(String str) {
        mIndexStr.append(str);
    }
    public String getIndexStr() {
        return mIndexStr.toString();
    }

    public void convertStringToArray() {
        int index = mIndexStr.indexOf(FbxKeys.ARRAY);
        mIndexStr.delete(index, index + FbxKeys.ARRAY.length());
        String result = mIndexStr.toString().replaceAll(FbxKeys.REGEX_CLEAN, FbxKeys.REPLACE_EMPTY);
       /// result = result.replaceAll("\\}", FbxKeys.REPLACE_EMPTY);
        String[] split = result.split(",");
        mIndicies = new short[split.length];
        try {
            for (int i = 0; i < split.length; i++) {
                mIndicies[i] = Short.parseShort(split[i]);
                if (mIndicies[i] < 0) {
                    if (i == 3) mDrawMode = GL10.GL_TRIANGLE_FAN;
                    else if (i == 2) mDrawMode = GL10.GL_TRIANGLE_STRIP;
                    mIndicies[i] = (short) (Math.abs(mIndicies[i]) - 1);
                }
            }
            initBuffer();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public short[] getIndicies() {
        return mIndicies;
    }

    public void setIndicies(short[] indicies) {
        mIndicies = indicies;
    }

    private void initBuffer() {
        ByteBuffer indexBuffer = ByteBuffer.allocateDirect(Short.SIZE/8 * mIndicies.length);
        indexBuffer.order(ByteOrder.nativeOrder());
        mIndexBuffer = indexBuffer.asShortBuffer();
        mIndexBuffer.put(mIndicies);
        mIndexBuffer.position(0);
    }

    public ShortBuffer getIndexBuffer() {
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
