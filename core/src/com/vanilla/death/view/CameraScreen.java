package com.vanilla.death.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class CameraScreen {

    private OrthographicCamera orthographicCamera;
    private Vector2 edges, minEdges;

    private static CameraScreen object;

    private CameraScreen() {
        orthographicCamera = new OrthographicCamera();
        edges = new Vector2(0, 0);
        orthographicCamera.position.set(0,0,0);
        minEdges = new Vector2(40, 25);
    }

    void updatePosition(float x, float y) {
        orthographicCamera.update();
        orthographicCamera.position.x = x < minEdges.x ? minEdges.x : (x > edges.x ? edges.x : x);
        orthographicCamera.position.y = y < minEdges.y ? minEdges.y : (y > edges.y ? edges.y : y);
    }

    Matrix4 getCombined() {
        return orthographicCamera.combined;
    }

    void setEdges(float x, float y) {
        edges.set(x, y);
    }

    OrthographicCamera getOrthographicCamera() {
        return orthographicCamera;
    }

    public static CameraScreen getObject() {
        if (object == null) {
            object = new CameraScreen();
        }
        return object;
    }

    public void dispose() {
        orthographicCamera = null;
        object = null;
    }

    public static boolean isUsed() {
        return object != null;
    }
}
