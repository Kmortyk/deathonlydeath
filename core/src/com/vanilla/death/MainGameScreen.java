package com.vanilla.death;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.vanilla.death.algorithm.PathFinder;
import com.vanilla.death.controller.BattleModeController;
import com.vanilla.death.controller.DialogueController;
import com.vanilla.death.controller.HeroController;
import com.vanilla.death.controller.MobsController;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.utils.InputManager;
import com.vanilla.death.utils.ScriptLoader;
import com.vanilla.death.view.InterfacePackage.InterfaceManager;
import com.vanilla.death.view.InterfacePackage.InventoryBox;
import com.vanilla.death.view.WorldRenderer;

import java.util.logging.Logger;

/**
 * Created by mrkotyk on 22.06.17.
 */
public class MainGameScreen implements Screen, InputProcessor {

    private WorldRenderer renderer;
    private HeroController	controller;
    private MobsController mobsController;
    private MainClass mainClass;
    //object manager, data


    MainGameScreen(MainClass mainClass) {
        this.mainClass = mainClass;
    }

    private int width, height;

    private Vector3 touchPosition = new Vector3();

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
            //Logger log = Logger.getLogger(MainGameScreen.class.getName());
            //log.info("CLICKED! x="+x+" y="+y);

            touchPosition.set(x,y,0);
            touchPosition = renderer.CameraConvert(touchPosition);

            //if (!Gdx.app.getType().equals(Application.ApplicationType.Android)) return false;

            controller.touched(touchPosition.x,touchPosition.y);
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

    /*public boolean touchMoved(int x, int y) {
        return false;
    }*/

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
        GameWorld.getInstance();

        GameWorld.getInstance().setLocalID(0);
        GameWorld.getInstance().addHero(0);

        renderer = new WorldRenderer(mainClass);
        controller = new HeroController();
        DialogueController.getObject();
        BattleModeController.getObject();
        mobsController = new MobsController();
        InputManager.getObject().addInputProcessor(this);
        //Gdx.input.setInputProcessor(this);
        ScriptLoader.getObject();
        //scriptLoader.callScript("test.lua");
       // scriptLoader.makeScript("gm:addHero()");
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void hide() {
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
        try {
            Gdx.input.setInputProcessor(null);
            BattleModeController.getObject().dispose();
            PathFinder.getObject().dispose();
            InterfaceManager.getObject().dispose();
            GameWorld.getInstance().dispose();
            InputManager.getObject().dispose();
            DialogueController.getObject().dispose();
            InventoryBox.getObject().dispose();
            renderer.dispose();
            controller = null;
            mobsController = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        mobsController.update(delta);
        renderer.render();
        //System.out.println(mainClass.getInputProcessor().inputMultiplexer.getProcessors().size);

        //System.out.println("Start id check");

        //for (Hero hero : GameWorld.getInstance().getHeroes()) {
        //    System.out.println("ID = " + hero.getId());
        //}
    }

}
