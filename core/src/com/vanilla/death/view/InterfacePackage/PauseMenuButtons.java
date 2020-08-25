package com.vanilla.death.view.InterfacePackage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.vanilla.death.utils.FontManager;
import com.vanilla.death.utils.ResourceManager;

public class PauseMenuButtons {

    private static PauseMenuButtons object;

    private TextButton[] textButtons;
    private Table pauseMenuTable;

    private PauseMenuButtons() {
        I18NBundle localizationBundle = ResourceManager.getInstance().getLocalizationBundle();
        Skin defaultSkin = FontManager.getObject().getDefaultSkin();
        textButtons = new TextButton[] {
                new TextButton(localizationBundle.get("BackPauseButton"), defaultSkin),
                new TextButton(localizationBundle.get("MainMenuPauseButton"), defaultSkin)
        };
        pauseMenuTable = new Table();
        pauseMenuTable.setFillParent(true);
        for (int i = 0; i < textButtons.length; i++) {
            textButtons[i].getLabel().getStyle().font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            textButtons[i].getLabel().setFontScale(1);
            switch (i) {
                case 0:
                    textButtons[i].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            InterfaceManager.getObject().changeInterfaceState("GAME");
                        }
                    });
                    break;
                case 1:
                    textButtons[i].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            InterfaceManager.getObject().changeInterfaceState("EXIT");
                        }
                    });
            }
            pauseMenuTable.add(textButtons[i]);
        }
        pauseMenuTable.center();
    }

    Table getPauseMenuTable() {
        return pauseMenuTable;
    }

    public static PauseMenuButtons getObject() {
        if (object == null) {
            object = new PauseMenuButtons();
        }
        return object;
    }

    public void dispose() {
        pauseMenuTable.clear();
        textButtons = null;
        object = null;
    }

    public static boolean isUsed() {
        return object != null;
    }
}
