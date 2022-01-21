package dev.slickcollections.kiwizin.hook.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.slickcollections.kiwizin.libraries.holograms.HologramLibrary;
import dev.slickcollections.kiwizin.libraries.holograms.api.Hologram;
import dev.slickcollections.kiwizin.libraries.npclib.NPCLibrary;
import dev.slickcollections.kiwizin.nms.NMS;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HologramAdapter extends PacketAdapter {
  
  public HologramAdapter() {
    super(params().plugin(NPCLibrary.getPlugin()).types(PacketType.Play.Server.SPAWN_ENTITY,
        PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.ENTITY_METADATA));
  }
  
  @Override
  public void onPacketSending(PacketEvent evt) {
    PacketContainer packet = evt.getPacket();
    
    Player player = evt.getPlayer();
    Entity entity = HologramLibrary.getHologramEntity(packet.getIntegers().read(0));
    
    Hologram hologram;
    if (entity == null || !HologramLibrary.isHologramEntity(entity) || (hologram = HologramLibrary.getHologram(entity)) == null) {
      hologram = NMS.getPreHologram(packet.getIntegers().read(0));
    }
    
    if (hologram == null || hologram.canSee(player)) {
      return;
    }
    
    evt.setCancelled(true);
  }
  
  @Override
  public void onPacketReceiving(PacketEvent evt) {}
}
