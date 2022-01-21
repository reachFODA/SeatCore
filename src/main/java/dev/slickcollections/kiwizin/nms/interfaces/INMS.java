package dev.slickcollections.kiwizin.nms.interfaces;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import dev.slickcollections.kiwizin.libraries.holograms.api.Hologram;
import dev.slickcollections.kiwizin.libraries.holograms.api.HologramLine;
import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPCAnimation;
import dev.slickcollections.kiwizin.libraries.npclib.npc.skin.SkinnableEntity;
import dev.slickcollections.kiwizin.nms.interfaces.entity.IArmorStand;
import dev.slickcollections.kiwizin.nms.interfaces.entity.IItem;
import dev.slickcollections.kiwizin.nms.interfaces.entity.ISlime;

import java.util.Collection;

public interface INMS {

  IArmorStand createArmorStand(Location location, String name, HologramLine line);

  IItem createItem(Location location, ItemStack item, HologramLine line);

  ISlime createSlime(Location location, HologramLine line);

  Hologram getHologram(Entity entity);

  Hologram getPreHologram(int entityId);

  boolean isHologramEntity(Entity entity);
  
  void playChestAction(Location location, boolean open);
  
  void playAnimation(Entity entity, NPCAnimation animation);

  void setValueAndSignature(Player player, String value, String signature);

  void sendTabListAdd(Player player, Player listPlayer);

  void sendTabListRemove(Player player, Collection<SkinnableEntity> skinnableEntities);

  void sendTabListRemove(Player player, Player listPlayer);

  void removeFromPlayerList(Player player);

  void removeFromServerPlayerList(Player player);

  boolean addToWorld(World world, Entity entity, SpawnReason reason);

  void removeFromWorld(Entity entity);

  void replaceTrackerEntry(Player player);

  void sendPacket(Player player, Object packet);

  void look(Entity entity, float yaw, float pitch);

  void setHeadYaw(Entity entity, float yaw);

  void setStepHeight(LivingEntity entity, float height);

  float getStepHeight(LivingEntity entity);

  SkinnableEntity getSkinnable(Entity entity);

  void flyingMoveLogic(LivingEntity entity, float f, float f1);

  void sendActionBar(Player player, String message);

  void sendTitle(Player player, String title, String subtitle);

  void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);

  void sendTabHeaderFooter(Player player, String header, String footer);

  void refreshPlayer(Player player);
}
