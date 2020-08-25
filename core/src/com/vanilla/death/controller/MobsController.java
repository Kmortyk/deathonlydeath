package com.vanilla.death.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.algorithm.PathFinder;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.Hex;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;
import com.vanilla.death.model.person.Person;

import javax.swing.text.html.HTMLDocument;
import java.util.Iterator;

/**
 * Created by mrkotyk on 28.07.17.
 */
public class MobsController {

    private Hex[][] hexes;
    private int length;
    float time = 0;

    public MobsController() {
        this.hexes = GameWorld.getInstance().getHexes();
        length = hexes.length;
    }


    public void toRandomHex(Mob mob, Mob.State STATE) {
        mob.setHexPressedXY((int) (Math.random()*length),(int) (Math.random()*length));
        if (!mob.isMoving()) nextHex(mob);
        mob.STATE = STATE;

    }

    public void toHex(Mob mob, int i, int j){
        mob.setHexPressedXY(i,j);
        if (!mob.isMoving()) nextHex(mob);
        if(mob.STATE == Mob.State.NONE) mob.STATE = Mob.State.WALK;
    }

    public void toRandomHex(Mob mob){ toRandomHex(mob, Mob.State.WALK);}

    public void hexReleased(Mob mob) {
        if (mob.STATE != Mob.State.DEAD) {
            mob.STATE = Mob.State.NONE;
            mob.setAtPoint(false);
        }
    }

    public void nextHex(Mob mob) {
        if(hexes[mob.getHexPressedX()][mob.getHexPressedY()].getGround()==null) return;

        mob.setMinDistance(GameWorld.getInstance().getMaxDistance());
        int I = mob.getI(), J = mob.getJ();

//        for (int i = I - 1; i <= I + 1; i++) {
//
//            for (int j = J - 1; j <= J + 1; j++) {
//                if ((i!=I || j!=J) && GameWorld.getInstance().inArray(i,j) && hexes[i][j].getGround() != null) {
//                    switch (I % 2) {
//                        case 0:
//                            if (!(i == I + 1 && j == J - 1) && !(i == I - 1 && j == J - 1)) {
//                                calculateHex(i, j, mob);
//                            }
//                            break;
//                        case 1:
//                            if (!(i == I + 1 && j == J + 1) && !(i == I - 1 && j == J + 1)) {
//                                calculateHex(i, j, mob);
//                            }
//                            break;
//                    }
//                }
//
//            }
//
//        }

        int[] path = PathFinder.getObject().pathFind(I,J,mob.getHexPressedX(),mob.getHexPressedY()).toArray();
        int path_length = path.length;

        if(path_length >= 2) mob.setHexNextXY(path[path_length-2],path[path_length-1]);
        else{
            mob.setHexNextXY(mob.getI(), mob.getJ());
            //Gdx.app.log("MobsController ERROR","Mob path length lower then 2.");
        }
        //System.out.println(mob.getHexPressedX()+" Pressed "+mob.getHexPressedY());
        //System.out.println(mob.getHexNextX()+" Next "+mob.getHexNextY());
        //System.out.println(mob.getI()+" Position "+mob.getJ());
    }


    private void calculateHex(int i, int j, Mob mob) {
        int hexPressedX = mob.getHexPressedX();
        int hexPressedY = mob.getHexPressedY();

        Vector2 nextPosition = hexes[i][j].getGround().hexCenter;
        //Vector2 nextPosition = Ground.getPosition(i,j);
        Vector2 lastPosition = hexes[hexPressedX][hexPressedY].getGround().hexCenter;

        if(Vector2.dst2(nextPosition.x,nextPosition.y,lastPosition.x,lastPosition.y) <= mob.getMinDistance()){
            if(hexes[i][j].isFree(mob)) {
                mob.setHexNextXY(i, j);
                mob.setMinDistance(Vector2.dst2(nextPosition.x, nextPosition.y, lastPosition.x, lastPosition.y));
            }
        }
    }

    private void setBelongsHex(Mob mob) {

        Ground next = hexes[mob.getHexNextX()][mob.getHexNextY()].getGround();
        Ground last = hexes[mob.getI()][mob.getJ()].getGround();

        if (!last.bounds.contains(mob.bounds)) {
            hexes[mob.getI()][mob.getJ()].setMob(null);
            //System.out.println(mob.getI()+" "+mob.getJ());

            if (next.bounds.contains(mob.bounds)) {
                mob.setIJ(mob.getHexNextX(), mob.getHexNextY());
                hexes[mob.getHexNextX()][mob.getHexNextY()].setMob(mob);
            }
        }
    }

