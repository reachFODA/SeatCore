package dev.slickcollections.kiwizin.nms.interfaces.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import dev.slickcollections.kiwizin.libraries.holograms.api.HologramLine;

public interface ISlime {

  public void setPassengerOf(Entity entity);

  public void setLocation(double x, double y, double z);

  public boolean isDead();

  public void killEntity();

  public Slime getEntity();

  public HologramLine getLine();
}
