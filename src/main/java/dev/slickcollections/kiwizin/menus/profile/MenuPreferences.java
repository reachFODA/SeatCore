package dev.slickcollections.kiwizin.menus.profile;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.database.data.container.PreferencesContainer;
import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.menus.MenuProfile;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.enums.*;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuPreferences extends PlayerMenu {
  
  public MenuPreferences(Profile profile) {
    super(profile.getPlayer(), "Preferências", 5);
    
    PreferencesContainer pc = profile.getPreferencesContainer();
    
    PlayerVisibility pv = pc.getPlayerVisibility();
    this.setItem(11, BukkitUtils.deserializeItemStack("347 : 1 : nome>&aJogadores : desc>&7Ative ou desative os\n&7jogadores no lobby."));
    this.setItem(20, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + pv.getInkSack() + " : 1 : nome>" + pv.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pv.getName()) + "\n \n&eClique para modificar!"));
    
    PrivateMessages pm = pc.getPrivateMessages();
    this.setItem(12, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>&aMensagens privadas : desc>&7Ative ou desative as mensagens\n&7enviadas através do tell."));
    this.setItem(21, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + pm.getInkSack() + " : 1 : nome>" + pm.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pm.getName()) + "\n \n&eClique para modificar!"));
    
    BloodAndGore bg = pc.getBloodAndGore();
    this.setItem(14, BukkitUtils.deserializeItemStack("REDSTONE : 1 : nome>&aViolência : desc>&7Ative ou desative as partículas\n&7de sangue no PvP."));
    this.setItem(23, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + bg.getInkSack() + " : 1 : nome>" + bg.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(bg.getName()) + "\n \n&eClique para modificar!"));

    ProtectionLobby pl = pc.getProtectionLobby();
    this.setItem(15, BukkitUtils.deserializeItemStack("NETHER_STAR : 1 : nome>&aProteção no /lobby : desc>&7Ative ou desative o pedido de\n&7confirmação ao utilizar /lobby."));
    this.setItem(24, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + pl.getInkSack() + " : 1 : nome>" + pl.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pl.getName()) + "\n \n&eClique para modificar!"));

    ChatMention cm = pc.getChatMention();
    this.setItem(13, BukkitUtils.deserializeItemStack("MAP : 1 : esconder>tudo : nome>&aMenção no Chat : desc>&7Ative ou desative a menção no\n&7chat."));
    this.setItem(22, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + cm.getInkSack() + " : 1 : nome>" + cm.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(cm.getName()) + "\n \n&eClique para modificar!"));

    AutoGG ag = pc.getAutoGG();
    this.setItem(10, BukkitUtils.deserializeItemStack("FIREWORK : 1 : esconder>tudo : nome>&aAutoGG : desc>&7Ative ou desative o auto gg."));
    this.setItem(19, BukkitUtils.deserializeItemStack(
        "INK_SACK:" + ag.getInkSack() + " : 1 : nome>" + ag.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(ag.getName()) + "\n \n&eClique para modificar!"));

    this.setItem(40, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"));
    
    this.register(Core.getInstance());
    this.open();
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      
      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }
        
        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          
          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 10 || evt.getSlot() == 11 || evt.getSlot() == 13 || evt.getSlot() == 12 || evt.getSlot() == 14 || evt.getSlot() == 15 || evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 19) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeAutoGG();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 20) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePlayerVisibility();
              if (!profile.playingGame()) {
                profile.refreshPlayers();
              }
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 21) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePrivateMessages();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 22) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeChatMention();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 23) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeBloodAndGore();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 24) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeProtectionLobby();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 40) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            }
          }
        }
      }
    }
  }
  
  public void cancel() {
    HandlerList.unregisterAll(this);
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
