package com.vanilla.death.multiplayer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.vanilla.death.multiplayer.model.*;
import com.vanilla.death.multiplayer.packet.*;


/**
 * Created by hedgehog on 24.07.17.
 */
public class Network {
    static public final int port = 54555;

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
//        kryo.register(Login.class);
//        kryo.register(RegistrationRequired.class);

        kryo.register(RegisterCheckPacket.class);
        kryo.register(LoginCheckPacket.class);

        kryo.register(RegisterPacket.class);
        kryo.register(RoomPacket.class);
        kryo.register(MovePlayerPacket.class);
        kryo.register(UpdatePlayerPacket.class);
        kryo.register(RemovePlayerPacket.class);

        kryo.register(GameRoom.class);
        kryo.register(Player.class);

//        kryo.register(AddPlayer.class);
//        kryo.register(RemovePlayer.class);

    }
//
//    static public class Login {
//        public String name;
//    }

//    static public class RegistrationRequired {
//    }

//
//    static public class AddPlayer {
//        public Player player;
//    }
//
//    static public class RemovePlayer {
//        public int id;
//    }
}
