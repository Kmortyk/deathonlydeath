package com.vanilla.death.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.vanilla.death.controller.GameContactListener;
import com.vanilla.death.model.inanimate.Decoration;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;
import com.vanilla.death.utils.ObjectManager;
import com.vanilla.death.model.GameObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mrkotyk on 23.06.17.
 */
public class GameWorld {

    //Array<Ground> ground = new Array<Ground>();
    //Array<Mob> data.mobs = new Array<Mob>();
    //Array<Hero> heroes = new Array<Hero>();
    //Array<Decoration> decorations = new Array<Decoration>();
    //Array<Item> items = new Array<Item>();

    private static GameWorld instance;

    public static GameWorld getInstance() {
        if (instance == null)
            instance = new GameWorld();
        return instance;
    }


    private Hex[][] hexes;
    public int length = 0;
    private float max_distance = 0;
    private ArrayList<Ground> ground;
    private ArrayList<Mob> mobs;
    private ArrayList<Decoration> decorations;
    private ArrayList<Item> items;
    private ArrayList<Hero> heroes;

    private Hero hero;

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    private String mapName = "coat";
    private int localID;

    private ObjectManager objectManager;

    //physics
    //private World physicalWorld;
    //physics

    private GameWorld() {
        //physics
        //physicalWorld = new World(new Vector2(0,-20), true);
        //physicalWorld.setContactListener(new GameContactListener(physicalWorld));
        //physics
        objectManager = new ObjectManager();
        CreateWorld(mapName);
    }


    public void CreateWorld(String mapName)
    {
        length = objectManager.GetMapLength(mapName);
        max_distance = (float) (GameObject.HEXHEIGHT*length*Math.sqrt(2));

        hexes = new Hex[length][length];
        for(int i=0;i<length;i++)
            for(int j=0;j<length;j++)
                hexes[i][j] = new Hex();

        ground = objectManager.GetGround(mapName+"G");
        for (Ground gr: ground) {
            if(gr!=null)hexes[gr.getI()][gr.getJ()].setGround(gr);
        }

        mobs = objectManager.GetMobs(mapName+"M");
        for(Mob mb: mobs){
            if(mb!=null)hexes[mb.getI()][mb.getJ()].setMob(mb);
        }

        items = objectManager.GetItems(mapName+"I");
        for(Item itm: items){
            if(itm!=null)hexes[itm.getI()][itm.getJ()].getItems().add(itm);
        }

        //hero = new Hero("hero1",0,0);
        heroes = new ArrayList<Hero>();
        //TODO object manager bad boy
//        mobs = objectManager.GetMobObjectMap(mapName+"M");
//        decorations = objectManager.GetDecorationObjectMap(mapName+"D");
//        items = objectManager.GetItemObjectMap(mapName+"I");

//        addHero(localID);

    }

    public Hero getHero() {
        return hero;
    }

    public ArrayList<Ground> getGround() {
        return ground;
    }

    public ArrayList<Mob> getMobs() {
        return mobs;
    }

    public ArrayList<Decoration> getDecorations() {
        return decorations;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Hero> getHeroes(){ return heroes; }

    public Hex[][] getHexes(){ return hexes; }

    public void addHero(int id){
        for(int i=0;i<length;i++){
            for(int j=0;j<length;j++){
                if(hexes[i][j].isFree()){

                    hexes[i][j].setHero(objectManager.GetHero("Hero",i,j,id));
                    heroes.add(hexes[i][j].getHero());
                    if(id == localID) hero = hexes[i][j].getHero();

                    return;
                }

            }
        }
    }

    public void addHero(int id, int i, int j){
        hexes[i][j].setHero(objectManager.GetHero("Hero",i,j,id));
        heroes.add(hexes[i][j].getHero());
        if(id == localID) hero = hexes[i][j].getHero();
    }



    public int getID(){ return localID; }

    public float getMaxDistance() {return max_distance;}

    public String getMapName() {return mapName;}

    public void setMapName(String mapName) {this.mapName = mapName;}

    public boolean inArray(int i, int j){
        return (i >= 0 && i < length) && (j >= 0 && j < length);
    }



    //    public Hero getHeroById(String id){
//
//        for(Hero[] heroes: getHeroes()){
//
//            for(Hero hero: heroes){
//                if(hero!=null) {
//                    if(hero.getId().equals(id)) return hero;
//                }
//            }
//        }
//        return null;
//    }

//    public void removeHero(String id){
//
//        for(Hero[] heroes: getHeroes()){
//
//            for(Hero hero: heroes){
//                if(hero!=null) {
//                    if(hero.getId().equals(id)) hero = null;
//                }
//            }
//        }
//    }

    public void dispose() {
        hexes = null;
        ground  = null;
        decorations = null;
        items = null;
        mobs = null;
        heroes = null;
        hero = null;
        instance = null;
    }

    public static boolean isUsed() {
        return instance != null;
    }

}