    //главный метод класса...обновляем состояния объектов здесь
    public void update(float delta) {
//        for(int i = length-1; i>=0; i--){
//            for(int j=0;j<length;j++){
//                System.out.print(" ");
//                System.out.print(hexes[i][j].getMob()!=null);
//            }
//            System.out.println();
//        }
//        System.out.println("=========================================================");

        for (Mob mob : GameWorld.getInstance().getMobs()) {
            if(mob==null)continue;
            if(mob.getHP() <= 0) {
                if (mob.STATE == Mob.State.BATTLEMODE) {
                    BattleModeController.getObject().removeMob(mob);
                }
                mob.STATE = Mob.State.DEAD;
            }

            Hex pressedHex = hexes[mob.getHexPressedX()][mob.getHexPressedY()];
            Hex nextHex = hexes[mob.getHexNextX()][mob.getHexNextY()];
            Hex presentHex = hexes[mob.getI()][mob.getJ()];

            switch (mob.STATE) {
                case WALK:
                    if(!nextHex.isFree(mob) || !pressedHex.isFree(mob)){
                        hexReleased(mob);
                        toRandomHex(mob);
                        return;
                    }
                    setBelongsHex(mob);
                    if (mob.update(nextHex.getGround().hexCenter, delta)) {
                        if (presentHex == pressedHex) {
                            hexReleased(mob);
                            toRandomHex(mob);
                        } else {
                            hexReleased(mob);
                            nextHex(mob);
                        }
                    }
                    break;

                case DIALOGUE:
                    break;

                case NONE:
                    if(mob.updateTime(delta))mob.STATE = Mob.State.WALK;
                    break;

                case ANGRY:
                    int i = mob.getI() - mob.lookDir[0], j = mob.getJ() - mob.lookDir[1];
                    if(GameWorld.getInstance().inArray(i,j) && hexes[i][j].hasVisibleHero()){

                        hexes[i][j].getHero().STATE = Hero.State.WAIT;
                        mob.setAtPoint(false);

//                        while(!mob.update(nextHex.getGround().hexCenter, delta)){
//                            if(hexes[i][j] == nextHex) break;
//                        setBelongsHex(mob);
//                        }


                        hexes[i][j].getHero().STATE = Hero.State.BATTLEMODE;
                        BattleModeController.getObject().getHeroesParty().add(hexes[i][j].getHero());

                        for(i = mob.getI()-2; i < mob.getI()+2; i++){
                            for(j = mob.getJ()-2; j < mob.getJ()+2; j++){
                                if(GameWorld.getInstance().inArray(i,j))
                                    BattleModeController.getObject().addHex(hexes[i][j]);
                            }
                        }
                        mob.STATE  = Mob.State.BATTLEMODE;
                        BattleModeController.getObject().getMobsParty().add(mob);
                        return;
                    }

                    if(!nextHex.isFree(mob) || !pressedHex.isFree(mob)) {
                        toRandomHex(mob, Mob.State.ANGRY); return;
                    }

                    setBelongsHex(mob);

                    if (mob.update(nextHex.getGround().hexCenter, delta)) {
                        if (presentHex == pressedHex) {
                            toRandomHex(mob, Mob.State.ANGRY);
                        } else {
                            mob.setAtPoint(false);
                            nextHex(mob);
                        }
                    }
                    break;

                case BATTLEMODE:
                    if(BattleModeController.getObject().isMyTurn(mob)){
                        //System.out.println("My turn");
                        if(mob.getTarget()==null){
                            mob.setTarget(BattleModeController.getObject().getNearHero(mob));
                            if(mob.getTarget()!=null) toHex(mob, mob.getTarget().getI(), mob.getTarget().getJ());
                        }

                        if(!nextHex.isFree(mob) || !nextHex.inBattleMode()){
                            //toRandomHex(mob, Mob.State.BATTLEMODE);
                            //toHex(mob, mob.getTarget().getI(), mob.getTarget().getJ());
                            if(nextHex.hasVisibleHero()){
                                Gdx.app.log("MobsController", "Attack hero.");
                                BattleModeController.getObject().next();
                                return;
                            }
                            //}

                        }
                        setBelongsHex(mob);
                        if (mob.update(nextHex.getGround().hexCenter, delta)) {
                            //toRandomHex(mob, Mob.State.BATTLEMODE);
                            mob.setAtPoint(false);
                            if(mob.getTarget()!=null) toHex(mob, mob.getTarget().getI(), mob.getTarget().getJ());
                            BattleModeController.getObject().next();
                        }
                    }else{
                        if(mob.getTarget()!=null) mob.setTarget(null);
                    }

                    BattleModeController.getObject().checkPartys(mob);
                    break;

                case DEAD:
                    presentHex.setMob(null);
                    break;
            }


        }
    }


}
