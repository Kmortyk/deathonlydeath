package com.vanilla.death.model.person.interactions;

import com.badlogic.gdx.Gdx;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mrkotyk on 24.08.17.
 */
public class GameScript {


    //0 - add quest to hero
    int type;
    private Quest quest;
    private ArrayList<Item> items;
    private ArrayList<Mob> mobs;


    public GameScript(String name, int i, int j){

    }

    public GameScript(GameScript gameScript){

    }

    public void run(){

        switch(type){
            case 0:
                Hero hero = GameWorld.getInstance().getHero();
                hero.addQuest(quest);
                Gdx.app.log("GameScript","Quest added");
                break;

        }

    }


    public static void readScript(String name){

    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Mob> getMobs() {
        return mobs;
    }

    public void setMobs(ArrayList<Mob> mobs) {
        this.mobs = mobs;
    }

}
