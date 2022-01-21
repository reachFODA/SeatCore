package dev.slickcollections.kiwizin.servers;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.servers.balancer.BaseBalancer;
import dev.slickcollections.kiwizin.servers.balancer.Server;
import dev.slickcollections.kiwizin.servers.balancer.type.LeastConnection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ServerItem {
  
  public static final KConfig CONFIG = Core.getInstance().getConfig("servers");
  public static final List<Integer> DISABLED_SLOTS = CONFIG.getIntegerList("disabled-slots");
  
  public static final Map<String, Integer> SERVER_COUNT = new HashMap<>();
  private static final List<ServerItem> SERVERS = new ArrayList<>();
  
  private final String key;
  private final int slot;
  private final String icon;
  private String options;
  private final BaseBalancer<Server> balancer;
  
  public ServerItem(String key, int slot, String icon, String options, BaseBalancer<Server> baseBalancer) {
    this.key = key;
    this.slot = slot;
    this.icon = icon;
    this.options = options;
    if (this.options == null) {
      this.options = "action>NORMAL : cancel>false";
    }
    this.balancer = baseBalancer;
  }
  
  public static void setupServers() {
    for (String key : CONFIG.getSection("items").getKeys(false)) {
      ServerItem si = new ServerItem(key, CONFIG.getInt("items." + key + ".slot"), CONFIG.getString("items." + key + ".icon"), CONFIG.getString("items." + key + ".options"), new LeastConnection<>());
      SERVERS.add(si);
      CONFIG.getStringList("items." + key + ".servernames").forEach(server -> {
        if (server.split(" ; ").length < 2) {
          return;
        }
        
        si.getBalancer().add(server, new Server(server.split(" ; ")[0],
            server.split(" ; ")[1], CONFIG.getInt("items." + key + ".max-players")));
      });
    }
    
    new BukkitRunnable() {
      @Override
      public void run() {
        SERVERS.forEach(server -> server.getBalancer().getList().forEach(Server::fetch));
      }
    }.runTaskTimerAsynchronously(Core.getInstance(), 0, 40);
  }
  
  public static Collection<ServerItem> listServers() {
    return SERVERS;
  }

  public static ServerItem getServerItem(String key) {
    return SERVERS.stream().filter(si -> si.getKey().equals(key)).findFirst().orElse(null);
  }

  public String getOptions() {
    return this.options;
  }

  public static boolean alreadyQuerying(String servername) {
    return SERVERS.stream().anyMatch(si -> si.getBalancer().keySet().contains(servername));
  }

  public static int getServerCount(ServerItem serverItem) {
    return serverItem.getBalancer().getTotalNumber();
  }
  
  public static int getServerCount(String servername) {
    return SERVER_COUNT.get(servername) == null ? 0 : SERVER_COUNT.get(servername);
  }
  
  public void connect(Profile profile) {
    Server server = balancer.next();
    if (server != null) {
      Core.sendServer(profile, server.getName());
    } else {
      profile.getPlayer().sendMessage("§cNão foi possível se conectar a esse servidor no momento!");
    }
  }
  
  public String getKey() {
    return this.key;
  }
  
  public int getSlot() {
    return this.slot;
  }
  
  public String getIcon() {
    return this.icon;
  }
  
  public BaseBalancer<Server> getBalancer() {
    return this.balancer;
  }
}
