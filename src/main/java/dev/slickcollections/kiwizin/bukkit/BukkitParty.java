package dev.slickcollections.kiwizin.bukkit;

import dev.slickcollections.kiwizin.party.Party;

public class BukkitParty extends Party {
  
  public BukkitParty(String leader, int slots) {
    super(leader, slots);
  }
  
  @Override
  public void delete() {
    BukkitPartyManager.listParties().remove(this);
    this.destroy();
  }
}
