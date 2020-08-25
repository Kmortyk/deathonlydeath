package com.vanilla.death.multiplayer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.vanilla.death.model.GameWorld;
import com.vanilla.death.model.person.Hero;

import com.vanilla.death.multiplayer.model.Player;
import com.vanilla.death.multiplayer.packet.*;
import com.vanilla.death.multiplayer.ui.TestFormValidator;
import com.vanilla.death.screens.LoginScreen;
import com.vanilla.death.screens.MultiplayerGame;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.Iterator;


/**
 * Created by hedgehog on 19.07.17.
 */
public class ClientController {

    private MultiplayerGame gameScreen;
    private static Client client;
    private static ClientController instance;

    private String room;

    public String getRoom() {
        return room;
    }

    private Player localPlayer;

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public static ClientController getInstance() {
        if (instance == null)
            instance = new ClientController();
        return instance;
    }

    public Client getClient() {
        return client;
    }

    public void updateServer(int x, int y) {
        MovePlayerPacket msg = new MovePlayerPacket();

        Gdx.app.log("Kryonet", x + "," + y);

        msg.room = room;
        msg.x = x;
        msg.y = y;

        client.sendTCP(msg);
    }


    private ClientController() {
        client = new Client();
        client.start();

        Network.register(client);
        client.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection connection) {


            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof RegisterCheckPacket) {
                    RegisterCheckPacket packet = (RegisterCheckPacket) object;
                    if (packet.check)
                        LoginScreen.resultReg = Result.REG_SUCCESS;
                    else
                        LoginScreen.resultReg = Result.REG_FAILED;
                }

                if (object instanceof LoginCheckPacket) {
                    LoginCheckPacket packet = (LoginCheckPacket) object;
                    if (packet.check) {
                        room = packet.room;
                        localPlayer = packet.player;
                        LoginScreen.resultLogin = Result.LOGIN_SUCESS;
                    } else
                        LoginScreen.resultLogin = Result.LOGIN_FAILED;
                }

                if (object instanceof RegisterPacket) {
                    RegisterPacket msg = (RegisterPacket) object;
                    GameWorld.getInstance().addHero(msg.player.id);
                    Gdx.app.log("Register Packet", "Add new hero " + msg.player.login);
                    return;
                }

                if (object instanceof UpdatePlayerPacket) {
                    UpdatePlayerPacket updatePlayer = (UpdatePlayerPacket) object;

                    for (Hero hero : GameWorld.getInstance().getHeroes()) {
                        if (hero.getId() == updatePlayer.id) {
                            gameScreen.movingForTouch(updatePlayer.x, updatePlayer.y, hero);
                        }
                    }
                }

                if (object instanceof RoomPacket) {
                    RoomPacket room = (RoomPacket) object;
                    boolean hasHero = false;

                    synchronized (connection) {
                        for (Hero hero : GameWorld.getInstance().getHeroes()) {
                            if (hero.getId() == room.player.id)
                                hasHero = true;
                        }

                        if (!hasHero) {
                            GameWorld.getInstance().addHero(room.player.id, room.player.x, room.player.y);
                            gameScreen.movingForTouch(room.player.x, room.player.y, GameWorld.getInstance().getHeroes().get(GameWorld.getInstance().getHeroes().size() - 1));
                        }
                    }
                }

                if (object instanceof RemovePlayerPacket) {
                    RemovePlayerPacket packet = (RemovePlayerPacket) object;

                    Iterator<Hero> iter = GameWorld.getInstance().getHeroes().iterator();
                    while (iter.hasNext()) {
                        Hero hero = iter.next();
                        if (hero.getId() == packet.player.id) {
                            iter.remove();
                            System.out.println("Remove succesful");
                            return;
                        }
                    }
                }
            }

            @Override
            public void disconnected(Connection connection) {
                client.close();
            }
        }));

        try

        {
            client.connect(5000, "localhost", Network.port);
        } catch (
                Exception ex)

        {
            ex.printStackTrace();
        }

    }

    public void setGameScreen(MultiplayerGame gameScreen) {
        this.gameScreen = gameScreen;
    }

    public static boolean isUsed() {
        return instance != null;
    }

    public void dispose() {
        gameScreen.dispose();
        try {
            client.close();
            client.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
        localPlayer = null;
        instance = null;
    }
}
