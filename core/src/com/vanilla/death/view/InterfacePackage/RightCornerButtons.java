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

public class RightCornerButtons {

    private static RightCornerButtons object;

    private TextButton[] textButtons;
    private Table rightCornerTable;

    private RightCornerButtons() {
        I18NBundle localizationBundle = ResourceManager.getInstance().getLocalizationBundle();
        Skin defaultSkin = FontManager.getObject().getDefaultSkin();
        textButtons = new TextButton[] {
                new TextButton(localizationBundle.get("PauseMenuButton"), defaultSkin),
                new TextButton(localizationBundle.get("ChatMenuButton"), defaultSkin)
        };
        rightCornerTable = new Table();
        rightCornerTable.setFillParent(true);
        for (int i = 0; i < textButtons.length; i++) {
            textButtons[i].getLabel().getStyle().font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            textButtons[i].getLabel().setFontScale(1);
            switch (i) {
                case 0:
                    textButtons[i].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            InterfaceManager.getObject().changeInterfaceState("PAUSE");
                        }
                    });
                    break;
                case 1:
                    textButtons[i].addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            InterfaceManager.getObject().changeInterfaceState("CHAT");
                        }
                    });
            }
            rightCornerTable.add(textButtons[i]);
        }
        rightCornerTable.right().bottom();
    }

    public static RightCornerButtons getObject() {
        if (object == null) {
            object = new RightCornerButtons();
        }
        return object;
    }

    Table getRightCornerTable() {
        return rightCornerTable;
    }

    public void dispose() {
        textButtons = null;
        rightCornerTable.clear();
        object = null;
    }

    public static boolean isUsed() {
        return object != null;
    }
}
