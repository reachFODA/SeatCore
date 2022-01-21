package dev.slickcollections.kiwizin.libraries.npclib.trait;

import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import org.bukkit.Location;

public class CurrentLocation extends NPCTrait {
  
  private Location location = new Location(null, 0, 0, 0);
  
  public CurrentLocation(NPC npc) {
    super(npc);
  }
  
  public Location getLocation() {
    return location;
  }
  
  public void setLocation(Location location) {
    this.location = location;
  }
}
