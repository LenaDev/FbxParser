package com.lenayeliieshvili.fbxparser.fbx;


public class Geometry {

    private int mId;
    private String mConnectionsName;

    private GeometryVertex mVertex;
    private GeometryIndex mIndex;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getConnectionsName() {
        return mConnectionsName;
    }

    public void setConnectionsName(String connectionsName) {
        mConnectionsName = connectionsName;
    }

    public GeometryVertex getVertex() {
        return mVertex;
    }

    public void setVertex(GeometryVertex vertex) {
        mVertex = vertex;
    }

    public GeometryIndex getIndex() {
        return mIndex;
    }

    public void setIndex(GeometryIndex index) {
        mIndex = index;
    }
}
