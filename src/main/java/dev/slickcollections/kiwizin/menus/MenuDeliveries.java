package dev.slickcollections.kiwizin.menus;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.database.data.container.DeliveriesContainer;
import dev.slickcollections.kiwizin.deliveries.Delivery;
import dev.slickcollections.kiwizin.libraries.menu.UpdatablePlayerMenu;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuDeliveries extends UpdatablePlayerMenu {
  
  private Profile profile;
  private Map<ItemStack, Delivery> deliveries;

  public MenuDeliveries(Profile profile) {
    this(profile, false);
  }

  public MenuDeliveries(Profile profile, Boolean profileMenu) {
    super(profile.getPlayer(), "Entregas", profileMenu ? 6 : 5);
    this.profile = profile;
    this.deliveries = new HashMap<>();

    if (profileMenu) {
      this.setItem(49, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"));
    }
    
    this.update();
    this.register(Core.getInstance(), 20);
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            Delivery delivery = this.deliveries.get(item);
            if (evt.getSlot() == 49) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            } else if (delivery != null) {
              DeliveriesContainer container = this.profile.getDeliveriesContainer();
              if (container.alreadyClaimed(delivery.getId()) || !delivery.hasPermission(player)) {
                EnumSound.ENDERMAN_TELEPORT.play(this.player, 0.5F, 1.0F);
              } else {
                EnumSound.LEVEL_UP.play(this.player, 1.0F, 1.0F);
                container.claimDelivery(delivery.getId(), delivery.getDays());
                delivery.listRewards().forEach(reward -> reward.dispatch(this.profile));
                this.player.sendMessage(delivery.getMessage());
                this.player.closeInventory();
              }
            }
          }
        }
      }
    }
  }
  
  @Override
  public void update() {
    this.deliveries.clear();
    for (Delivery delivery : Delivery.listDeliveries()) {
      ItemStack item = delivery.getIcon(this.profile);
      this.setItem(delivery.getSlot(), item);
      this.deliveries.put(item, delivery);
    }
    
    this.player.updateInventory();
  }
  
  public void cancel() {
    super.cancel();
    HandlerList.unregisterAll(this);
    this.profile = null;
    this.deliveries.clear();
    this.deliveries = null;
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}
