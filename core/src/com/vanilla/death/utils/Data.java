package com.vanilla.death.utils;

/**
 * Created by hedgehog on 20.06.17.
 */
public class Data {

    private static Data instance;
    private MapEnum[][] mapEnum;

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();
        return instance;
    }

    private Data() {
    }

    public MapEnum[][] getMapEnum() {
        return mapEnum;
    }

    public void setMapEnum(MapEnum[][] mapEnum) {
        this.mapEnum = mapEnum;
    }

    public void dispose() {
        mapEnum = null;
        instance = null;
    }

    public static boolean isUsed() {
        return instance != null;
    }
}
