package com.vanilla.death.controller;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.Hex;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.person.Hero;

/**
 * Created by mrkotyk on 16.07.17.
 */
public class HeroesController {

    private Hex[][] hexes;

        public HeroesController() {
            this.hexes = GameWorld.getInstance().getHexes();
        }

        public void toHex(Hero hero, int x, int y) {
            hero.STATE = Hero.State.WALK;
        }

        public void hexReleased(Hero hero) {
            if (hero.STATE != Hero.State.DEAD) {
                hero.STATE = Hero.State.NONE;
              //  isDirX = false;
               // isDirY = false;
            }
        }


        //главный метод класса...обновляем состояния объектов здесь
        public void update(Hero hero, float delta, int hexPressedX, int hexPressedY) {


            if (hero.STATE == Hero.State.WALK) {

                Ground gr = hexes[hexPressedX][hexPressedY].getGround();
                if(gr == null) return;
                Vector2 direction = gr.hexCenter;
                //Rectangle groundBounds = ground[hexPressedX][hexPressedY].bounds;


                if(!hero.bounds.contains(gr.hexCenter.x, hero.position.y)) {
                    if (direction.x > hero.position.x) hero.velocity.x = hero.spriteSpeed;
                    if (direction.x < hero.position.x) hero.velocity.x = -hero.spriteSpeed;
                }

                if(!hero.bounds.contains(hero.position.x, gr.hexCenter.y)) {
                    if (direction.y > hero.position.y) hero.velocity.y = hero.spriteSpeed;
                    if (direction.y < hero.position.y) hero.velocity.y = -hero.spriteSpeed;
                }
                //if(Vector2.dst2(hero.position.x,hero.position.y,direction.x,direction.y) <= 0.0005f) hexReleased();

                if(hero.bounds.contains(gr.hexCenter.x, hero.position.y)
                        && hero.bounds.contains(hero.position.x, gr.hexCenter.y)) hexReleased(hero);


                hero.update(gr.hexCenter, delta);
            }

        }
    }

