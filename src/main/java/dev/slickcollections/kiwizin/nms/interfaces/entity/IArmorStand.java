package dev.slickcollections.kiwizin.nms.interfaces.entity;

import org.bukkit.entity.ArmorStand;
import dev.slickcollections.kiwizin.libraries.holograms.api.HologramLine;

public interface IArmorStand {
  
  int getId();

  void setName(String name);

  void setLocation(double x, double y, double z);

  boolean isDead();

  void killEntity();

  ArmorStand getEntity();

  HologramLine getLine();
}
