package com.vanilla.death.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class InputManager implements InputProcessor {

    private static InputManager object;

    private InputMultiplexer allInputProcessors;

    private InputManager() {
        allInputProcessors = new InputMultiplexer();
        allInputProcessors.addProcessor(this);
        Gdx.input.setInputProcessor(allInputProcessors);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public static InputManager getObject() {
        if (object == null) {
            object = new InputManager();
        }
        return object;
    }

    public void addInputProcessor(InputProcessor newInputProcessor) {
        allInputProcessors.addProcessor(newInputProcessor);
    }

    public void removeInputProcessor(InputProcessor inputProcessor) {
        allInputProcessors.removeProcessor(inputProcessor);
    }

    @SuppressWarnings("unused")
    public void resetInputManager() {
        Gdx.input.setInputProcessor(null);
        allInputProcessors.clear();
    }

    public void dispose() {
        allInputProcessors.clear();
        object = null;
    }

    public static boolean isUsed() {
        return object != null;
    }
}
