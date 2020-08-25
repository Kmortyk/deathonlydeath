package com.vanilla.death.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.jetbrains.annotations.Contract;

public class FontManager {

    private static FontManager object;

    private Skin defaultSkin;
    private GlyphLayout glyphLayout;

    private FontManager() {
        defaultSkin = ResourceManager.getInstance().getInterfaceSkin();
        glyphLayout = new GlyphLayout();
    }

    public float getHeightOfTheWrappedText(Label text, float width) {
        glyphLayout.setText(text.getStyle().font, text.getText().toString(), Color.BLACK, width, 0, true);
        System.out.println(glyphLayout.height);
        return glyphLayout.height;
    }

    public float getScaleForLabel(Label text, float width, float height, float startScale) {
        glyphLayout.setText(text.getStyle().font, text.getText().toString(), Color.BLACK, width, 0, true);
        if (glyphLayout.height > height) {
            return getScaleForLabel(text, width * 1.1f, height * 1.1f, startScale * 0.9f);
        } else {
            return startScale;
        }
    }


    public static FontManager getObject() {
        if (object == null) {
            object = new FontManager();
        }
        return object;
    }

    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    public void dispose() {
        defaultSkin.dispose();
        glyphLayout = null;
        object = null;
    }

    @Contract(pure = true)
    public static boolean isUsed() {
        return object != null;
    }
}
