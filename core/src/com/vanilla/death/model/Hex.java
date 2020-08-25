package com.vanilla.death.model;

import com.badlogic.gdx.utils.Array;
import com.vanilla.death.model.inanimate.Decoration;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;
import com.vanilla.death.model.person.Person;

import java.util.ArrayList;

/**
 * Created by mrkotyk on 18.07.17.
 */
public class Hex {

    private Hero hero;
    private Mob mob;
    private Ground ground;
    private Decoration decoration;
    private ArrayList<Item> items = new ArrayList<Item>();
    private boolean battleMode = false;

    public boolean inBattleMode(){ return battleMode; }
    public void setBattleMode(boolean battleMode){ this.battleMode = battleMode; }

    public Hero getHero(){ return hero; }

    public void setHero(Hero hero){ this.hero = hero; }

    public Mob getMob(){ return mob; }

    public void setMob(Mob mob){ this.mob = mob; }

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }

    public Decoration getDecoration() {return decoration;}

    public void setDecoration(Decoration decoration) {
        this.decoration = decoration;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public boolean hasItems(){
        return !items.isEmpty();
    }
    public boolean hasVisibleHero(){ return hero!=null; }
    //public boolean hasMob(){ return mob!=null; }
    public boolean hasTalkingMob(){ return mob!=null && mob.isTalking() && mob.STATE == Mob.State.NONE;}

    public boolean isFree(){return hero == null && mob == null && ground!=null && ground.getStepable();}
    public boolean isFree(Hero hero){return (this.hero == hero || this.hero == null) && mob == null && ground!=null && ground.getStepable();}
    public boolean isFree(Mob mob){return hero == null && (this.mob == mob || this.mob==null) && ground!=null && ground.getStepable();}
}
