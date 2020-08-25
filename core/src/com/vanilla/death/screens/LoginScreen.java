package com.vanilla.death.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.vanilla.death.MainClass;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.multiplayer.ClientController;
import com.vanilla.death.multiplayer.Result;
import com.vanilla.death.multiplayer.ui.TestFormValidator;

/**
 * Created by hedgehog on 26.07.17.
 */
public class LoginScreen implements Screen {

    private MainClass mainClass;

    private Stage stage;

    private TestFormValidator loginPanel;
    private TestFormValidator regPanel;

    public static Result resultReg;
    public static Result resultLogin;

    MultiplayerGame gameScreen;

    //Constructor
    public LoginScreen(MainClass mainClass) {
        this.mainClass = mainClass;
    }

    @Override
    public void show() {
        VisUI.load(VisUI.SkinScale.X1);
        ClientController.getInstance();
        GameWorld.getInstance();

        gameScreen = new MultiplayerGame(mainClass);

        ClientController.getInstance().setGameScreen(gameScreen);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        loginPanel = new TestFormValidator("Вход");
        loginPanel.login();

        regPanel = new TestFormValidator("Регистрация");
        regPanel.reg();

        stage.addActor(loginPanel);
        stage.addActor(regPanel);

    }

    int i = 0;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (resultReg != null)
            switch (resultReg) {
                case REG_FAILED:
                    regPanel.setColor(Color.RED);
                    regPanel.setErrorLabelReg("Такой шикарный логин занят!");
                    break;
                case REG_SUCCESS:
                    regPanel.setColor(Color.GREEN);
                    regPanel.setErrorLabelReg("Регистрация успешно пройдена!");
                    break;
                default:
                    break;
            }

        if (resultLogin != null) {
            switch (resultLogin) {
                case LOGIN_FAILED:
                    loginPanel.setColor(Color.RED);
                    loginPanel.setErrorLabelLog("Игрок не найден!");
                    break;
                case LOGIN_SUCESS:
                    loginPanel.setColor(Color.GREEN);
                    loginPanel.setErrorLabelLog("Входим в игру...");
                    mainClass.setScreen(gameScreen);
                default:
                    break;
            }
        }
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        VisUI.dispose();
        stage.dispose();
        loginPanel.clear();
        regPanel.clear();
        resultReg = null;
        resultLogin = null;
        gameScreen = null;
    }
}
