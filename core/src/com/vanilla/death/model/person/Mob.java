package com.vanilla.death.model.person;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.model.GameObject;
import com.vanilla.death.model.person.interactions.Dialogue;

/**
 * Created by mrkotyk on 20.06.17.
 */
public class Mob extends Person{

    public enum State{
        NONE, WALK, DEAD, FEAR, DIALOGUE, ANGRY, BATTLEMODE
    }

    public State STATE;
    int DELTAHP = 0;
    Texture texture;
    private boolean isTalking;
    private Dialogue dialogue;

    private GameObject target = null;
    public GameObject getTarget() { return target; }
    public void setTarget(GameObject target) { this.target = target; }

    public Mob(String name){
        super();
        this.name = name;
    }

    public Mob(Mob mb, int i, int j){
        super();
        name = mb.name;
        setBounds(5f, 5f);

        //boundsWidth = getGroundTexture().getWidth()*scale;
        //boundsHeight = getGroundTexture().getHeight()*scale;
        setIJ(i,j);
        this.position = getPosition(i,j);
        setPosition(position);
        STATE = mb.STATE;
        setTalking(mb.isTalking());
        if(isTalking()) setDialogue(new Dialogue(mb.getDialogue()));
        setHP(mb.getHP());
        //setHexNextXY(i,j);
        setHexPressedXY(i,j);
        setAtPoint(false);

    }


    public Vector2 getPosition(int i, int j){


        if(i%2!=0){
            return new Vector2((j + 0.5f)*HEXWIDTH*scale,(i*0.75f + 0.5f)*HEXHEIGHT*scale);
        }else{
            return new Vector2((j + 1f)*HEXWIDTH*scale,(i*0.75f + 0.5f)*HEXHEIGHT*scale);
        }

//        if(i%2!=0){
//            return new Vector2((j*HEXWIDTH + HEXWIDTH/2)*scale,
//                    (i*(HEXHEIGHT-HEXHEIGHT/4) + HEXHEIGHT/2)*scale);
//        }else{
//            return new Vector2(((j*HEXWIDTH+HEXWIDTH/2) + HEXWIDTH/2)*scale,
//                    (i*(HEXHEIGHT-HEXHEIGHT/4) + HEXHEIGHT/2)*scale);
//        }

        //return new Vector2(i,j);
    }


    public boolean isTalking() {return isTalking;}

    public void setTalking(boolean talking) {isTalking = talking;}

    public Dialogue getDialogue() {return dialogue;}

    public void setDialogue(Dialogue dialogue) {this.dialogue = dialogue;}

    //public boolean isMoving(){ return STATE == State.WALK && (getI() != getHexNextX() || getJ() != getHexNextY()); }

    public boolean isMoving(){ return STATE == State.WALK && !atPoint(); }
}
