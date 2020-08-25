package com.vanilla.death.utils;

import com.badlogic.gdx.utils.ArrayMap;
import com.vanilla.death.model.GameObject;

/**
 * Created by mrkotyk on 28.08.17.
 */
public class GraphicLists {

    private static GraphicLists object;

    public static GraphicLists getObject(){
        if(object == null) object = new GraphicLists();
        return object;
    }

    private String[] commonAnimationNames;
    private float[] commonFramerates;
    private String[] commonSpriteNames;
    private ArrayMap<GameObject,String> path;

    private GraphicLists(){
        path = new ArrayMap<GameObject, String>();
    }

    public String[] getCommonAnimationNames() {
        return commonAnimationNames;
    }

    public float[] getCommonFramerates() {
        return commonFramerates;
    }

    public String[] getCommonSpriteNames() {
        return commonSpriteNames;
    }

    public void setCommonAnimationNames(String[] commonAnimationNames) {
        this.commonAnimationNames = commonAnimationNames;
    }

    public void setCommonFramerates(float[] commonFramerates) {
        this.commonFramerates = commonFramerates;
    }

    public void setCommonSpriteNames(String[] commonSpriteNames) {
        this.commonSpriteNames = commonSpriteNames;
    }

    public ArrayMap<GameObject, String> getPath() {
        return path;
    }
}
