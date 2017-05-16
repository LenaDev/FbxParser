package com.lenayeliieshvili.fbxparser.parser;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GeometryVertex {

    private float[] mVertices;

    private StringBuilder mVerticesStr = new StringBuilder();
    private FloatBuffer mVertexBuffer;

    public GeometryVertex() {

    }

    public GeometryVertex(float[] vertices) {
        mVertices = vertices;
    }

    public float[] getVertices() {
        return mVertices;
    }

    public void setVertices(float[] vertices) {
        mVertices = vertices;
    }

    public void setStrVertices(String str) {
        mVerticesStr.append(str);
    }

    public String getStrVertex() {
        return mVerticesStr.toString();
    }

    public void convertStringToArray() {
        int index = mVerticesStr.indexOf(FbxKeys.ARRAY);
        mVerticesStr.delete(index, index + FbxKeys.ARRAY.length());
        String result = mVerticesStr.toString().replaceAll(FbxKeys.REGEX_CLEAN, FbxKeys.REPLACE_EMPTY);
        String[] split = result.split(",");
        mVertices = new float[split.length];
        try {
            for (int i = 0; i < split.length; i++) {
                mVertices[i] = Float.parseFloat(split[i]);
            }
            initBuffer();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void initBuffer() {
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(Float.SIZE/8 * mVertices.length);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = vertexByteBuffer.asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mVertexBuffer.position(0);
    }

    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }
}
