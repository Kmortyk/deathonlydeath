package com.vanilla.death.multiplayer.packet;

import com.vanilla.death.multiplayer.model.Player;

/**
 * Created by hedgehog on 27.07.17.
 */
public class LoginCheckPacket {
    public String login, room;
    public boolean check;
    public Player player;
}
