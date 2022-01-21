package dev.slickcollections.kiwizin.nms.v1_8_R3.network;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import dev.slickcollections.kiwizin.nms.v1_8_R3.entity.EntityNPCPlayer;

public class EmptyNetHandler extends PlayerConnection {

  public EmptyNetHandler(EntityNPCPlayer entityplayer) {
    super(entityplayer.server, new EmptyNetworkManager(), entityplayer);
  }

  @Override
  public void sendPacket(Packet packet) {
    // nao envie packets para um NPC.
  }
}
