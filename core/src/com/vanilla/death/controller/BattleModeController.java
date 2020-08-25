package com.vanilla.death.controller;

import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.Hex;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;
import com.vanilla.death.model.person.Person;
import java.util.ArrayList;

/**
 * Created by mrkotyk on 12.08.17.
 */
public class BattleModeController {

    private static BattleModeController object;

    public static BattleModeController getObject(){
        if(object==null){
            object = new BattleModeController();
        }
        return object;
    }

    private static ArrayList<Hero> heroesParty = new ArrayList<Hero>();
    private static ArrayList<Mob> mobsParty = new ArrayList<Mob>();
    private static ArrayList<Hex> hexesBattle = new ArrayList<Hex>();
    private static int i = 0;

    private static boolean heroTurn = true;

    public void next(){
        i++;
        if(heroTurn && i >= heroesParty.size()){ heroTurn = !heroTurn; i = 0; }
        if(!heroTurn && i >= mobsParty.size()){ heroTurn = !heroTurn; i = 0; }
    }

    public boolean isMyTurn(Hero hero){
        //System.out.print("Hero: ");
        //System.out.println(heroTurn && i<heroesParty.size() && heroesParty.get(i)==hero);
        return heroTurn && i<heroesParty.size() && heroesParty.get(i)==hero;
    }

    public boolean isMyTurn(Mob mob){
        //System.out.print("Mob: ");
        //System.out.println(!heroTurn && i<mobsParty.size() && mobsParty.get(i)==mob);
        return !heroTurn && i<mobsParty.size() && mobsParty.get(i)==mob;
    }

    public ArrayList<Hero> getHeroesParty() { return heroesParty; }

    public ArrayList<Mob> getMobsParty() { return mobsParty; }

    public void addHex(Hex hex){
        hex.setBattleMode(true);
        hexesBattle.add(hex);
    }

    public void clearHexes(){
        for(Hex hx: hexesBattle){
            hx.setBattleMode(false);
        }
        hexesBattle.clear();
    }

    public void reAddHexes(){
        for(Hex hx: hexesBattle){
            hx.setBattleMode(true);
        }
    }

    public Hero getNearHero(Mob mob){
        int min_length = 2 * GameWorld.getInstance().length;
        Hero hero = null;
        for(Hero hr: heroesParty){
            if(Math.abs(mob.getI()-hr.getI())+Math.abs(mob.getJ()-hr.getJ()) < min_length){
                hero = hr;
            }
        }
        return hero;
    }

    public void removeHero(Hero hero){
        heroesParty.remove(hero);
        hero.STATE = Hero.State.NONE;
        hero.restoreMOVEPOINTS();
        next();
    }

    public void removeMob(Mob mob){
        mobsParty.remove(mob);
        mob.STATE = Mob.State.NONE;
        mob.restoreMOVEPOINTS();
        next();
    }

    public void stepped(Person pr){
        pr.setMOVEPOINTS(pr.getMOVEPOINTS()-1);
        if (pr.getMOVEPOINTS() <= 0) {BattleModeController.getObject().next(); pr.restoreMOVEPOINTS(); }
    }

    public void checkPartys(Mob mob){
        if(heroesParty.size()==0){
            mobsParty.remove(mob);
            mob.STATE = Mob.State.ANGRY;
            mob.restoreMOVEPOINTS();
            clearHexes();
            i = 0;
        }
    }

    public void checkPartys(Hero hero){
        if(mobsParty.size()==0){
            heroesParty.remove(hero);
            hero.STATE = Hero.State.NONE;
            hero.restoreMOVEPOINTS();
            clearHexes();
            i = 0;
        }
    }

    public void dispose(){
        heroesParty = null;
        mobsParty = null;
        hexesBattle = null;
        object = null;
    }

}
