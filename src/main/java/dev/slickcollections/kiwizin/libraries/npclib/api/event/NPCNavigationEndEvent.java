package dev.slickcollections.kiwizin.libraries.npclib.api.event;

import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import org.bukkit.event.HandlerList;

public class NPCNavigationEndEvent extends NPCEvent {
  
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final NPC npc;
  
  public NPCNavigationEndEvent(NPC npc) {
    this.npc = npc;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
  
  public NPC getNPC() {
    return npc;
  }
  
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
