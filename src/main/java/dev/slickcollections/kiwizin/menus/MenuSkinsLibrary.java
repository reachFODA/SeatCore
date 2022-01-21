package dev.slickcollections.kiwizin.menus;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.database.data.container.SkinsContainer;
import dev.slickcollections.kiwizin.libraries.menu.PagedPlayerMenu;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuSkinsLibrary extends PagedPlayerMenu {

  public static Map<String, String> LIBRARY = new HashMap<>();

  static {
    LIBRARY.put("Nezuko", "Nezuko______Chan:ewogICJ0aW1lc3RhbXAiIDogMTYxOTU1MjY5MjEwNSwKICAicHJvZmlsZUlkIiA6ICI4OGU0YWNiYTQwOTc0YWZkYmE0ZDM1YjdlYzdmNmJmYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJKb2FvMDkxNSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81OTAzYjY1MGIwYTk1ZTAyMDEyYTUzYTU0N2YzMjc0YzM5MDVkNDE4OTJkZDc0YmY2YTRjNmJhOWI1NjE3YmIyIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0");
    LIBRARY.put("Tanjiro", "Tanjxro_:ewogICJ0aW1lc3RhbXAiIDogMTYzMjg0MjUxNjQxOSwKICAicHJvZmlsZUlkIiA6ICJmMjU5MTFiOTZkZDU0MjJhYTcwNzNiOTBmOGI4MTUyMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJmYXJsb3VjaDEwMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xOTdkYjUxMjE2Y2U1ZDZjOTQ0N2M1OGUzZGNkZWNhYmJhZDQ0ODJkMzY2OWRmNDE0NDA3NjhjMTYwYmY4MGRhIgogICAgfQogIH0KfQ==");
    LIBRARY.put("Akame", "EgirlFxse:ewogICJ0aW1lc3RhbXAiIDogMTY0MjcyOTM2OTk3NSwKICAicHJvZmlsZUlkIiA6ICJhOGJhMGY1YTFmNjQ0MTgzODZkZGI3OWExZmY5ZWRlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmVlcGVyOTA3NSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iZDZmODBlYTJiNjc2ZGFjZGUyZTU3NmQxOTdmMmQwMTQyZTYwNTQ2NDQ1NDZjNzQ5NDdlZDQ3OGM1YTU0ZjIwIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
    LIBRARY.put("Tatsumi", "Tatsuumiii:ewogICJ0aW1lc3RhbXAiIDogMTY0MjcyOTU2MjQxMiwKICAicHJvZmlsZUlkIiA6ICJiYjdjY2E3MTA0MzQ0NDEyOGQzMDg5ZTEzYmRmYWI1OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsYXVyZW5jaW8zMDMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcwYzNkOWRlMDdiNTUzYjcyMGVjNmFlYzdiYTc0NDFkYmRlNWUzZjI3MWI4YzIwY2UzODQzYjQ0ZTYxOTg4YSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
    LIBRARY.put("Venom", "Venoms_GM:ewogICJ0aW1lc3RhbXAiIDogMTYyMDI0OTIyOTkxNCwKICAicHJvZmlsZUlkIiA6ICIzOTg5OGFiODFmMjU0NmQxOGIyY2ExMTE1MDRkZGU1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNeVV1aWRJcyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zM2E3NTIxMzMxOTFhMjY3NDIzYzQ3NWJmOTg0NzBjMTE1ZDAxOTYwMWMwYTRmZGZmNjBhMmI0MDBmYjdlODc3IgogICAgfQogIH0KfQ==");
    LIBRARY.put("Goku", "Oyo_Boyo:ewogICJ0aW1lc3RhbXAiIDogMTYxNzM0MTQ0NjczMSwKICAicHJvZmlsZUlkIiA6ICI1NjY3NWIyMjMyZjA0ZWUwODkxNzllOWM5MjA2Y2ZlOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVJbmRyYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NDAzOTdlYzQ1MmQ2NmFjOThhMGQyNGRkNDc0ZmY3ZThhODFjNGNlMzdhNGEwNDRlNjQ5YTFmNTFjYTdjYzI4IgogICAgfQogIH0KfQ==");
    LIBRARY.put("Naruto", "Naturc:ewogICJ0aW1lc3RhbXAiIDogMTYyNTc1NDgxMDc3NCwKICAicHJvZmlsZUlkIiA6ICIwNjNhMTc2Y2RkMTU0ODRiYjU1MjRhNjQyMGM1YjdhNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJkYXZpcGF0dXJ5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRkNTM1NjgwZWJkN2M4OGMxNDgyMzU5YWFlZmVjMTg2OTVhNTlmMzcwYTZkN2Y3ZDJlYTZkY2I5MjQ5OWY1MzMiCiAgICB9CiAgfQp9");
    LIBRARY.put("Homem Aranha", "700c:ewogICJ0aW1lc3RhbXAiIDogMTY0MDI5NTExNDYzOSwKICAicHJvZmlsZUlkIiA6ICI2MzMyMDgwZTY3YTI0Y2MxYjE3ZGJhNzZmM2MwMGYxZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUZWFtSHlkcmEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDVlMzIzODNhNGMyOGFhODFjYjIzYWUzYWNhY2JlNTc0N2EyOTNkN2VhOTRiZjhhNDQ2OTkyZmIzMGU5NTI4NyIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
    LIBRARY.put("Homem de Ferro", "6ixsauer:eyJ0aW1lc3RhbXAiOjE1NzcyNDMwODk4NjIsInByb2ZpbGVJZCI6IjIzZjFhNTlmNDY5YjQzZGRiZGI1MzdiZmVjMTA0NzFmIiwicHJvZmlsZU5hbWUiOiIyODA3Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iMTI5ODg0NTkxM2VmNmZkN2JlYTc2NmUxY2YyYTEzMTczNDFhOWZlYTcxYjE2M2E0NGZkMTk3NTM4NmQ5ZDEyIn19fQ==");
    LIBRARY.put("Jiraya", "Zakharov:ewogICJ0aW1lc3RhbXAiIDogMTYzMTk3MjkyNTkxOCwKICAicHJvZmlsZUlkIiA6ICJjMGYzYjI3YTUwMDE0YzVhYjIxZDc5ZGRlMTAxZGZlMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDEzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZkM2JlNmNhMWI3MGRjOGQ5ZTE3ZWNkZjg3MjBmYWIzYWVhYTNkYTU1NmE2N2ZmNjk5ZjllYjEyMWExZmM4NjkiCiAgICB9CiAgfQp9");
    LIBRARY.put("Kakashi", "eZio789:ewogICJ0aW1lc3RhbXAiIDogMTYyNTM0OTI4OTgxMCwKICAicHJvZmlsZUlkIiA6ICI5ZDQyNWFiOGFmZjg0MGU1OWM3NzUzZjc5Mjg5YjMyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJUb21wa2luNDIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiYzU3MDBkODBhMGQ1ZDcyYWE2MDE3MDk2ZmNhNmI5ZjA3MjI2YTVkNWFkYThmZTYxZDhhYWNhMzU2NjRjMiIKICAgIH0KICB9Cn0=");
    LIBRARY.put("Obito", "sharinganek:ewogICJ0aW1lc3RhbXAiIDogMTYyNjA3ODAxMDY2OCwKICAicHJvZmlsZUlkIiA6ICIxOTI1MjFiNGVmZGI0MjVjODkzMWYwMmE4NDk2ZTExYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTZXJpYWxpemFibGUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc4MWRkYzNjMDBjM2VlMWYzMzBjZTVlZDlmODhlMTNhNWFhODMxNTk3Yjk1YjliMjBkYjc0NmY2M2I5ODJmNCIKICAgIH0KICB9Cn0=");
    LIBRARY.put("Mitsuki", "iXwin:ewogICJ0aW1lc3RhbXAiIDogMTYyNjI2NTY3MDk1NiwKICAicHJvZmlsZUlkIiA6ICIxOTI1MjFiNGVmZGI0MjVjODkzMWYwMmE4NDk2ZTExYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTZXJpYWxpemFibGUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFmZDA0ZDZkMGVlODcyMzUwZDJhMmE3NmU2MGMzOTkzNWQxMTYxNWMyN2MxNjZkMTYwY2JkYjU3ZmI0MmJhZCIKICAgIH0KICB9Cn0=");
  }

  private Map<ItemStack, String> skins = new HashMap<>();

  public MenuSkinsLibrary(Profile profile) {
    super(profile.getPlayer(), "Biblioteca", (LIBRARY.size() / 7) + 4);
    this.previousPage = (this.rows * 9) - 9;
    this.nextPage = (this.rows * 9) - 1;
    this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

    SkinsContainer skinsContainer = profile.getSkinsContainer();

    String selectedSkin = skinsContainer == null ? "none" : skinsContainer.getSkin();

    List<ItemStack> items = new ArrayList<>();
    LIBRARY.forEach((key, value) -> {
      String skin = value.split(":")[1];
      ItemStack icon = BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : skin>" + skin + " : nome>&a" + key
          + " : desc>&7Altere sua skin para " + key + ".\n \n" + (selectedSkin.equals(value.split(":")[0]) ? "§eSelecionado." : "§eClique para alterar!"));
      items.add(icon);
      this.skins.put(icon, key);
    });

    this.removeSlotsWith(BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar"), (this.rows * 9) - 5);

    this.setItems(items);
    items.clear();

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
            String skin = this.skins.get(item);

            if (evt.getSlot() == (this.rows * 9) - 5) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuSkins(profile);
            } else if (skin != null) {
              SkinsContainer container = profile.getSkinsContainer();
              SkinsContainer listContainer = profile.getSkinListContainer();
              String skinName = LIBRARY.get(skin).split(":")[0];

              if (!container.getSkin().equals(skinName)) {
                player.chat("/skin library " + skin);
                container.setSkin(skinName);
                listContainer.addSkin(skinName);
                player.closeInventory();
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
