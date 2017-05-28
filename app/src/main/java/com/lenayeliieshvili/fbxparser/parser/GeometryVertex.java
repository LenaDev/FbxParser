package com.lenayeliieshvili.fbxparser.parser;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GeometryVertex {

    private float[] mVertices;
    private float[] mColors;

    float[] colors = {
            0f, 1f, 1f,
            0f, 0.3f, 0.3f,
            0f, 0.5f, 0.5f

    };

    private StringBuilder mVerticesStr = new StringBuilder();
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;

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
        mColors = new float[split.length];
        try {
            for (int i = 0; i < split.length; i++) {
                mVertices[i] = Float.parseFloat(split[i]);
                mColors[i] = colors[i % colors.length];
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

        ByteBuffer colorBuffer = ByteBuffer.allocateDirect(mColors.length * 4);
        colorBuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = colorBuffer.asFloatBuffer();
        mColorBuffer.put(mColors);
        mColorBuffer.position(0);
    }

    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }

    public FloatBuffer getColorBuffer() {
        return mColorBuffer;
    }
}
