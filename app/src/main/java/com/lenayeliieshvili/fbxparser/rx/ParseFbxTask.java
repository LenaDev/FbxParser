package com.lenayeliieshvili.fbxparser.rx;


import com.lenayeliieshvili.fbxparser.fbx.FbxModel;
import com.lenayeliieshvili.fbxparser.fbx.Geometry;
import com.lenayeliieshvili.fbxparser.fbx.GeometryIndex;
import com.lenayeliieshvili.fbxparser.fbx.GeometryVertex;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

import static com.lenayeliieshvili.fbxparser.fbx.FbxKeys.ARRAY;
import static com.lenayeliieshvili.fbxparser.fbx.FbxKeys.GEOMETRY;
import static com.lenayeliieshvili.fbxparser.fbx.FbxKeys.OBJECT_END;
import static com.lenayeliieshvili.fbxparser.fbx.FbxKeys.OBJECT_START;
import static com.lenayeliieshvili.fbxparser.fbx.FbxKeys.POLYGON_VERTEX_INDEX;
import static com.lenayeliieshvili.fbxparser.fbx.FbxKeys.PROPERTY;
import static com.lenayeliieshvili.fbxparser.fbx.FbxKeys.VERTICES;

public class ParseFbxTask {

    public Observable<FbxModel> parseFbxFile(BufferedReader reader) {
        return Observable.defer(new Func0<Observable<FbxModel>>() {
            @Override
            public Observable<FbxModel> call() {
                return Observable.create(subscriber -> {
                        FbxModel fbxModel = new FbxModel();
                        String currentObj = "";
                        boolean isReadingArray = false;
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
                                if (isReadingArray) {
                                    if (currentObj.equals(VERTICES)) {
                                        isReadingArray = true;
                                        fbxModel.getLastAddedGeometry().getVertex().setStrVertices(line);
                                    }
                                    if (currentObj.equals(POLYGON_VERTEX_INDEX)) {
                                        isReadingArray = true;
                                        fbxModel.getLastAddedGeometry().getIndex().setIndexStr(line);
                                    }
                                }
                                if (line.contains(PROPERTY)) {
                                    if (currentObj.equals(VERTICES) && line.contains(ARRAY)) {
                                        isReadingArray = true;
                                        fbxModel.getLastAddedGeometry().getVertex().setStrVertices(line);
                                    }
                                    if (currentObj.equals(POLYGON_VERTEX_INDEX) && line.contains(ARRAY)) {
                                        isReadingArray = true;
                                        fbxModel.getLastAddedGeometry().getIndex().setIndexStr(line);
                                    }
                                }
                                if(line.contains(OBJECT_END)) {
                                    isReadingArray = false;
                                    currentObj = "";
                                }
                            }
                            reader.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        List<Geometry> geometries = fbxModel.getGeometries();
                        for (Geometry g : geometries) {
                            if (g.getIndex() != null) {
                                g.getIndex().convertStringToArray();
                            }
                            if (g.getVertex() != null) {
                                g.getVertex().convertStringToArray();
                            }
                        }


                        subscriber.onNext(fbxModel);
                        subscriber.onCompleted();
                });
            }
        });

    }
}
