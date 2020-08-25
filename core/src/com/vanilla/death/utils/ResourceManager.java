package com.vanilla.death.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.I18NBundle;
import com.vanilla.death.view.GraphicRepresentation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by hedgehog on 19.06.17.
 */
public class ResourceManager {


    ResourceManager() {
        manager = new AssetManager();
        loadResources();
    }

    private static ResourceManager instance;
    private AssetManager manager;
    private Sprite errorSprite;
    private Image errorImage;
    private Animation<TextureAtlas.AtlasRegion> errorAnimation;
    private Array<TextureAtlas.AtlasRegion> errorAtlasRegion;

    private void loadResources() {
//      manager.load("floor.png", Texture.class);
//      manager.load("font.fnt", BitmapFont.class);
        loadErrorResources();
        loadResourcesForInterface();
        manager.finishLoading();
//      Floor.tex = manager.get("floor.png", Texture.class)
    }

    private void loadErrorResources() {
        try {
            String PATH_TO_ERROR_ATLAS = "sprites/gameObjects/ErrorSprites/ErrorAtlas.atlas";
            manager.load(PATH_TO_ERROR_ATLAS, TextureAtlas.class);
            manager.finishLoading();
            errorSprite = new Sprite(manager.get(PATH_TO_ERROR_ATLAS, TextureAtlas.class).findRegion("MissedSprite"));
            errorAtlasRegion = manager.get(PATH_TO_ERROR_ATLAS, TextureAtlas.class).findRegions("MissedAnimation");
            errorImage = new Image(new SpriteDrawable(errorSprite));
            errorImage.setSize(errorImage.getWidth() * GraphicRepresentation.SCALE_FACTOR,
                    errorImage.getHeight() * GraphicRepresentation.SCALE_FACTOR);
            errorAnimation = new Animation<TextureAtlas.AtlasRegion>(1f/2f, errorAtlasRegion);
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.log("Resource manager", "Texture storage is corrupted!");
            throw new NullPointerException("Error resources couldn't loaded");
        }
    }

    public Sprite getErrorSprite() {
        return errorSprite;
    }

    public Animation<TextureAtlas.AtlasRegion> getErrorAnimation() {
        return errorAnimation;
    }

    public Array<TextureAtlas.AtlasRegion> getErrorAtlasRegions() {
        return errorAtlasRegion;
    }

    public Image getErrorImage() {
        return errorImage;
    }

    private void loadResourcesForInterface() {
        manager.load("skins/defaultRus/uiskinRus.json", Skin.class);
        manager.load("localizations/Interface/Elements", I18NBundle.class);
        manager.load("sprites/interface/usersGUI/UsersGUI.atlas", TextureAtlas.class);
        manager.load("sprites/interface/InventoryBox/InventoryBoxAtlas.atlas", TextureAtlas.class);
        manager.load("sprites/interface/dialogueBox/BoxPatched.9.png", Texture.class);
    }

    public TextureAtlas getTextureAtlasByPath(String path) {
        manager.load(path, TextureAtlas.class);
        manager.finishLoading();
        return manager.get(path, TextureAtlas.class);
    }

    public static ResourceManager getInstance() {
        if (instance == null)
            instance = new ResourceManager();
        return instance;
    }

    @SuppressWarnings("unused")
    public AssetManager getManager() {
        return manager;
    }

    String[][] GetMap(String mapName) {
        //считывает двумерный массив из файла
        //двумерный массив должен быть "квадратным" - одинаковое количество строк и столбцов
        //дальше(в движке) используется квадратность двумерного массива и через length() легко определяется его размерность
/**/
        //Logger log = Logger.getLogger(ObjectManager.class.getName());
/**/

        BufferedReader bufferReader = new BufferedReader(Gdx.files.internal("data/maps/" + mapName + ".txt").reader());
        String line;
        String[][] mas = null;
        int length;

        try {

            //if ((line = bufferReader.readLine()) != null) {
                //length = line.length();
                //mas = new String[length][];
                //mas[0] = line.split(" ");
                //mas[0] = line.split(" ");} else return null;

            if((line = bufferReader.readLine())!=null) {
                length = line.split(" ").length;
                mas = new String[length][];
                mas[length-1] = line.split(" ");
            }else return null;

            for (int i = length-2; i >=0 ; i--) {
                if ((line = bufferReader.readLine()) != null)
                    mas[i] = line.split(" ");
            }

//        for(int i=0;i<length;i++){
//                for(int j=0;j<length;j++){
//                    System.out.print(mas[i][j]+" ");
//                }
//                System.out.println();
//        }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //log.info("========MrKotyk========Mass: "+length);
        return mas;
    }

    public Skin getInterfaceSkin() {
        return manager.get("skins/defaultRus/uiskinRus.json", Skin.class);
    }

    public I18NBundle getLocalizationBundle() {
        return manager.get("localizations/Interface/Elements", I18NBundle.class);
    }

    public TextureAtlas getUsersGUIAtlas() {
        return manager.get("sprites/interface/usersGUI/UsersGUI.atlas", TextureAtlas.class);
    }

    public TextureAtlas getInventoryBoxAtlas() {
        return manager.get("sprites/interface/InventoryBox/InventoryBoxAtlas.atlas", TextureAtlas.class);
    }

    public Texture getDialogueBoxBackground() {
        return manager.get("sprites/interface/dialogueBox/BoxPatched.9.png", Texture.class);
    }

    public void dispose() {
        manager.dispose();
        instance = null;
    }

    @Contract(pure = true)
    public static boolean isUsed() {
        return instance != null;
    }

    public ArrayMap<String, String[][]> readMap(String mapName){
        BufferedReader bufferReader = new BufferedReader(Gdx.files.internal("data/maps/" + mapName + ".txt").reader());
        String line;
        int length = 0;

        ArrayMap<String, String[][]> maps = new ArrayMap<String, String[][]>();
        Array<String> mapTypes = new Array<String>(new String[]{"ground", "item", "decoration", "hero", "mob"});

        try {
            do{
                line = bufferReader.readLine();

                if(mapTypes.contains(line,true)){

                    String mapType = line;

                    line = bufferReader.readLine();
                    length = line.split(" ").length;
                    String[][] map = new String[length][];

                    for(int i=0; i< length; i++){
                        map[i] = line.split(" ");
                    }
                    maps.put(mapType, map);
                }

            }while(line != null);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return maps;
    }


}
