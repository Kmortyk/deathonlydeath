package com.vanilla.death.model.person;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.vanilla.death.model.GameObject;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.interactions.Quest;

import java.util.ArrayList;

/**
 * Created by mrkotyk on 20.06.17.
 */
public abstract class Person extends GameObject {

    private int HP;
    private int LVL;
    private int EXP;
    private int ATTACK = 1;
    private int SHIELD;
    private int SPEED = 1;
    //состояние персонажа: напуган,зол и тд
    //String STATE;

    private Texture SPRITE;
    private float spriteSize = 0.7f;
    public int[] lookDir = new int[2];

    private boolean isAlive;
    private int MOVEPOINTS;
    private int ATTACKDISTANCE;

    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Quest> quests = new ArrayList<Quest>();
    private ArrayList<String> completed_quests = new ArrayList<String>();

    private int hexPressedX, hexPressedY;
    private int hexNextX, hexNextY;
    private int min_lengthX, min_lengthY;
    private float min_distandce;
    private float waiting_time, max_time = 2;
    public int getHexPressedX() {return hexPressedX;}
    public void setHexPressedX(int hexPressedX) {this.hexPressedX = hexPressedX;}
    public int getHexPressedY() {return hexPressedY;}
    public void setHexPressedY(int hexPressedY) {this.hexPressedY = hexPressedY;}
    public void setHexPressedXY(int x, int y){ hexPressedX = x; hexPressedY = y;}
    public int getHexNextX() {return hexNextX;}
    public void setHexNextXY(int hexNextX,int hexNextY) {this.hexNextX = hexNextX; this.hexNextY = hexNextY;}
    public int getHexNextY() {return hexNextY;}
    public int getMinLengthX() {return min_lengthX;}
    public void setMinLengthXY(int min_lengthX, int min_lengthY) {this.min_lengthX = min_lengthX; this.min_lengthY = min_lengthY;}
    public int getMinLengthY() {return min_lengthY;}
    public float getMinDistance(){ return min_distandce; }
    public void setMinDistance(float min_distandce){ this.min_distandce = min_distandce; }

    public void updateLookDir(Vector2 next_postion){
        lookDir[0] = (position.x >= next_postion.x) ? 1 : -1;
        if(position.x == next_postion.x) lookDir[0] = 0;
        lookDir[1] = (position.y >= next_postion.y) ? 1 : -1;
        if(position.y == next_postion.y) lookDir[1] = 0;
    }

    public void hit(int damage){ HP-=damage; }

    public boolean updateTime(float delta) {
        waiting_time+=delta;
        if(waiting_time>max_time){
            waiting_time = 0;
            return true;
        }
        return false;
    }

    public int getHP() {return HP;}

    public void setHP(int HP) {this.HP = HP;}

    public int getLVL() {return LVL;}

    public void setLVL(int LVL) {this.LVL = LVL;}

    public int getEXP() {return EXP;}

    public void setEXP(int EXP) {this.EXP = EXP;}

    public int getATTACK() {return ATTACK;}

    public void setATTACK(int ATTACK) {this.ATTACK = ATTACK;}

    public int getSHIELD() {return SHIELD;}

    public void setSHIELD(int SHIELD) {this.SHIELD = SHIELD;}

    public int getSPEED() {return SPEED;}

    public void setSPEED(int SPEED) {this.SPEED = SPEED;}

    public Texture getSPRITE() {return SPRITE;}

    public void setSPRITE(Texture SPRITE) {this.SPRITE = SPRITE;}

    public float getSpriteSpeed() {return spriteSpeed;}

    public void setSpriteSpeed(float spriteSpeed) {this.spriteSpeed = spriteSpeed;}

    public float getSpriteSize() {return spriteSize;}

    public void setSpriteSize(float spriteSize) {this.spriteSize = spriteSize;}

    public boolean isAlive() {return isAlive;}

    public void setAlive(boolean alive) {isAlive = alive;}

    public int getMOVEPOINTS() {return MOVEPOINTS;}

    public void setMOVEPOINTS(int MOVEPOINTS) {this.MOVEPOINTS = MOVEPOINTS;}

    public void restoreMOVEPOINTS(){
        MOVEPOINTS = SPEED;
    }

    public int getATTACKDISTANCE() {return ATTACKDISTANCE;}

    public void setATTACKDISTANCE(int ATTACKDISTANCE) {this.ATTACKDISTANCE = ATTACKDISTANCE;}


    public void addItems(ArrayList<Item> items){
        this.items.addAll(items);
    }

    public ArrayList<Item> getItems(){ return items; }

    public void addQuest(Quest quest){  System.out.println("Person : Quest «" + quest.getName() + "» added"); quests.add(quest); }

    public ArrayList<Quest> getQuests(){ return quests; }

    public ArrayList<String> getCompletedQuests(){ return completed_quests; }

    public void useItem(Item it){

        loadScripts(it.getScriptNames());

        switch(it.getType())
        {
            default:
                ATTACK += it.getSHIELD();
                SPEED += it.getSPEED();
                SHIELD += it.getSHIELD();
                HP += it.getHP();
                //MOVEPOINTS
                //DISTANCE
                //addExp();
                break;
        }

    }

    //public void снять предмет
    //одеть предмет

}
