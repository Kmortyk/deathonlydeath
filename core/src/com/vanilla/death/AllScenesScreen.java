package com.vanilla.death;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.vanilla.death.screens.LoginScreen;
import com.vanilla.death.utils.FontManager;
import com.vanilla.death.utils.InputManager;

public class AllScenesScreen implements Screen {

    private SpriteBatch spriteBatch;
    private MainGameScreen mainGameScreen;
    private MainClass mainClass;
    private TextButton[] textButtons;
    private final int IMAGE_BUTTONS_LENGTH = 2;
    private Stage stage;

    //Constructor
    public AllScenesScreen(MainClass mainClass) {
        this.mainClass = mainClass;
    }

    @Override
    public void show() {
        //Initializing all variables
        spriteBatch = new SpriteBatch();
        stage = new Stage(MainClass.getViewportInterfaceStage());
        mainGameScreen = new MainGameScreen(mainClass);
        setButtons();
        setTableOfButtons();
        InputManager.getObject().addInputProcessor(stage);
    }

    private void setTableOfButtons() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        for (int i = 0; i < IMAGE_BUTTONS_LENGTH; i++) {
            if (i % 2 == 0 ) {
                table.row();
            }
            table.add(textButtons[i]).width(300).height(100);
        }
        stage.addActor(table);
    }

    private void setButtons() {
        textButtons = new TextButton[IMAGE_BUTTONS_LENGTH];
        for (int i = 0; i < IMAGE_BUTTONS_LENGTH; i++) {
            textButtons[i] = new TextButton("" + i, FontManager.getObject().getDefaultSkin());
            textButtons[i].getLabel().setWrap(true);
            //Put your action here
            switch (i) {
                case 0:
                    textButtons[i].setText("MainGameScreen");
                    textButtons[i].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            mainClass.setScreen(mainGameScreen);
                        }
                    });
                    break;
                case 1:
                    textButtons[i].setText("LoginScreen");
                    textButtons[i].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            mainClass.setScreen(new LoginScreen(mainClass));
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        InputManager.getObject().removeInputProcessor(stage);
        dispose();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        textButtons = null;
    }
}
