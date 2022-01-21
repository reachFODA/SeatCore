package dev.slickcollections.kiwizin.libraries.npclib.api;

import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface EntityController {
  
  void spawn(Location location, NPC npc);
  
  void remove();
  
  Entity getBukkitEntity();
}
