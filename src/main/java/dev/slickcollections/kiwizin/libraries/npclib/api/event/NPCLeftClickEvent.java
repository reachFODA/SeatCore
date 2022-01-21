package dev.slickcollections.kiwizin.libraries.npclib.api.event;

import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class NPCLeftClickEvent extends NPCEvent {
  
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final NPC npc;
  private final Player player;
  
  public NPCLeftClickEvent(NPC npc, Player clicked) {
    this.npc = npc;
    this.player = clicked;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
  
  public NPC getNPC() {
    return npc;
  }
  
  public Player getPlayer() {
    return player;
  }
  
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
