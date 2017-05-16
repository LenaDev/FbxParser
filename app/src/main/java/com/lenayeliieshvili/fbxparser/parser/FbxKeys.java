package com.lenayeliieshvili.fbxparser.parser;


public class FbxKeys {

    public static final String FBX_OBJECTS = "Objects:";
    public static final String GEOMETRY = "Geometry:";
    public static final String VERTICES = "Vertices:";
    public static final String POLYGON_VERTEX_INDEX = "PolygonVertexIndex:";
    public static final String ARRAY = "a:";

    public static final String OBJECT_START = "{";
    public static final String OBJECT_END = "}";
    public static final String PROPERTY = ":";
    public static final String COMMENT = ";";

    public static final String REGEX_CLEAN = "\\s|\\t|\\n|\\}|\\{";
    public static final String REGEX_NO_SPACE_NO_QUOTE = "\\\"|\\s";
    public static final String REGEX_NO_QUOTE = "\\\"";
    public static final String REGEX_NO_FUNNY_CHARS = "\\W";
    public static final String REPLACE_EMPTY = "";

}
