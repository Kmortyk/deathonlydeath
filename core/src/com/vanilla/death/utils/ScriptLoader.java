package com.vanilla.death.utils;

import com.badlogic.gdx.Gdx;
import com.vanilla.death.model.GameWorld;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import static org.luaj.vm2.LuaValue.NIL;
import static org.luaj.vm2.LuaValue.tableOf;

/**
 * Created by mrkotyk on 29.08.17.
 */
public class ScriptLoader{

    private static ScriptLoader object;

    public static ScriptLoader getObject(){
        if(object == null){
            object = new ScriptLoader();
        }
        return object;
    }

    private Globals globals;

    public ScriptLoader() {
        globals = JsePlatform.standardGlobals();
        globals.set("gm", CoerceJavaToLua.coerce(new GameWorldOptions()));
        globals.set("game_world", CoerceJavaToLua.coerce(GameWorld.getInstance()));
        globals.set("hero", CoerceJavaToLua.coerce(GameWorld.getInstance().getHero()));
    }

    public void callScript(String scriptName){
        globals.get("dofile").call(LuaValue.valueOf("data/scripts/"+scriptName));
    }

    public void makeScript(String scriptText){
        globals.load(scriptText).call();
    }

    private class GameWorldOptions{
        public void addHero(){
            GameWorld.getInstance().addHero(1);
            System.out.println("Added");
        }
    }
}
