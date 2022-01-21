package dev.slickcollections.kiwizin.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.nickuc.login.api.events.bungee.BungeeAuthenticateEvent;
import com.nickuc.login.api.events.bungee.BungeeLoginEvent;
import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.Manager;
import dev.slickcollections.kiwizin.database.data.container.SkinsContainer;
import dev.slickcollections.kiwizin.database.exception.ProfileLoadException;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.enums.ChatMention;
import dev.slickcollections.kiwizin.player.enums.PrivateMessages;
import dev.slickcollections.kiwizin.player.enums.ProtectionLobby;
import dev.slickcollections.kiwizin.player.fake.FakeManager;
import dev.slickcollections.kiwizin.player.hotbar.HotbarButton;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.reflection.Accessors;
import dev.slickcollections.kiwizin.reflection.acessors.FieldAccessor;
import dev.slickcollections.kiwizin.titles.TitleManager;
import dev.slickcollections.kiwizin.utils.SlickUpdater;
import dev.slickcollections.kiwizin.utils.StringUtils;
import dev.slickcollections.kiwizin.utils.Validator;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.spigotmc.WatchdogThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Listeners implements Listener {
  
  public static final KLogger LOGGER = ((KLogger) Core.getInstance().getLogger()).getModule("Listeners");
  public static final Map<String, Long> DELAY_PLAYERS = new HashMap<>();
  private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();
  
  private static final FieldAccessor<Map> COMMAND_MAP = Accessors.getField(SimpleCommandMap.class, "knownCommands", Map.class);
  private static final SimpleCommandMap SIMPLE_COMMAND_MAP = (SimpleCommandMap) Accessors.getMethod(Bukkit.getServer().getClass(), "getCommandMap").invoke(Bukkit.getServer());
  private static final FieldAccessor<WatchdogThread> RESTART_WATCHDOG = Accessors.getField(WatchdogThread.class, "instance", WatchdogThread.class);
  private static final FieldAccessor<Boolean> RESTART_WATCHDOG_STOPPING = Accessors.getField(WatchdogThread.class, "stopping", boolean.class);
  
  public static void setupListeners() {
    Bukkit.getPluginManager().registerEvents(new Listeners(), Core.getInstance());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
    if (evt.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
      try {
        Profile.createOrLoadProfile(evt.getName());
      } catch (ProfileLoadException ex) {
        LOGGER.log(Level.SEVERE, "Nao foi possível carregar os dados do perfil \"" + evt.getName() + "\": ", ex);
      }
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerLoginMonitor(PlayerLoginEvent evt) {
    Profile profile = Profile.getProfile(evt.getPlayer().getName());
    if (profile == null) {
      evt.disallow(PlayerLoginEvent.Result.KICK_OTHER,
          "§cAparentemente o servidor não conseguiu carregar seu Perfil.\n \n§cIsso ocorre normalmente quando o servidor ainda está despreparado para receber logins, aguarde um pouco e tente novamente.");
      return;
    }
    
    profile.setPlayer(evt.getPlayer());
    SkinsContainer container = Profile.getProfile(evt.getPlayer().getName()).getSkinsContainer();
    if (container != null) {
      String skinName = container.getSkin();

      if (skinName != null && container.getValue() != null && container.getSignature() != null && !skinName.equals("none")) {
        Property property = new Property("textures", container.getValue(), container.getSignature());
        GameProfile gameProfile = (GameProfile) Accessors
                .getMethod(evt.getPlayer().getClass(), GameProfile.class, "getProfile").invoke(evt.getPlayer());
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", property);
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerMove(PlayerMoveEvent evt) {
    Player player = evt.getPlayer();
    if (evt.getTo().getBlock().getRelative(BlockFace.DOWN)
        .getType() == Material.SLIME_BLOCK) {
      EnumSound.FIREWORK_LAUNCH.play(player, 1F, 1F);

      Vector vector = player.getEyeLocation().getDirection();
      vector.multiply(3.35F);
      vector.setY(1.15F);

      player.setVelocity(vector);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent evt) {
    Player player = evt.getPlayer();
    SkinsContainer container = Profile.getProfile(player.getName()).getSkinsContainer();
    if (container != null) {
      String skinName = container.getSkin();

      if (skinName != null && container.getValue() != null && container.getSignature() != null && !skinName.equals("none")) {
        Property property = new Property("textures", container.getValue(), container.getSignature());
        GameProfile gameProfile = (GameProfile) Accessors
            .getMethod(player.getClass(), GameProfile.class, "getProfile").invoke(player);
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", property);
      }
      NMS.refreshPlayer(player);
    }

    if (player.hasPermission("kcore.admin")) {
      if (SlickUpdater.UPDATER != null && SlickUpdater.UPDATER.canDownload) {
        TextComponent component = new TextComponent("");
        for (BaseComponent components : TextComponent.fromLegacyText("\n §6§lATUALIZAÇÃO \n \n §7Foi encontrado um update novo do §6kCore §f(" +
            SlickUpdater.getVersion(2) + ")§7, para atualizar basta clicar ")) {
          component.addExtra(components);
        }
        TextComponent click = new TextComponent("AQUI");
        click.setColor(ChatColor.GREEN);
        click.setBold(true);
        click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kc atualizar"));
        click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique aqui para atualizar o kCore.")));
        component.addExtra(click);
        for (BaseComponent components : TextComponent.fromLegacyText("§7.\n ")) {
          component.addExtra(components);
        }
        
        player.spigot().sendMessage(component);
        EnumSound.LEVEL_UP.play(player, 1.0F, 1.0F);
      }
    }
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    Profile profile = Profile.unloadProfile(evt.getPlayer().getName());
    if (profile != null) {
      if (profile.getGame() != null) {
        profile.getGame().leave(profile, profile.getGame());
      }
      TitleManager.leaveServer(profile);
      if (!((CraftServer) Bukkit.getServer()).getHandle().getServer().isRunning() || RESTART_WATCHDOG_STOPPING.get(RESTART_WATCHDOG.get(null))) {
        // server stopped - save SYNC
        profile.saveSync();
        Core.getInstance().getLogger().info("O perfil " + profile.getName() + " foi salvo!");
      } else {
        // server running - save ASYNC
        profile.save();
      }
      profile.destroy();
    }
    
    FakeManager.fakeNames.remove(evt.getPlayer().getName());
    FakeManager.fakeRoles.remove(evt.getPlayer().getName());
    FakeManager.fakeSkins.remove(evt.getPlayer().getName());
    DELAY_PLAYERS.remove(evt.getPlayer().getName());
    PROTECTION_LOBBY.remove(evt.getPlayer().getName().toLowerCase());
  }

  static public final List<String> SKINS = new ArrayList<>();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onAsyncPlayerChatSkin(AsyncPlayerChatEvent evt) {
    Player player = evt.getPlayer();
    if (SKINS.contains(player.getName())) {
      evt.setCancelled(true);
      String message = evt.getMessage();
      if (message.equalsIgnoreCase("skin:cancel")) {
        player.sendMessage("§cAção cancelada.");
        SKINS.remove(player.getName());
        return;
      }
      if (!Validator.isValidUsername(message)) {
        player.sendMessage("§cUtilize apenas caracteres válidos.");
        return;
      }
      Profile profile = Profile.getProfile(player.getName());
      player.sendMessage("§aAtualizando sua skin...");
      if (profile.getSkinListContainer().getSkins().size() > 20) {
        player.sendMessage("§cVocê atingiu o limite máximo de skins.");
        return;
      }
      profile.getSkinListContainer().addSkin(message);
      player.sendMessage("§aSua skin foi atualizada, relogue para ela aparecer.");
      profile.getSkinsContainer().setSkin(message);
      SKINS.remove(player.getName());
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
    if (evt.isCancelled()) {
      return;
    }
    
    Player player = evt.getPlayer();
    String format = String.format(evt.getFormat(), player.getName(), evt.getMessage());

    String current = Manager.getCurrent(player.getName());
    Role role = Role.getPlayerRole(player);

    TextComponent component = new TextComponent("");
    for (BaseComponent components : TextComponent.fromLegacyText(format)) {
      component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + current + " "));
      component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
          TextComponent.fromLegacyText(StringUtils.getLastColor(role.getPrefix()) + current + "\n§fGrupo: " + role.getName() + "\n \n§eClique para enviar uma mensagem privada.")));
      component.addExtra(components);
    }
    
    evt.setCancelled(true);
    evt.getRecipients().forEach(players -> {
      if (players != null) {

        TextComponent c = new TextComponent("");
        if (format.contains(players.getName()) && Profile.getProfile(players.getName())
                .getPreferencesContainer().getChatMention() == ChatMention.ATIVADO && players != player) {
          NMS.sendActionBar(players, Role.getColored(player.getName()) + " §emencionou você no chat!");

          EnumSound.ORB_PICKUP.play(players, 1F, 1F);
          for (BaseComponent components : TextComponent.fromLegacyText(format.replace(players.getName(), "§e@" + players.getName() +
                  (role.isDefault() ? "§7" : "§f")))) {
            c.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + current + " "));
            c.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText(StringUtils.getLastColor(role.getPrefix()) + current + "\n§fGrupo: " + role.getName() + "\n \n§eClique para enviar uma mensagem privada.")));
            c.addExtra(components);
          }
        } else {
          for (BaseComponent components : TextComponent.fromLegacyText(format)) {
            c.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + current + " "));
            c.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText(StringUtils.getLastColor(role.getPrefix()) + current + "\n§fGrupo: " + role.getName() + "\n \n§eClique para enviar uma mensagem privada.")));
            c.addExtra(components);
          }
        }

        players.spigot().sendMessage(c);
      }
    });
  }
  
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    if (evt.isCancelled()) {
      return;
    }
    
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null) {
      String[] args = evt.getMessage().replace("/", "").split(" ");
      
      if (args.length > 0) {
        String command = args[0];
        if (COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("lobby") && command.equals("lobby") && profile.getPreferencesContainer()
            .getProtectionLobby() == ProtectionLobby.ATIVADO) {
          long last = PROTECTION_LOBBY.getOrDefault(player.getName().toLowerCase(), 0L);
          if (last > System.currentTimeMillis()) {
            PROTECTION_LOBBY.remove(player.getName().toLowerCase());
            return;
          }
          
          evt.setCancelled(true);
          PROTECTION_LOBBY.put(player.getName().toLowerCase(), System.currentTimeMillis() + 3000);
          player.sendMessage("§aVocê tem certeza? Utilize /lobby novamente para voltar ao lobby.");
        } else if (COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("tell") && args.length > 1 && command.equals("tell") && !args[1].equalsIgnoreCase(player.getName())) {
          profile = Profile.getProfile(args[1]);
          if (profile != null && profile.getPreferencesContainer().getPrivateMessages() != PrivateMessages.TODOS) {
            evt.setCancelled(true);
            player.sendMessage("§cEste usuário desativou as mensagens privadas.");
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null && profile.getHotbar() != null) {
      ItemStack item = player.getItemInHand();
      if (evt.getAction().name().contains("CLICK") && item != null && item.hasItemMeta()) {
        HotbarButton button = profile.getHotbar().compareButton(player, item);
        if (button != null) {
          evt.setCancelled(true);
          button.getAction().execute(profile);
        }
      }
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent evt) {
    if (evt.getRightClicked() instanceof ArmorStand) {
      if (evt.getPlayer().getGameMode() == GameMode.ADVENTURE) {
        evt.setCancelled(true);
      }
    }
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getWhoClicked() instanceof Player) {
      Player player = (Player) evt.getWhoClicked();
      Profile profile = Profile.getProfile(player.getName());
      
      if (profile != null && profile.getHotbar() != null) {
        ItemStack item = evt.getCurrentItem();
        if (item != null && item.getType() != Material.AIR) {
          if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(player.getInventory()) && item.hasItemMeta()) {
            HotbarButton button = profile.getHotbar().compareButton(player, item);
            if (button != null) {
              evt.setCancelled(true);
              button.getAction().execute(profile);
            }
          }
        }
      }
    }
  }
}
