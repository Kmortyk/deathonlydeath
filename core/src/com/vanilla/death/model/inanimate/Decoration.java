package com.vanilla.death.model.inanimate;

import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.model.GameObject;
import com.vanilla.death.model.person.interactions.GameScript;

/**
 * Created by mrkotyk on 20.06.17.
 */
public class Decoration extends GameObject {

    private int HP;
    private boolean movable;
    private boolean jumpable;

    public Decoration(String name, Vector2 position){
        setPosition(position);
    }

}
