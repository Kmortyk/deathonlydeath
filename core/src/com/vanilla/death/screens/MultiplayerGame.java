package com.vanilla.death.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.vanilla.death.MainClass;
import com.vanilla.death.MainGameScreen;
import com.vanilla.death.controller.HeroesController;
import com.vanilla.death.controller.MobsController;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.inanimate.Ground;
import com.vanilla.death.model.person.Hero;
import com.vanilla.death.multiplayer.ClientController;
import com.vanilla.death.multiplayer.packet.RegisterPacket;
import com.vanilla.death.utils.InputManager;
import com.vanilla.death.view.WorldRenderer;



import java.util.logging.Logger;


/**
 * Created by hedgehog on 14.07.17.
 */

public class MultiplayerGame implements Screen, InputProcessor {

    private final float UPDATE_TIME = 1/60f;
    float timer;

    private WorldRenderer renderer;
    private HeroesController heroesController;
    private MobsController mobsController;

    private MainClass mainClass;

    private int width, height;
    //private int length = 0;

    private Vector3 touchPosition = new Vector3();

    public MultiplayerGame(MainClass mainClass) {
        this.mainClass = mainClass;
    }

    @Override
    public boolean keyDown(int keycode) {return true;}
    @Override
    public boolean keyUp(int keycode) {return true;}
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        Logger log = Logger.getLogger(MainGameScreen.class.getName());
        log.info("CLICKED! x="+x+" y="+y);

        touchPosition.set(x,y,0);
        touchPosition = renderer.CameraConvert(touchPosition);


        //if (!Gdx.app.getType().equals(Application.ApplicationType.Android)) return false;

        for(Ground gr: GameWorld.getInstance().getGround()){
            if(gr==null) continue;
            if(gr.hexBounds.contains(touchPosition.x,touchPosition.y)){

                log.info("I: "+gr.getI()+" J:"+gr.getJ());
                //log.info(GameWorld.getInstance().getGround()[i][j].name+"=====================================");

                ChangeNavigation(gr.getI(),gr.getJ(), GameWorld.getInstance().getHero());


                GameWorld.getInstance().getHero().setHexPressedX(gr.getI());
                GameWorld.getInstance().getHero().setHexPressedY(gr.getJ());


            }
        }
        ClientController.getInstance().updateServer(  GameWorld.getInstance().getHero().getHexPressedX()
                , GameWorld.getInstance().getHero().getHexPressedY());
        //ChangeNavigation(x,y);
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(Application.ApplicationType.Android)) return false;
        //controller.hexReleased();
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        //ChangeNavigation(x,y);
        return false;
    }

    public boolean touchMoved(int x, int y) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }


    @Override
    public void show() {

        GameWorld.getInstance().setLocalID(ClientController.getInstance().getClient().getID());
        GameWorld.getInstance().addHero(ClientController.getInstance().getClient().getID());

        RegisterPacket reg = new RegisterPacket();

        reg.player = ClientController.getInstance().getLocalPlayer();
        reg.room = ClientController.getInstance().getRoom();
        ClientController.getInstance().getClient().sendTCP(reg);

        heroesController = new HeroesController();
        renderer = new WorldRenderer(mainClass);

        mobsController = new MobsController();

        InputManager.getObject().addInputProcessor(this);

    }

    @Override
    public void resize(int width, int height) {
        if (renderer != null) renderer.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void hide() {
        InputManager.getObject().removeInputProcessor(this);
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        renderer.dispose();
        heroesController = null;
        mobsController = null;
        touchPosition = null;
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mobsController.update(delta);

        for (Hero hero : GameWorld.getInstance().getHeroes()) {
            heroesController.update(hero, delta, hero.getHexPressedX(), hero.getHexPressedY());

        }

        renderer.render();
    }

    private void ChangeNavigation(int x, int y, Hero hero){
        if (hero != null) {
            heroesController.hexReleased(hero);
            heroesController.toHex(hero, x, y);
        }
    }

    public void movingForTouch(int x, int y, Hero hero) {
//        Vector3 vector3 = new Vector3(x, y, 0);
//        vector3 = renderer.CameraConvert(vector3);

        ChangeNavigation(x, y, hero);

        hero.setHexPressedX(x);
        hero.setHexPressedY(y);


//        for(Ground gr: GameWorld.getInstance().getGround()){
//            if (gr == null) continue;
//            if(gr.hexBounds.contains(vector3.x, vector3.y)){
//
//                for (Hero _hero : GameWorld.getInstance().getHeroes()) {
//                    if (_hero.getId() == hero.getId()){
//                        _hero.setHexPressedX(gr.getI());
//                        _hero.setHexPressedY(gr.getJ());
//                        ChangeNavigation(gr.getI(),gr.getJ(), hero);
//                    }
//                }
//
//
//            }
//
//        }
    }
}
