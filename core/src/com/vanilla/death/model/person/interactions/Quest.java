package com.vanilla.death.model.person.interactions;

import com.badlogic.gdx.Gdx;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.Hero;

import java.util.ArrayList;

/**
 * Created by mrkotyk on 22.07.17.
 */
public class Quest {

    //0 - position, 1-required items and counts
    private int type;
    private String name;
    private String text;
    private String questName;
    private String heroName;

    private String mapName;
    private int i,j;
    private int[] path;
    private String[] items;
    private int[] items_count;
    private String[] mobs;

    public boolean isCompleted = false;
    private ArrayList<Item> reward_items;
    private int reward_EXP;
    private int reward_HP;


    public Quest(String name){ this.name = name; }

    public boolean check(Hero hero){
        switch(type){
            case 0:
                return checkPosition(hero);
            case 1:
                return checkItems(hero);
            case 2:
                break;
            default:
                Gdx.app.log("Quest ERROR","Wrong quest type.");
                break;
        }
        return false;
    }

    private boolean checkPosition(Hero hero){
        if(mapName.equals(GameWorld.getInstance().getMapName()))
            if(hero.getI() == i && hero.getJ() == j) return true;
        return false;
    }

    private boolean checkItems(Hero hero){
        boolean has = false;
        for(int i = 0; i < items.length;i++){
            for(Item itm: hero.getItems()){
                if(itm.name.equals(items[i]) && itm.count == items_count[i]){
                    has  = true;
                }
            }
            if(!has)break;
            else has = false;
        }
        return i == items.length-1;
    }

    public void getReward(Hero hero){
        //addExp
        //addHp
        //items etc.
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String[] getMobs() {
        return mobs;
    }

    public void setMobs(String[] mobs) {
        this.mobs = mobs;
    }

    public ArrayList<Item> getRewardItems() {
        return reward_items;
    }

    public void setRewardItems(ArrayList<Item> reward_items) {
        this.reward_items = reward_items;
    }

    public int getRewardEXP() {
        return reward_EXP;
    }

    public void setRewardEXP(int reward_EXP) {
        this.reward_EXP = reward_EXP;
    }

    public int getRewardHP() {
        return reward_HP;
    }

    public void setRewardHP(int reward_HP) {
        this.reward_HP = reward_HP;
    }

    public int[] getPath() {
        return path;
    }

    public void setPath(int[] path) {
        this.path = path;
    }

    public int[] getItemsCount() {
        return items_count;
    }

    public void setItemsCount(int[] items_count) {
        this.items_count = items_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
