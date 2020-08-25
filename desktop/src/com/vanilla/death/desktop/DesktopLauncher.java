package com.vanilla.death.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vanilla.death.MainClass;

public class 	DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Death Only Death";
		//This size like Android smartphone
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new MainClass(), config);
	}
}
