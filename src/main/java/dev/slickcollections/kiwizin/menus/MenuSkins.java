package dev.slickcollections.kiwizin.menus;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.database.data.container.SkinsContainer;
import dev.slickcollections.kiwizin.libraries.menu.PagedPlayerMenu;
import dev.slickcollections.kiwizin.listeners.Listeners;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

public class MenuSkins extends PagedPlayerMenu {

  private static final SimpleDateFormat SDF = new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm",
      Locale.forLanguageTag("pt-BR"));
  private Map<ItemStack, String> skins = new HashMap<>();

  public MenuSkins(Profile profile) {
    super(profile.getPlayer(), "Lista de Skins", (profile
        .getSkinListContainer().getSkins().size() / 7) + 4);
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

    SkinsContainer skinsContainer = profile.getSkinsContainer();

    String selectedSkin = skinsContainer == null ? "none" : skinsContainer.getSkin();

    List<ItemStack> items = new ArrayList<>();
    Map<String, String> skinsList = profile.getSkinListContainer().getSkins();
    skinsList.forEach((key, value) -> {
      String skin = value.split(":")[1];
      ItemStack icon = BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : skin>" + skin + " : nome>&a" + key
          + " : desc>&fUsado pela última vez:\n&a➟ " + SDF.format(Long.parseLong(value.split(":")[0])) + "\n \n" + (selectedSkin.equals(key) ? "§eSelecionado." : "§eClique para usar\n&6Clique com shift direito para remover."));
      items.add(icon);
      this.skins.put(icon, key);
    });

    this.removeSlotsWith(BukkitUtils.deserializeItemStack(
        "BOOK_AND_QUILL : 1 : nome>&aEscolher uma Skin : desc>&7Você pode escolher uma nova skin\n&7para ser utilizada em sua conta.\n \n&fComando: &7/skin [nome]\n \n&eClique para escolher!"), (this.rows * 9) - 6);

    this.removeSlotsWith(BukkitUtils.deserializeItemStack(
        "BARRIER : 1 : nome>&aAtualizar sua Skin : desc>&7Restaura a sua skin para a skin\n&7da sua conta do Minecraft.\n \n&fComando: &7/skin atualizar\n \n&eClique para atualizar!"), (this.rows * 9) - 5);

    this.removeSlotsWith(BukkitUtils.deserializeItemStack(
        "SKULL_ITEM:3 : 1 : nome>&aAjuda : desc>&7As ações disponíveis nesse menu também\n&7podem ser realizadas por comandos.\n \n&fComando: &7/skin ajuda\n \n&eClique para ver mais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzE4MDc5ZDU4NDc0MTZhYmY0NGU4YzJmZWMyY2NkNDRmMDhkNzM2Y2E4ZTUxZjk1YTQzNmQ4NWY2NDNmYmMifX19"), (this.rows * 9) - 4);

    this.removeSlotsWith(BukkitUtils.deserializeItemStack("BOOK : 1 : nome>&aBiblioteca : desc>&7Confira a biblioteca de\n&7skins customizadas disponibilizadas\n&7gratuitamente.\n \n&eClique para ver mais!"), (this.rows * 9) - 2);

    this.setItems(items);
    items.clear();
    skinsList.clear();

    this.register(Core.getInstance());
    this.open();
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getCurrentInventory())) {
      evt.setCancelled(true);

      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
          ItemStack item = evt.getCurrentItem();

          if (item != null && item.getType() != Material.AIR) {
            String skinName = this.skins.get(item);

            if (evt.getSlot() == (this.rows * 9) - 2) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuSkinsLibrary(profile);
            } else if (evt.getSlot() == (this.rows * 9) - 6) {
              player.closeInventory();
              if (!player.hasPermission("kcore.skins.update")) {
                player.sendMessage("§cVocê precisa ter algum plano VIP para executar este comando.");
                return;
              }
              Listeners.SKINS.add(player.getName());
              TextComponent component = new TextComponent("");
              for (BaseComponent components : TextComponent.fromLegacyText("\n§aDigite a sua nova skin no chat ou clique ")) {
                component.addExtra(components);
              }
              TextComponent click = new TextComponent("AQUI");
              click.setColor(ChatColor.GREEN);
              click.setBold(true);
              click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "skin:cancel"));
              click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique aqui para cancelar a troca de skin.")));
              component.addExtra(click);
              for (BaseComponent components : TextComponent.fromLegacyText("§a para cancelar.\n ")) {
                component.addExtra(components);
              }

              player.spigot().sendMessage(component);
            } else if (evt.getSlot() == (this.rows * 9) - 5) {
              player.closeInventory();
              player.chat("/skin atualizar");
            } else if (evt.getSlot() == (this.rows * 9) - 4) {
              player.closeInventory();
              player.chat("/skin ajuda");
            } else if (skinName != null) {
              SkinsContainer container = profile.getSkinsContainer();
              SkinsContainer listContainer = profile.getSkinListContainer();
              if (evt.getClick().equals(ClickType.SHIFT_RIGHT) && !container.getSkin().equals(skinName)) {
                listContainer.removeSkin(skinName);
                player.sendMessage("§cSkin removida com sucesso.");
                new MenuSkins(profile);
              } else {
                if (!container.getSkin().equals(skinName)) {
                  player.chat("/skin " + skinName);
                  container.setSkin(skinName);
                  player.closeInventory();
                }
              }
            }
          }
        }
      }
    }
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
    this.skins.clear();
    this.skins = null;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
      this.cancel();
    }
  }
}
