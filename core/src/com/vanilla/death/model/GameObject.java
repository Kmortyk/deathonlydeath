package com.vanilla.death.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.model.person.interactions.GameScript;
import com.vanilla.death.utils.ScriptLoader;
import com.vanilla.death.view.GraphicRepresentation;

/**
 * Created by mrkotyk on 20.06.17.
 */
public abstract class GameObject {
    public String name;

    public Vector2 position;
    public Vector2 velocity = new Vector2();
    public float spriteSpeed = 21f;

    //physics
    //public BodyDef bodyDefinition = new BodyDef();
    //physics

    protected int i, j;
    //private boolean isDirX = false, isDirY = false;
    private boolean atPoint;

    //FIXME properties?
    public static final float HEXWIDTH = 221f, HEXHEIGHT = 256f;
    public static final float scale = 0.1f;

    private GraphicRepresentation graphics;

    private String[] scriptNames = null;

    public Rectangle bounds = new Rectangle();
    //public Rectangle getBounds() {
    //    return bounds;
    //}

   // public void setBounds(Rectangle bounds) {this.bounds = bounds;}

    public GameObject(){

    }

    public void updateBounds(){ bounds.setPosition(position); }

    public void setPosition(Vector2 position){
        this.position = position;
        updateBounds();
    }

    public void setBounds(float boundsWidth, float boundsHeight){
       bounds.setSize(boundsWidth, boundsHeight);
    }

    public int getI(){return i;}
    public int getJ(){return j;}
    public void setIJ(int i, int j){this.i = i; this.j = j;}
    //public void setDirX(boolean dirX) {isDirX = dirX; }
    //public void setDirY(boolean dirY) {isDirY = dirY;}
    //public void setDirXY(boolean value){ isDirX = value; isDirY = value; }
    //public boolean isDirXY() {return isDirX && isDirY;}


//    public boolean update(Vector2 next_position, float delta){
//
//        //velocity.set(next_position.x - position.x, next_position.y - position.y);
//        velocity.set(next_position.x - position.x, next_position.y - position.y);
//        velocity.nor();
//
////        if(!bounds.contains(next_position.x,position.y)){
////            if(next_position.x != position.x){ velocity.x *= spriteSpeed; }
////        }else isDirX = true;
////
////        if(!bounds.contains(position.x, next_position.y)){
////            if(next_position.y != position.y){ velocity.y *= spriteSpeed; }
////        }else isDirY = true;
//
//
//
////        if(bounds.contains(next_position.x, next_position.y)){
////            setDirXY(true);
////        }
//
//        if(Vector2.dst2(position.x, position.y, next_position.x, next_position.y) <= delta){
//            setDirXY(true);
//        }else{
//            velocity.x *= spriteSpeed;
//            velocity.y *= spriteSpeed;
//
//        }
//
//        updateLookDir();
//        position.mulAdd(velocity, delta);
//        updateBounds();
//        //velocity.scl(spriteSpeed);
//
////        if (!bounds.contains(next_position.x, position.y)) {
////           if (next_position.x > position.x) { velocity.x *= spriteSpeed; lookDir[0] = 1; }
////           if (next_position.x < position.x) { velocity.x *= -spriteSpeed; lookDir[0] = -1; }
////           if(next_position.x == position.x) lookDir[0] = 0;
////        } else isDirX = true;
////
////        if (!bounds.contains(position.x, next_position.y)) {
////            if (next_position.y > position.y) {velocity.y *= spriteSpeed; lookDir[1] = 1; }
////            if (next_position.y < position.y) {velocity.y *= -spriteSpeed; lookDir[1] = -1; }
////            if(next_position.y == position.y) lookDir[1] = 0;
////        } else isDirY = true;
//        return isDirXY();
//    }
    //return true when path completed
    public boolean update(Vector2 next_position, float delta){

        if(Vector2.dst2(position.x, position.y, next_position.x, next_position.y) <= delta){
            position.set(next_position.x, next_position.y);
            atPoint = true;
        }else{
            velocity.set(next_position.x - position.x, next_position.y - position.y).nor();
            velocity.x *= spriteSpeed;
            velocity.y *= spriteSpeed;
            position.mulAdd(velocity, delta);
        }

        bounds.setPosition(position);

        return atPoint;
    }

    public boolean atPoint(){ return atPoint; }
    public void setAtPoint(boolean atPoint){ this.atPoint = atPoint; }

    public String[] getScriptNames() {
        return scriptNames;
    }

    public void setScriptNames(String[] scriptNames) {
        this.scriptNames = scriptNames;
    }

    //    public Vector2 getNextPosition(){ return next_position; }
//    public void setNextPosition(Vector2 next_position){ this.next_position = next_position; }


    public GraphicRepresentation getGraphics() {
        return graphics;
    }

    public void setGraphics(GraphicRepresentation graphics) {
        this.graphics = graphics;
    }

    public static void loadScripts(String[] scriptNames) {

        if(scriptNames==null) return;

        for(String script_name: scriptNames){
            ScriptLoader.getObject().callScript(script_name);
        }
    }
}

