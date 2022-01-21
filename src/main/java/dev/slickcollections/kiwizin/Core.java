package dev.slickcollections.kiwizin;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.slickcollections.kiwizin.achievements.Achievement;
import dev.slickcollections.kiwizin.booster.Booster;
import dev.slickcollections.kiwizin.cmd.Commands;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.deliveries.Delivery;
import dev.slickcollections.kiwizin.hook.KCoreExpansion;
import dev.slickcollections.kiwizin.hook.protocollib.FakeAdapter;
import dev.slickcollections.kiwizin.hook.protocollib.HologramAdapter;
import dev.slickcollections.kiwizin.hook.protocollib.NPCAdapter;
import dev.slickcollections.kiwizin.libraries.MinecraftVersion;
import dev.slickcollections.kiwizin.libraries.holograms.HologramLibrary;
import dev.slickcollections.kiwizin.libraries.npclib.NPCLibrary;
import dev.slickcollections.kiwizin.listeners.Listeners;
import dev.slickcollections.kiwizin.listeners.PluginMessageListener;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.fake.FakeManager;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.player.vanish.Vanish;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.servers.ServerItem;
import dev.slickcollections.kiwizin.titles.Title;
import dev.slickcollections.kiwizin.utils.SlickUpdater;
import dev.slickcollections.kiwizin.utils.queue.Queue;
import dev.slickcollections.kiwizin.utils.queue.QueuePlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class Core extends KPlugin {
  
  public static final List<String> warnings = new ArrayList<>();
  public static final List<String> minigames = Arrays.asList("Sky Wars", "The Bridge", "Bed Wars");
  
  public static boolean validInit;
  public static String minigame = "";
  
  private static Core instance;
  private static Location lobby;
  
  public static Location getLobby() {
    return lobby;
  }
  
  public static void setLobby(Location location) {
    lobby = location;
  }
  
  public static Core getInstance() {
    return instance;
  }
  
  public static void sendServer(Profile profile, String name) {
    if (!Core.getInstance().isEnabled()) {
      return;
    }
    
    Player player = profile.getPlayer();
    if (Core.getInstance().getConfig("utils").getBoolean("queue")) {
      if (player != null) {
        player.closeInventory();
        Queue queue = player.hasPermission("kcore.queue") ? Queue.VIP : Queue.MEMBER;
        QueuePlayer qp = queue.getQueuePlayer(player);
        if (qp != null) {
          if (qp.server.equalsIgnoreCase(name)) {
            qp.player.sendMessage("§cVocê já está na fila de conexão!");
          } else {
            qp.server = name;
          }
          return;
        }
        
        queue.queue(player, profile, name);
      }
    } else {
      if (player != null) {
        Profile.getProfile(player.getName()).saveSync();
        Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
          if (player.isOnline()) {
            player.closeInventory();
            NMS.sendActionBar(player, "");
            player.sendMessage("§aConectando...");
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(name);
            player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
          }
        });
      }
    }
  }
  
  @Override
  public void start() {
    instance = this;
  }
  
  @Override
  public void load() {}
  
  @Override
  public void enable() {
    if (!NMS.setupNMS()) {
      this.setEnabled(false);
      this.getLogger().warning("A sua versao nao e compativel com o plugin, utilize a versao 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }
    
    saveDefaultConfig();
    lobby = Bukkit.getWorlds().get(0).getSpawnLocation();
    
    // Remover o spawn-protection-size
    if (Bukkit.getSpawnRadius() != 0) {
      Bukkit.setSpawnRadius(0);
    }
    
    // Plugins que causaram incompatibilidades
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getResource("blacklist.txt"), StandardCharsets.UTF_8))) {
      String plugin;
      while ((plugin = reader.readLine()) != null) {
        if (Bukkit.getPluginManager().getPlugin(plugin.split(" ")[0]) != null) {
          warnings.add(" - " + plugin);
        }
      }
    } catch (IOException ex) {
      this.getLogger().log(Level.SEVERE, "Cannot load blacklist.txt: ", ex);
    }
    
    if (!warnings.isEmpty()) {
      CommandSender sender = Bukkit.getConsoleSender();
      StringBuilder sb = new StringBuilder(" \n §6§lAVISO IMPORTANTE\n \n §7Aparentemente você utiliza plugins que conflitam com o kCore.\n §7Você não poderá iniciar o servidor com os seguintes plugins:");
      for (String warning : warnings) {
        sb.append("\n§f").append(warning);
      }
      sb.append("\n ");
      sender.sendMessage(sb.toString());
      System.exit(0);
      return;
    }
    
    // Remover /reload
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
      field.setAccessible(true);
      Map<String, Command> knownCommands = (Map<String, Command>) field.get(simpleCommandMap);
      knownCommands.remove("rl");
      knownCommands.remove("reload");
      knownCommands.remove("bukkit:rl");
      knownCommands.remove("bukkit:reload");
    } catch (ReflectiveOperationException ex) {
      getLogger().log(Level.SEVERE, "Cannot remove reload command: ", ex);
    }
    
    if (!PlaceholderAPIPlugin.getInstance().getDescription().getVersion().equals("2.10.5")) {
      Bukkit.getConsoleSender().sendMessage(
          " \n §6§lAVISO IMPORTANTE\n \n §7Utilize a versão 2.10.5 do PlaceHolderAPI, você está utilizando a v" +
              PlaceholderAPIPlugin.getInstance().getDescription().getVersion() + "\n ");
      System.exit(0);
      return;
    }
    
    PlaceholderAPI.registerExpansion(new KCoreExpansion());
    
    Database.setupDatabase(
        getConfig().getString("database.tipo"),
        getConfig().getString("database.mysql.host"),
        getConfig().getString("database.mysql.porta"),
        getConfig().getString("database.mysql.nome"),
        getConfig().getString("database.mysql.usuario"),
        getConfig().getString("database.mysql.senha"),
        getConfig().getBoolean("database.mysql.hikari", false),
        getConfig().getBoolean("database.mysql.mariadb", false),
        getConfig().getString("database.mongodb.url", "")
    );
    
    NPCLibrary.setupNPCs(this);
    HologramLibrary.setupHolograms(this);
    
    setupRoles();
    FakeManager.setupFake();
    Title.setupTitles();
    Booster.setupBoosters();
    Vanish.setup();
    Delivery.setupDeliveries();
    ServerItem.setupServers();
    Achievement.setupAchievements();
    
    Commands.setupCommands();
    Listeners.setupListeners();
    
    ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
    
    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerOutgoingPluginChannel(this, "kCore");
    getServer().getMessenger().registerIncomingPluginChannel(this, "kCore", new PluginMessageListener());
    
    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }
  
  @Override
  public void disable() {
    if (validInit) {
      Bukkit.getOnlinePlayers().forEach(player -> {
        Profile profile = Profile.unloadProfile(player.getName());
        if (profile != null) {
          profile.saveSync();
          this.getLogger().info("O perfil " + profile.getName() + " foi salvo!");
          profile.destroy();
        }
      });
      Database.getInstance().close();
    }
    this.getLogger().info("O plugin foi desativado.");
  }
  
  private void setupRoles() {
    KConfig config = getConfig("roles");
    for (String key : config.getSection("roles").getKeys(false)) {
      String name = config.getString("roles." + key + ".name");
      String prefix = config.getString("roles." + key + ".prefix");
      String permission = config.getString("roles." + key + ".permission");
      boolean broadcast = config.getBoolean("roles." + key + ".broadcast", true);
      boolean alwaysVisible = config.getBoolean("roles." + key + ".alwaysvisible", false);
      
      Role.listRoles().add(new Role(name, prefix, permission, alwaysVisible, broadcast));
    }
    
    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Membro", "&7", "", false, false));
    }
  }
}
