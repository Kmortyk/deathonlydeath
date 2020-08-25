package com.vanilla.death.model.person;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.person.Person;
import com.vanilla.death.model.person.interactions.Quest;
import com.vanilla.death.utils.GraphicLists;
import com.vanilla.death.view.GraphicRepresentation;
import com.vanilla.death.view.InterfacePackage.InterfaceManager;

import java.util.HashMap;

/**
 * Created by mrkotyk on 20.06.17.
 */
public class Hero extends Person {

    private int id;
    private String room;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public enum State{
        NONE, WALK, DEAD, DIALOGUE, BATTLEMODE, WAIT
    }

    public boolean localHero = false;
    int POINTS;
    final int MOVEPOINTS=1;
    int maxHP = 100;
    int expTimer = 2500;
    public State STATE = State.NONE;

    //String[][]inventoryMas = new String[10][10];
    //HashMap<String,Object> itemsHashMap;

    public Hero(String name){
        super();
        this.name = name;
    }

    public Hero(int id, int i, int j) {
        this.id = id;
        setBounds(5f, 5f);
        setIJ(i,j);
        this.position = getPosition(i,j);
        setPosition(position);

        restoreMOVEPOINTS();
        //hex[i][j].setHero(this)
        //bounds = new Rectangle(x,y,x+0.3f,y+0.3f);
    }

    public Hero(Hero hr, int i, int j, int id) {
        super();
        setBounds(5f, 5f);
        this.id = id;
        setIJ(i,j);
        this.position = getPosition(i,j);
        setPosition(position);

        setHP(hr.getHP());
        restoreMOVEPOINTS();
        setGraphics(new GraphicRepresentation(GraphicLists.getObject().getPath().get(hr),
                GraphicLists.getObject().getCommonSpriteNames(), GraphicLists.getObject().getCommonFramerates(),
                GraphicLists.getObject().getCommonAnimationNames()));
        getGraphics().setSprite("LookLeft");
        getGraphics().setAnimation("WalkLeft");
        //hex[i][j].setHero(this)
        //bounds = new Rectangle(x,y,x+0.3f,y+0.3f);
    }


    public int getId(){ return id; }

    private Vector2 getPosition(int i, int j){

//        if(i%2!=0){
//            return new Vector2((j + 0.5f)*HEXWIDTH*scale,(i*0.75f + 0.5f)*HEXHEIGHT*scale);
//        }else{
//            return new Vector2((j + 1f)*HEXWIDTH*scale,(i*0.75f + 0.5f)*HEXHEIGHT*scale);
//        }

        Vector2 position = Ground.getPosition(i,j);
        //ширина несколько меньше, а при отрисовке гексагона пустые поля с нулевой альфой тоже рисуются
        //поэтому везде длина на два, тогда ровно в центре
        position.x += (HEXHEIGHT/2)*scale;
        position.y += (HEXHEIGHT/2)*scale;
        return position;

    }

    public void checkQuests(){
        for(Quest qst: getQuests()){
            if(!qst.isCompleted && qst.check(this)){
               qst.isCompleted = true;
                System.out.println("Hero : Quest «" + qst.getName() + "» completed");
                //TODO Quest completed deferambs
                //InterfaceManager.getObject().YEEQUESTCOMPLETED();
            }
        }
    }

}
