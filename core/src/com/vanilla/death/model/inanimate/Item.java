package com.vanilla.death.model.inanimate;

import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.model.GameObject;

/**
 * Created by mrkotyk on 20.06.17.
 */
public class Item extends GameObject {

    public enum State{
        ACTIVE, FALLING, WALK, NONE
    }

    public enum Type{
       ARMOR, GUN, BOOTS, PANTS, RING, GLOWS
    }

    public State STATE;
    private Type TYPE;

    private int item_type;
    private boolean isUsable;
    public int count;


    private int ADD_HP, ADD_SHIELD, ADD_ATTACK, ADD_SPEED;

    boolean WEARING = false;
    private int HEIGHT;

    private int toHexX, toHexY;
    private boolean isDirX = false, isDirY = false;

    public Item(String name){
        super();
        this.name = name;
    }

    public Item(Item itm, int i, int j){
        super();
        name = itm.name;
        setBounds(2f, 2f);
        setIJ(i,j);
        this.position = getPosition(i,j);
        setPosition(position);

        setHP(itm.getHP());
        setSHIELD(itm.getSHIELD());
        setATTACK(itm.getATTACK());
        setSPEED(itm.getSPEED());
        setUSABLE(itm.isUSABLE());


    }


    private Vector2 getPosition(int i, int j){

        float rand = (float) (Math.random()*4);

        if(i%2!=0){
            return new Vector2((j + 0.5f)*HEXWIDTH*scale +rand,(i*0.75f + 0.5f)*HEXHEIGHT*scale +rand);
        }else{
            return new Vector2((j + 1f)*HEXWIDTH*scale +rand,(i*0.75f + 0.5f)*HEXHEIGHT*scale +rand);
        }
    }

    public Type getType() {return TYPE;}

    public void setType(Type type) {this.TYPE = type;}

    public int getItemType() {return item_type;}

    public void setItemType(int item_type) {this.item_type = item_type;}

    public boolean isUSABLE() {return isUsable;}

    public void setUSABLE(boolean isUsable) {this.isUsable = isUsable;}

    public int getHP() {return ADD_HP;}

    public void setHP(int ADD_HP) {this.ADD_HP = ADD_HP;}

    public int getSHIELD() {return ADD_SHIELD;}

    public void setSHIELD(int ADD_SHIELD) {this.ADD_SHIELD = ADD_SHIELD;}

    public int getATTACK() {return ADD_ATTACK;}

    public void setATTACK(int ADD_ATTACK) {this.ADD_ATTACK = ADD_ATTACK;}

    public int getSPEED() {return ADD_SPEED;}

    public void setSPEED(int ADD_SPEED) {this.ADD_SPEED = ADD_SPEED;}

    public int getHEIGHT() {return HEIGHT;}

    public void setHEIGHT(int HEIGHT) {this.HEIGHT = HEIGHT;}

    public void setToHexXY(int toHexX, int toHexY) {this.toHexX = toHexX; this.toHexY = toHexY;}

    public int getToHexX() {return toHexX;}

    public int getToHexY() {return toHexY;}

    public boolean isDirXY() {return isDirX && isDirY;}

    public void setDirX(boolean dirX) {isDirX = dirX;}

    public void setDirY(boolean dirY) {isDirY = dirY;}

    public boolean isMoving(){ return (STATE == State.WALK || STATE == State.FALLING) && !atPoint(); }
}
