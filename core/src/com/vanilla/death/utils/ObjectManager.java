package com.vanilla.death.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.vanilla.death.model.GameObject;
import com.vanilla.death.model.inanimate.Decoration;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;

import java.util.ArrayList;


/**
 * Created by mrkotyk on 21.06.17.
 */
public class ObjectManager {

    private ResourceManager resourceManager = new ResourceManager();
    private JsonParser jsonParser = new JsonParser();

    private Array<Ground> allGround = new Array<Ground>();
    private Array<Mob> allMobs = new Array<Mob>();
    Array<Decoration> allDecorations = new Array<Decoration>();
    private Array<Item> allItems = new Array<Item>();
    private Array<Hero> allHeroes = new Array<Hero>();


//    public GameObject[][] GetObjectMap(String objectsType, String mapName) {
//
//        String[][] assetMap = resourceManager.GetMap(mapName);
//        int length = assetMap[0].length;
//
///**/
//        Logger log = Logger.getLogger(ObjectManager.class.getName());
//        log.info("Length: "+length);
///**/
//
//        GameObject[][] objectMap = new GameObject[length][length];
//
//
//        for (int i = 0; i < length; i++) {
//            for (int j = 0; j < length; j++) {
//
//                objectMap[i][j] = GetObject(objectsType, assetMap[i][j], i, j);
//
//            }
//        }
//
//        return objectMap;
//    }

    public ObjectManager(){
        jsonParser.createGraphicLists();
        allGround = jsonParser.GetGroundData();
        allMobs = jsonParser.GetMobData();
        allItems = jsonParser.GetItemData();
        allHeroes = jsonParser.GetHeroData();
        //System.out.println("SIZEEE"+allGround.size);

    }

    public int GetMapLength(String mapName){
        return resourceManager.GetMap(mapName+"G").length;
    }

    public GameObject GetObject(String objectType, String name, int i, int j){
        if(objectType.equals("ground")) {

            if(name.equals("-")) return null;

            for (Ground gr : allGround) {
                if (gr.name.equals(name)) {
                    return new Ground(gr,i,j);
                }
            }
        }

        if(objectType.equals("mob")){

            if(name.equals("-")) return null;

                for(Mob mb: allMobs){
                    if(mb.name.equals(name)){
                        return new Mob(mb, i, j);
                    }
                }
        }

        if(objectType.equals("item")){

            if(name.equals("-")) return null;

            for(Item itm: allItems){
                if(itm.name.equals(name)){
                    return new Item(itm, i, j);
                }
            }
        }

        if(objectType.equals("decoration")) return new Decoration(name,new Vector2(i,j));
        return null;
    }


//    public Ground[][] GetGroundObjectMap(String mapName){
//        String[][] assetMap = resourceManager.GetMap(mapName);
//        int length = assetMap[0].length;
//
//        Ground[][] objectMap = new Ground[length][length];
//
//
//        for (int i = 0; i < length; i++) {
//            for (int j = 0; j < length; j++) {
//
//                //objectMap[i][j] = new Ground(assetMap[i][j], i, j);
//                objectMap[i][j] = (Ground) GetObject("ground", assetMap[i][j],i,j);
//
//
//            }
//
//        }
//
//        return objectMap;
//    }

    public ArrayList<Ground> GetGround(String mapName){
        String[][] assetMap = resourceManager.GetMap(mapName);
        int length = assetMap[0].length;

        ArrayList<Ground> ground = new ArrayList<Ground>();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {


               ground.add((Ground) GetObject("ground", assetMap[i][j],i,j));

            }

        }

        return ground;
    }

    public ArrayList<Mob> GetMobs(String mapName){
        String[][] assetMap = resourceManager.GetMap(mapName);
        int length = assetMap[0].length;

        ArrayList<Mob> mobs = new ArrayList<Mob>();


        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {

                //objectMap[i][j] = new Mob(assetMap[i][j], resourceManager.getCoordinates(i,j));
                Mob mob = (Mob) GetObject("mob", assetMap[i][j], i, j);
                if(mob!=null)mobs.add(mob);

            }
        }

        return mobs;
    }


    public ArrayList<Item> GetItems(String mapName){
        String[][] assetMap = resourceManager.GetMap(mapName);
        int length = assetMap[0].length;

        ArrayList<Item> items = new ArrayList<Item>();


        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {


                String[] assetMap_items = assetMap[i][j].split(",");
                for(String assetMap_item: assetMap_items){
                    Item item = (Item) GetObject("item", assetMap_item, i, j);
                    if(item!=null){
                        items.add(item);
                    }
                }

                //objectMap[i][j] = new Mob(assetMap[i][j], resourceManager.getCoordinates(i,j));


            }
        }

        return items;
    }

    public Hero GetHero(String name, int i, int j, int id){
        for(Hero hr: allHeroes){
                if(hr.name.equals(name)){
                    return new Hero(hr, i, j, id);
                }
        }

        return null;
    }

    public void GetMaps(){
        String[][] assetMap = resourceManager.GetMap("");
        int length = assetMap[0].length;

        ArrayList<Item> items = new ArrayList<Item>();


        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {


                String[] assetMap_items = assetMap[i][j].split(",");
                for(String assetMap_item: assetMap_items){
                    Item item = (Item) GetObject("item", assetMap_item, i, j);
                    if(item!=null){
                        items.add(item);
                    }
                }

                //objectMap[i][j] = new Mob(assetMap[i][j], resourceManager.getCoordinates(i,j));


            }
        }
    }

//    public Decoration[][] GetDecorationObjectMap(String mapName){
//        String[][] assetMap = resourceManager.GetMap(mapName);
//
//        int length = assetMap.length;
//
//        Decoration[][] objectMap = new Decoration[length][length];
//
//
//        for (int i = 0; i < length; i++) {
//            for (int j = 0; j < length; j++) {
//
//                //objectMap[i][j] = new Decoration(assetMap[i][j], options.getPosition(i,j));
//
//            }
//        }
//
//        return objectMap;
//    }
//
//    public Item[][] GetItemObjectMap(String mapName){
//        String[][] assetMap = resourceManager.GetMap(mapName);
//        int length = assetMap[0].length;
//
//        Item[][] objectMap = new Item[length][length];
//
//
//        for (int i = 0; i < length; i++) {
//            for (int j = 0; j < length; j++) {
//
//                //objectMap[i][j] = new Item(assetMap[i][j], options.getPosition(i,j));
//
//            }
//        }
//
//        return objectMap;
//    }


}
