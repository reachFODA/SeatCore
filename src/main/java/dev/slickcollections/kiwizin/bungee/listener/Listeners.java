package dev.slickcollections.kiwizin.bungee.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.nickuc.login.api.events.bungee.BungeeAuthenticateEvent;
import dev.slickcollections.kiwizin.Manager;
import dev.slickcollections.kiwizin.bungee.Bungee;
import dev.slickcollections.kiwizin.bungee.party.BungeeParty;
import dev.slickcollections.kiwizin.bungee.party.BungeePartyManager;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.database.HikariDatabase;
import dev.slickcollections.kiwizin.database.MongoDBDatabase;
import dev.slickcollections.kiwizin.database.MySQLDatabase;
import dev.slickcollections.kiwizin.reflection.Accessors;
import dev.slickcollections.kiwizin.reflection.acessors.FieldAccessor;
import dev.slickcollections.kiwizin.utils.StringUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Listeners implements Listener {
  
  private static final Map<String, LoginResult.Property[]> PROPERTY_BACKUP = new HashMap<>();
  private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();
  private static final Map<String, Boolean> TELL_CACHE = new HashMap<>(), PROTECTION_CACHE = new HashMap<>();
  private static final FieldAccessor<Map> COMMAND_MAP = Accessors.getField(PluginManager.class, "commandMap", Map.class);
  
  @EventHandler
  public void onPlayerDisconnect(PlayerDisconnectEvent evt) {
    TELL_CACHE.remove(evt.getPlayer().getName().toLowerCase());
    PROTECTION_CACHE.remove(evt.getPlayer().getName().toLowerCase());
    PROTECTION_LOBBY.remove(evt.getPlayer().getName().toLowerCase());
    PROPERTY_BACKUP.remove(evt.getPlayer().getName().toLowerCase());
  }
  
  @EventHandler
  public void onPostLogin(PostLoginEvent evt) {
    TELL_CACHE.remove(evt.getPlayer().getName().toLowerCase());
    PROTECTION_CACHE.remove(evt.getPlayer().getName().toLowerCase());
  }
  
  @EventHandler
  public void onPluginMessage(PluginMessageEvent evt) {
    if (evt.getSender() instanceof ServerConnection && evt.getReceiver() instanceof ProxiedPlayer) {
      if (evt.getTag().equalsIgnoreCase("kCore")) {
        ProxiedPlayer player = (ProxiedPlayer) evt.getReceiver();
        
        ByteArrayDataInput in = ByteStreams.newDataInput(evt.getData());
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("FAKE_SKIN")) {
          LoginResult profile = ((InitialHandler) player.getPendingConnection()).getLoginProfile();
          if (profile != null) {
            try {
              String[] data = in.readUTF().split(":");
              PROPERTY_BACKUP.put(player.getName().toLowerCase(), profile.getProperties());
              this.modifyProperties(profile, data);
            } catch (Exception ex) {
              LoginResult.Property[] properties = PROPERTY_BACKUP.remove(player.getName().toLowerCase());
              if (properties != null) {
                profile.setProperties(properties);
              }
            }
          }
        }
      }
    }
  }

  public static Map<String, String> LAST_SERVER = new HashMap<>();

  @EventHandler
  public void onPlayerLogin(BungeeAuthenticateEvent evt) {
    ProxyServer.getInstance().getScheduler().schedule(Bungee.getInstance(), () -> {
      String name = evt.getPlayer();
      ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
      if (player == null) {
        return;
      }
      ServerInfo server = ProxyServer.getInstance().getServerInfo(LAST_SERVER.getOrDefault(name, "lobby"));
      player.connect(server.getName().equals("login") ? ProxyServer.getInstance().getServerInfo("lobby") : server);

    }, 20, TimeUnit.MILLISECONDS);
  }

  @EventHandler
  public void onPlayerChangeServer(ServerConnectEvent evt) {
    ServerInfo server = evt.getTarget();
    if (server.getName().equals("login")) {
      return;
    }
    LAST_SERVER.put(evt.getPlayer().getName(), server.getName());
  }


  @EventHandler
  public void onProxyPing(ProxyPingEvent evt) {
    ServerPing response = evt.getResponse();
    response.setDescriptionComponent(new TextComponent(StringUtils.formatColors(Bungee.getInstance().getConfig()
        .getString("motd").replace("\\n", "\n"))));
    evt.setResponse(response);
  }

  @EventHandler
  public void onLogin(LoginEvent evt) {
    if (Database.getInstance() instanceof MongoDBDatabase) {
      Bungee.getInstance().getLogger().warning("Nao foi possivel atualizar skin no bungeecord. Banco de dados nao suportado");
      return;
    }

    CachedRowSet cachedRowSet = ((HikariDatabase) Database.getInstance()).query("SELECT * FROM `kCoreSkins` WHERE LOWER(`name`) = ?", evt.getConnection().getName().toLowerCase());

    String skinInfo = null;
    try {
      skinInfo = cachedRowSet.getString("info");
    } catch (Exception ignore) {}

    if (skinInfo != null) {
      JSONObject result = null;
      try {
        result = (JSONObject) new JSONParser().parse(skinInfo);
      } catch (Exception ignored) {}

      if (result != null) {
        if (result.get("value").toString().equalsIgnoreCase("none") || result.get("signature").toString().equalsIgnoreCase("none")) {
          return;
        }
        String data = result.get("value").toString() + ":" + result.get("signature").toString();

        LoginResult profile = ((InitialHandler) evt.getConnection()).getLoginProfile();
        if (profile != null) {
          this.modifyProperties(profile, data.split(":"));
        }
      }
    }
  }

  @EventHandler(priority = (byte) 128)
  public void onServerConnected(ServerConnectedEvent evt) {
    ProxiedPlayer player = evt.getPlayer();

    BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
    if (party != null) {
      party.sendData(evt.getServer().getInfo());
    }

    if (Bungee.isFake(player.getName())) {
      String skin = Bungee.getSkin(player.getName());
      // Enviar dados desse jogador que está utilizando Fake para o servidor processar.
      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      out.writeUTF("FAKE");
      out.writeUTF(player.getName());
      out.writeUTF(Bungee.getFake(player.getName()));
      out.writeUTF(StringUtils.stripColors(Bungee.getRole(player.getName()).getName()));
      out.writeUTF(skin);
      evt.getServer().sendData("kCore", out.toByteArray());

      LoginResult profile = ((InitialHandler) player.getPendingConnection()).getLoginProfile();
      if (profile != null) {
        this.modifyProperties(profile, skin.split(":"));
      }
    }
  }

  @EventHandler(priority = (byte) 128)
  public void onChat(ChatEvent evt) {
    if (evt.getSender() instanceof ProxiedPlayer) {
      if (evt.isCommand()) {
        ProxiedPlayer player = (UserConnection) evt.getSender();
        String[] args = evt.getMessage().replace("/", "").split(" ");
        
        String command = args[0];
        if (COMMAND_MAP.get(ProxyServer.getInstance().getPluginManager()).containsKey("lobby") && command.equals("lobby") && this
            .hasProtectionLobby(player.getName().toLowerCase())) {
          long last = PROTECTION_LOBBY.getOrDefault(player.getName().toLowerCase(), 0L);
          if (last > System.currentTimeMillis()) {
            PROTECTION_LOBBY.remove(player.getName().toLowerCase());
            return;
          }
          
          evt.setCancelled(true);
          PROTECTION_LOBBY.put(player.getName().toLowerCase(), System.currentTimeMillis() + 3000);
          player.sendMessage(TextComponent.fromLegacyText("§aVocê tem certeza? Utilize /lobby novamente para voltar ao lobby."));
        } else if (COMMAND_MAP.get(ProxyServer.getInstance().getPluginManager()).containsKey("tell") && args.length > 1 && command.equals("tell") && !args[1]
            .equalsIgnoreCase(player.getName())) {
          if (!this.canReceiveTell(args[1].toLowerCase())) {
            evt.setCancelled(true);
            player.sendMessage(TextComponent.fromLegacyText("§cEste usuário desativou as mensagens privadas."));
          }
        }
      }
    }
  }
  
  private void modifyProperties(LoginResult profile, String[] data) {
    List<LoginResult.Property> properties = new ArrayList<>();
    for (LoginResult.Property property : profile.getProperties() == null ? new ArrayList<LoginResult.Property>() : Arrays.asList(profile.getProperties())) {
      if (property.getName().equalsIgnoreCase("textures")) {
        continue;
      }
      
      properties.add(property);
    }

    properties.add(new LoginResult.Property("textures", data[0], data[1]));
    profile.setProperties(properties.toArray(new LoginResult.Property[0]));
  }
  
  private boolean canReceiveTell(String name) {
    if (TELL_CACHE.containsKey(name)) {
      return TELL_CACHE.get(name);
    }
    
    boolean canReceiveTell = Database.getInstance().getPreference(name, "pm", true);
    TELL_CACHE.put(name, canReceiveTell);
    return canReceiveTell;
  }
  
  private boolean hasProtectionLobby(String name) {
    if (PROTECTION_CACHE.containsKey(name)) {
      return PROTECTION_CACHE.get(name);
    }
    
    boolean hasProtectionLobby = Database.getInstance().getPreference(name, "pl", true);
    PROTECTION_CACHE.put(name, hasProtectionLobby);
    return hasProtectionLobby;
  }
}
