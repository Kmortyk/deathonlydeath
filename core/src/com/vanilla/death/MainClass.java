package com.vanilla.death;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.multiplayer.ClientController;
import com.vanilla.death.utils.*;
import com.vanilla.death.view.CameraScreen;
import com.vanilla.death.view.InterfacePackage.*;
import org.jetbrains.annotations.Contract;

public class MainClass extends Game {

	private static Viewport viewport;
	private static Viewport viewportInterfaceStage;
	private int disposeIndex = 0;

	//необходимо реализовать callback для перехода на другой экран
	@Override
	public void create () {
		//setup viewport
		viewportInterfaceStage = new ExtendViewport(800, 480);//Viewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), CameraScreen.getObject());
		InterfaceManager.mainClass = this;
		setScreen(new AllScenesScreen(this));
	}

	@SuppressWarnings("all") //FIXME
	public void setViewport(OrthographicCamera orthographicCamera, float minWorldWidth, float minWorldHeight) {
		viewport = new ExtendViewport(minWorldWidth, minWorldHeight, orthographicCamera);
		viewport.apply();
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void resize(int width, int height) {
		try {
			viewportInterfaceStage.update(width, height, true);
			//viewportInterfaceStage.setWorldSize();
			viewport.update(width,height, true);
		} catch (NullPointerException nullPointerException) {
			System.out.println("Viewport not set");
		}
	}

	@Contract(pure = true)
	public static Viewport getViewportInterfaceStage() {
		return viewportInterfaceStage;
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void dispose() {
		super.dispose();
		disposeIndex++;
		try {
			switch (disposeIndex) {
				case 0:
					if (ClientController.isUsed()) ClientController.getInstance().dispose();
					disposeIndex++;
				case 1:
					if (Data.isUsed()) Data.getInstance().dispose();
					disposeIndex++;
				case 2:
					if (FontManager.isUsed()) FontManager.getObject().dispose();
					disposeIndex++;
				case 3:
					if (InputManager.isUsed()) InputManager.getObject().dispose();
					disposeIndex++;
				case 4:
					if (ResourceManager.isUsed()) ResourceManager.getInstance().dispose();
					disposeIndex++;
				case 5:
					if (InterfaceManager.isUsed()) InterfaceManager.getObject().dispose();
					disposeIndex++;
				case 6:
					if (RightCornerButtons.isUsed()) RightCornerButtons.getObject().dispose();
					disposeIndex++;
				case 7:
					if (PauseMenuButtons.isUsed()) PauseMenuButtons.getObject().dispose();
					disposeIndex++;
				case 8:
					if (CameraScreen.isUsed()) CameraScreen.getObject().dispose();
					disposeIndex++;
				case 9:
					if (GameWorld.isUsed()) GameWorld.getInstance().dispose();
					disposeIndex++;
				case 10:
					if (UsersGUI.usUsed()) UsersGUI.getObject().dispose();
					disposeIndex++;
				case 11:
					if (DialogueBox.isUsed()) DialogueBox.getObject().dispose();
					disposeIndex++;
				case 12:
					if (InventoryBox.isUsed()) InventoryBox.getObject().dispose();
					disposeIndex++;
				default:
					viewportInterfaceStage = null;
			}
        } catch (Exception e) {
		    e.printStackTrace();
		    dispose();
        }
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void render() {
		super.render();
	}
}
