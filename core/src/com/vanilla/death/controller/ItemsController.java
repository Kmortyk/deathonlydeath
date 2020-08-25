package com.vanilla.death.controller;

import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.Hex;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.inanimate.Item;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.model.person.Mob;

/**
 * Created by mrkotyk on 28.07.17.
 */
public class ItemsController {

    private Hex[][] hexes;
    private int length;

    public ItemsController() {
        this.hexes = GameWorld.getInstance().getHexes();
        length = hexes.length;
    }

    public void toHex(Item item, int x, int y) {
        if(!item.isMoving()){item.setToHexXY(x,y);}
        item.STATE = Item.State.WALK;
    }

    public void hexReleased(Item item) {
            item.STATE = Item.State.NONE;
            item.setDirX(false);
            item.setDirY(false);
    }

    private void setBelongsHex(Item item) {

        Ground next = hexes[item.getToHexX()][item.getToHexY()].getGround();
        Ground last = hexes[item.getI()][item.getJ()].getGround();

        if (!last.bounds.contains(item.bounds)) {
            hexes[item.getI()][item.getJ()].removeItem(item);

            if (next.bounds.contains(item.bounds)) {
                item.setIJ(item.getToHexX(), item.getToHexY());
                hexes[item.getToHexX()][item.getToHexX()].getItems().add(item);
            }
        }
    }


    //главный метод класса...обновляем состояния объектов здесь
    public void update(float delta) {
        for (Item item : GameWorld.getInstance().getItems()) {
            if(item==null)continue;
            switch (item.STATE) {
                case WALK:

                    int toHexX = item.getToHexX();
                    int toHexY = item.getToHexY();

                    if(!hexes[toHexX][toHexY].isFree()){
                        item.STATE = Item.State.ACTIVE;
                        return;
                    }


                    Ground gr = hexes[toHexX][toHexY].getGround();
                    Vector2 direction = gr.hexCenter;

                    if (!item.bounds.contains(gr.hexCenter.x, item.position.y)) {
                        if (direction.x > item.position.x) item.velocity.x = item.spriteSpeed;
                        if (direction.x < item.position.x) item.velocity.x = -item.spriteSpeed;
                    } else item.setDirX(true);

                    if (!item.bounds.contains(item.position.x, gr.hexCenter.y)) {
                        if (direction.y > item.position.y) item.velocity.y = item.spriteSpeed;
                        if (direction.y < item.position.y) item.velocity.y = -item.spriteSpeed;
                    } else item.setDirY(true);

                    if (item.isDirXY()) {
                            hexReleased(item);
                    }
                    setBelongsHex(item);
                    item.update(gr.hexCenter, delta);
                    break;

                case FALLING:
                    break;
                case NONE:
                    break;
            }


        }
    }


}
