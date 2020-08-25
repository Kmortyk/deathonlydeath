package com.vanilla.death.model.inanimate;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.model.GameObject;


/**
 * Created by mrkotyk on 21.06.17.
 */
public class Ground extends GameObject {

    private boolean isStepable;
    //название земли под этой: для уничтожения грунта (взрыв обнажает нижнюю землю)
    private String BOTTOMNAME;
    private Sound step_sound;
    private Texture ground_texture;

    public Polygon hexBounds = new Polygon();
    public Vector2 hexCenter = new Vector2();


    public Ground(String name, int x, int y) {
        super();
        this.name = name;
        position.x = x;
        position.y = y;
        setBounds(0.6f, 0.6f);
        updateBounds();
    }

    public Ground(String name){
        super();
        this.name = name;
        setBounds(0.6f, 0.6f);
        setPosition(new Vector2(0,0));
    }
    /*copy constructor with coordinates*/
    public Ground(Ground gr, int i, int j){
        super();
        name = gr.name;
        setGroundTexture(gr.getGroundTexture());
        setBounds(getGroundTexture().getWidth()*scale, getGroundTexture().getHeight()*scale);
        setIJ(i,j);
        position = getPosition(i,j);
        setPosition(position);
        setHexPolygon();
        setStepable(gr.getStepable());


    }

    public void setStepable(boolean isStepable){this.isStepable = isStepable;}

    public boolean getStepable(){return isStepable;}

    public String getBOTTOMNAME() {return BOTTOMNAME;}

    public void setBOTTOMNAME(String BOTTOMNAME) {this.BOTTOMNAME = BOTTOMNAME;}

    public Sound getStepSound() {return step_sound;}

    public void setStepSound(Sound step_sound) {this.step_sound = step_sound;}


    public Texture getGroundTexture() {return ground_texture;}

    public void setGroundTexture(Texture ground_texture) {this.ground_texture = ground_texture;}



    //public Ground(Vector2 position)
    //{
    //    this.position = position;
    //}

    public void setHexPolygon(){

        float x = position.x + bounds.getWidth()/2;
        float y = position.y + bounds.getHeight()/2;

        float vertices[] = new float[12];

        for (int i = 0; i < 6; i++) {

            vertices[i*2] = (float) (x + bounds.getHeight()/2 * Math.cos(2 * Math.PI * i / 6 + Math.PI/6) );
            vertices[i*2+1] = (float) (y + bounds.getHeight()/2 * Math.sin(2 * Math.PI * i / 6 + Math.PI/6) );
        }

        hexBounds.setVertices(vertices);
        hexCenter.set(x,y);

    }

    public float getHexHeight(){
       return getGroundTexture().getHeight();
    }

    public float getHexWidth(){
        return getHexHeight()*((float) Math.sqrt(3)/2);
    }

    public static Vector2 getPosition(int i, int j){


        if(i%2!=0){
            return new Vector2(j*HEXWIDTH*scale, i*(HEXHEIGHT-HEXHEIGHT/4)*scale);
        }else{
            return new Vector2((j*HEXWIDTH+HEXWIDTH/2)*scale,  i*(HEXHEIGHT-HEXHEIGHT/4)*scale);
        }

        //return new Vector2(i,j);
    }

}
