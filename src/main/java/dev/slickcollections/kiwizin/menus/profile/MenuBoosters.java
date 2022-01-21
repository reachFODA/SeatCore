package dev.slickcollections.kiwizin.menus.profile;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.booster.Booster;
import dev.slickcollections.kiwizin.booster.NetworkBooster;
import dev.slickcollections.kiwizin.libraries.menu.PlayerMenu;
import dev.slickcollections.kiwizin.menus.MenuProfile;
import dev.slickcollections.kiwizin.menus.profile.boosters.MenuBoostersList;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.TimeUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuBoosters extends PlayerMenu {
  
  public MenuBoosters(Profile profile) {
    super(profile.getPlayer(), "Multiplicadores", 4);
    
    this.setItem(12, BukkitUtils.deserializeItemStack(
        "POTION : 1 : nome>&aMultiplicadores Pessoais : desc>&7Concede um &6Multiplicador de Coins &7apenas\n&7para &bVOCÊ &7em todos os minigames do servidor\n&7por um curto período de tempo.\n \n&eClique para ver seus multiplicadores!"));
    this.setItem(14, BukkitUtils.deserializeItemStack(
        "POTION:8232 : 1 : esconder>tudo : nome>&aMultiplicadores Gerais : desc>&7Concede um &6Multiplicador de Coins &7para\n&bTODOS &7os jogadores em apenas um minigame\n&7por um curto período de tempo.\n \n&eClique para ver seus multiplicadores!"));
    
    String booster = profile.getBoostersContainer().getEnabled();
    StringBuilder result = new StringBuilder(), network = new StringBuilder();
    for (int index = 0; index < Core.minigames.size(); index++) {
      String minigame = Core.minigames.get(index);
      NetworkBooster nb = Booster.getNetworkBooster(minigame);
      network.append(" &8• &b").append(minigame).append(": ")
          .append(nb == null ? "&cDesativado" : "&6" + nb.getMultiplier() + "x &7por " + Role.getColored(nb.getBooster()) + " &8(" + TimeUtils.getTimeUntil(nb.getExpires()) + ")")
          .append(index + 1 == Core.minigames.size() ? "" : "\n");
    }
    result.append("&fMultiplicador Pessoal ativo:\n ");
    if (booster == null) {
      result.append("&cVocê não possui nenhum multiplicador ativo.");
    } else {
      String[] splitted = booster.split(":");
      double all = 50.0 * Double.parseDouble(splitted[0]);
      result.append("&8• &6Multiplicador ").append(splitted[0]).append("x &8(").append(TimeUtils.getTimeUntil(Long.parseLong(splitted[1])))
          .append(")\n \n&fCálculo:\n &7Com o multiplicador ativo ao receber &650 Coins &7o\n &7total recebido será equivalente a &6").append((int) all).append(" Coins&7.");
    }
    this.setItem(30, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"));
    this.setItem(31, BukkitUtils.deserializeItemStack(
        "PAPER : 1 : nome>&aMultiplicadores de Crédito : desc>&7Os Multiplicadores são acumulativos. Quanto mais\n&7multiplicadores ativos, maior será o bônus recebido.\n \n&fMultiplicadores Gerais:\n" + network + "\n \n" + result));
    
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
            if (evt.getSlot() == 12) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              new MenuBoostersList<>(profile, "Pessoais", Booster.BoosterType.PRIVATE);
            } else if (evt.getSlot() == 14) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              new MenuBoostersList<>(profile, "Gerais", Booster.BoosterType.NETWORK);
            } else if (evt.getSlot() == 30) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            } else if (evt.getSlot() == 31) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
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
